/*
 * Copyright (c) 2013 by
 * AMOS 2013 Group 8: RFID Inventory Management (Elektrobit)
 *
 * POs:
 *  Andreas Lutz
 *  Jana Riechert
 *  Kerstin Stern
 *
 * SDs:
 *  Andreas Singer
 *  Liping Wang
 *  David Lehmeier
 *
 * This file is part of the RFID Inventory Management application.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package org.amos2013.rfid_inventory_management_web.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * This class is used, to access the database
 */
public class MetaDeviceDatabaseHandler
{
	private final static String DATABASE_URL = "jdbc:postgresql://faui2o2j.informatik.uni-erlangen.de:5432/ss13-proj8";

	// the class ConfigLoader.java which loads the db-password, is not committed to git
	private final static String DATABASE_PW = ConfigLoader.getDatabasePassword();

	// Database Access Object, is a handler for reading and writing
	private static Dao<MetaDeviceDatabaseRecord, Integer> databaseHandlerDao;


	/**
	 * Creates a database if there is no one existing
	 * @param connectionSource required for setting up db
	 * @throws Exception
	 */
	private static void setupDatabase(ConnectionSource connectionSource) throws Exception
	{
		databaseHandlerDao = DaoManager.createDao(connectionSource, MetaDeviceDatabaseRecord.class);

		// if the database is not existing create a new one
		if (databaseHandlerDao.isTableExists() == false)
		{
			try
			{
				// createTableIfNotExists is not working: always tries to create a new database
				// -> if block around
				TableUtils.createTableIfNotExists(connectionSource, MetaDeviceDatabaseRecord.class);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}


	/**
	 * Writes the given strings to the database
	 * @param category 		the category of the hardware e.g. phone
	 * @param type			the type of the hardware e.g. Lumia 900
	 * @param part_number	the part number e.g. EXG-1278-L900
	 * @param manufacturer	the manufacturer e.g. Nokia
	 * @param platform 		the platform/OS running on the hardware e.g. Windows Phone 8
	 * @throws SQLException when database connection close() fails
	 * @throws IllegalArgumentException when room or owner is null
	 * @throws Exception when database setup fails
	 */
	public static void writeRecordToDatabase(String category, String type, String part_number, String manufacturer, String platform) throws SQLException, IllegalArgumentException, Exception
	{
		if (part_number == null)
		{
			throw new IllegalArgumentException("At least one of the arguments for creating a DatabaseRecord is invalid");
		}

		ConnectionSource connectionSource = null;
		try
		{
			// create data-source for the database
			connectionSource = new JdbcConnectionSource(DATABASE_URL, "ss13-proj8", DATABASE_PW);
			// create a database, if non existing
			setupDatabase(connectionSource);

			// write the given Strings
			MetaDeviceDatabaseRecord record = new MetaDeviceDatabaseRecord(category, type, part_number, manufacturer, platform);

			// writes to the database: create if new id, or update if existing
			databaseHandlerDao.createOrUpdate(record);
		}
		finally
		{
			// destroy the data source which should close underlying connections
			if (connectionSource != null)
			{
				connectionSource.close();
			}
		}
	}


	/**
	 * Searches and returns a record that match the part_number given
	 * @param part_number the part number that is searched for
	 * @return the MetaDeviceDatabaseRecord that was searched for, null when nothing was found
	 * @throws SQLException when error occurs with the database
	 * @throws IllegalStateException when null or more than one record is returned from the database
	 */
	public static MetaDeviceDatabaseRecord getRecordFromDatabaseByPartNumber(String part_number) throws IllegalStateException, SQLException
	{
		ConnectionSource connectionSource = null;
		List<MetaDeviceDatabaseRecord> recordList = null;
		MetaDeviceDatabaseRecord nullRecord = null;

		try
		{
			// create our data-source for the database (url, user, pwd)
			connectionSource = new JdbcConnectionSource(DATABASE_URL, "ss13-proj8", DATABASE_PW);
			// setup our database and DAOs
			databaseHandlerDao = DaoManager.createDao(connectionSource, MetaDeviceDatabaseRecord.class);

			if (part_number != null)
			{
			recordList = databaseHandlerDao.queryForEq("part_number", part_number);
			}

		}
		finally
		{
			// always destroy the data source which should close underlying connections
			if (connectionSource != null)
			{
				connectionSource.close();
			}
		}

		// if there is no meta data for this unique device
		if (part_number == null)
		{
			return nullRecord;
		}
		
		// if empty database, return null
		if (recordList == null || recordList.toString().equals("[]"))
		{
			throw new IllegalStateException("Error while serarching for " + part_number + ": no object was returned!");
		}

		return recordList.get(0);
	}
}
