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

package de.fau.cs.amos2013.proj8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;

/**
 * TODO comment
 */
public class LocForm extends Form<Object>
{
	private static final long serialVersionUID = 1L;

	private String room;
	private String owner;
	private String status;
	private String result = " ";

	/**
	 * TODO comment
	 */
	public LocForm(String id)
	{
		super(id);
		setDefaultModel(new CompoundPropertyModel<Object>(this));
		add(new TextField<Object>("room"));
		add(new TextField<Object>("owner"));
		add(new Label("status"));

		try
		{
			result = TestAccess.Result();
		} 
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<String> myList = new ArrayList<String>(Arrays.asList(result.split(";")));
		add(new ListView<String>("listview", myList)
		{
			private static final long serialVersionUID = 1L;

			protected void populateItem(ListItem<String> item)
			{
				item.add(new Label("label", item.getModel()));
			}
		});
	}

	/**
	 * TODO comment
	 */
	public final void onSubmit()
	{
		try
		{
			TestDatabase.TestDatabase(room, owner);
			status = "Data saved - please refresh view!";
		} 
		catch (Exception e)
		{
			status = "An error occured!";
			e.printStackTrace();
		}
	}
}