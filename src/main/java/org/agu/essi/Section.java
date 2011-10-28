/**
 * Copyright (C) 2011 Tom Narock and Eric Rozell
 *
 *     This software is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.agu.essi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Container class for AGU Section information
 * @author Eric Rozell
 */
public class Section 
{
	private String _name;
	private String _id;
	private Meeting _meeting;

	public Section(String name, String id, Meeting meeting)
	{
		_name = name;
		_id = id;
		_meeting = meeting;
	}
	
	public Section(String name, Meeting meeting)
	{
		Pattern p1 = Pattern.compile("(.*?)\\s*\\((.*)\\)");
		Pattern p2 = Pattern.compile("(.*?)\\s*\\[(.*)\\]");
		Matcher m = p1.matcher(name);
		boolean matched = m.find();
		if (matched)
		{
			_name = m.group(1);
			_id = m.group(2);
		}
		else
		{
			m = p2.matcher(name);
			matched = m.find();
			if (matched)
			{
				_name = m.group(1);
				_id = m.group(2);
			}
			else
			{
				_name = name;
			}
		}
		_meeting = meeting;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public String getId()
	{
		return _id;
	}
	
	public Meeting getMeeting()
	{
		return _meeting;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Section && ((Section)o).getName().equals(this._name)
			&& ((Section)o).getMeeting().equals(this._meeting))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
