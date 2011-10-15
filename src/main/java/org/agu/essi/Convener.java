package org.agu.essi;

/**
 * Container class for AGU convener information
 * @author Eric Rozell
 */
public class Convener
{
	private String _convener;
	private Person _person;
	private Organization _org;
	
	public Convener(String convener)
	{
		_convener = convener;
		String[] arr = convener.split(",");
		_person = new Person(arr[0]);
		if (arr.length > 1)
		{
			_org = new Organization(arr[1]);
		}
	}
	
	public Person getPerson()
	{
		return _person;
	}
	
	public Organization getOrganization()
	{
		return _org;
	}
	
	@Override
	public String toString()
	{
		return _convener;
	}
}
