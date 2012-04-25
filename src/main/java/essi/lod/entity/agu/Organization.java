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

import java.util.Iterator;
import java.util.List;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.model.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geonames.Toponym;
import org.geonames.WebService;

/**
 * Organization parser
 * @author Eric Rozell
 *
 */
public class Organization 
{
	private static Log log = LogFactory.getLog(Organization.class);
	
	private String _org;
	private GeocoderResult _geocode;
	private GeocoderGeometry _geometry;
	private String _geonames;
	private boolean googled;
	
	public Organization(String orgString)
	{
		_org = orgString;
		googled = false;
	}
	
	public LatLng getCoordinates()
	{
		if (!googled)
		{
			getGeocode();
			googled = true;
		}
		if (_geometry != null)
		{
			return _geometry.getLocation();
		}
		else
		{
			return null;
		}
		
	}
	
	public String getGeoNamesId()
	{
		if (!googled)
		{
			getGeocode();
			googled = true;
		}
		if (_geonames != null)
		{
			return _geonames;
		}
		else if (_geometry != null)
		{
			try 
			{
				List<Toponym> l = WebService.findNearbyPlaceName(_geometry.getLocation().getLat().doubleValue(), _geometry.getLocation().getLng().doubleValue());
				Iterator<Toponym> iterator = l.iterator();
				if (iterator.hasNext())
				{
					_geonames = "http://sws.geonames.org/" + iterator.next().getGeoNameId() + "/";
					return _geonames;
				}
				else
				{
					return null;
				}
			} 
			catch (Exception e) 
			{
				log.error("Can't find address at: " + _geometry.getLocation().getLat().doubleValue() + ", " + _geometry.getLocation().getLng().doubleValue());
				e.printStackTrace();
				return null;
			}
		}
		else
		{
			return null;
		}
	}
	
	public String getAddress()
	{
		if (!googled)
		{
			getGeocode();
			googled = true;
		}
		if (_geocode != null)
		{
			return _geocode.getFormattedAddress();
		}
		else
		{
			return null;
		}
	}
	
	private void getGeocode()
	{
		Geocoder geo = new Geocoder();
		GeocoderRequest request = new GeocoderRequest();
		request.setAddress(_org);
		request.setLanguage("en");
		GeocodeResponse response = geo.geocode(request);
		GeocoderStatus status = response.getStatus();
		if (status.equals(GeocoderStatus.OK))
		{
			List<GeocoderResult> results = response.getResults();
			Iterator<GeocoderResult> iterator = results.iterator();
			if (iterator.hasNext())
			{
				_geocode = iterator.next();
				_geometry = _geocode.getGeometry();
			}	
		}
		else
		{
			//TODO: error handling for Geocoder
		}
	}
	
	/**
	 * Override of the toString method
	 */
	@Override
	public String toString()
	{
		return _org;
	}
	
	/**
	 * Override of the equals method
	 */
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Organization && ((Organization)o).toString().equals(this.toString()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
