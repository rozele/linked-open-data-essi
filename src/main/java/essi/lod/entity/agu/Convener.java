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
package essi.lod.entity.agu;

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
