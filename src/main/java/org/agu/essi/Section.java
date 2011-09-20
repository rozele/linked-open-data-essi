package org.agu.essi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Section {
	private String _name;
	private String _id;
	private Meeting _meeting;
	
	public Section(String name, Meeting meeting)
	{
		Pattern p = Pattern.compile("(.*?)\\s*\\((.*)\\)");
		Matcher m = p.matcher(name);
		_name = m.group(1);
		_id = m.group(2);
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
