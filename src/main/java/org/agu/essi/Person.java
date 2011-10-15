package org.agu.essi;

import java.util.regex.*;

/**
 * Container class for person information
 * @author Eric Rozell
 */
public class Person 
{
	private String _name;
	private String _email;
	
	/**
	 * Construct Person instance from name
	 * @param name name of person
	 */
	public Person(String name)
	{
		Pattern p = Pattern.compile("(\\*\\s*)?(.*)");
		Matcher m = p.matcher(name);
		boolean matched = m.find();
		if (matched)
		{
			_name = m.group(2);
		}
		else
		{
			_name = name;
		}
	}
	
	/**
	 * Construct Person instance from name and email
	 * @param name name of person
	 * @param email email of person
	 */
	public Person(String name, String email)
	{
		Pattern p = Pattern.compile("(\\*\\s*)?(.*)");
		Matcher m = p.matcher(name);
		boolean matched = m.find();
		if (matched)
		{
			_name = m.group(2);
		}
		else
		{
			_name = name;
		}
		_email = email;
	}
	
	/**
	 * Method to add an email for an existing author
	 * @param email email of author
	 */
	public void addEmail(String email)
	{
		_email = email;	
	}
	
	/**
	 * Method to get author name
	 * @return name of author
	 */
	public String getName()
	{
		return _name;
	}
	
	/**
	 * Method to get last name of author
	 * @return last name of author
	 */
	public String getLastName()
	{
		String[] arr = _name.split(",");
		if (arr.length > 1)
		{
			return arr[0];
		}
		else
		{
			arr = _name.split(" ");
			return arr[arr.length - 1];
		}
	}
	
	/**
	 * Method to get author email
	 * @return email of author
	 */
	public String getEmail()
	{
		return _email;
	}
	
	/**
	 * Equality method for Person
	 * @return true if name and email are equivalent, false otherwise
	 */
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Person)
		{
			Person p = (Person)o;
			if (p.getEmail() != null && this._email != null && p.getEmail().equalsIgnoreCase(this._email))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
}
