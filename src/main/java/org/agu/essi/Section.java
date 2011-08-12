package org.agu.essi;

public class Section {
	private String _name;
	private String _id;
	private Meeting _meeting;
	
	public Section(String name, Meeting meeting)
	{
		_name = name;
		_meeting = meeting;
	}
	
	public String getName()
	{
		return _name;
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
