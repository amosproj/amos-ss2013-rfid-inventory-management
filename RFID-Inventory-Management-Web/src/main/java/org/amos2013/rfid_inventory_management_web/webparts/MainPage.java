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

package org.amos2013.rfid_inventory_management_web.webparts;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.util.file.File;

/**
 * This class defines a website
 */
public abstract class MainPage extends WebPage {
	
	private static final long serialVersionUID = 6545187642710272786L;

	/**
	 * The constructor creates a parent-website which contains a menu to access several sub-websites and files
	 */
	public MainPage() throws IOException
	{
		add(new BookmarkablePageLink<Object>("listpage", ListPage.class));
		add(new BookmarkablePageLink<Object>("adminpage", AdminPage.class));
		
		// adds a label for downloading a particular resource
		URL url = getClass().getResource("/RFID-Inventory-Management-App.apk");
		if (url == null)
			throw new FileNotFoundException("Resource `RFID-Inventory-Management-App.apk' not found.");
		File file = new File(URLDecoder.decode(url.getFile(), "UTF8"));
		DownloadLink dl = new DownloadLink("download", file);
		add(dl);
	}
}