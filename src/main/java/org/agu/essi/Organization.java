package org.agu.essi;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.model.*;

import org.geonames.Toponym;
import org.geonames.WebService;

/**
 * Organization parser
 * @author Eric Rozell
 *
 */
public class Organization 
{
	private String _org;
	private GeocoderResult _geocode;
	private GeocoderGeometry _geometry;
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
		if (_geometry != null)
		{
			try 
			{
				List<Toponym> l = WebService.findNearbyPlaceName(_geometry.getLocation().getLat().doubleValue(), _geometry.getLocation().getLng().doubleValue());
				Iterator<Toponym> iterator = l.iterator();
				if (iterator.hasNext())
				{
					return "http://sws.geonames.org/" + iterator.next().getGeoNameId() + "/";
				}
				else
				{
					return null;
				}
			} 
			catch (Exception e) 
			{
				System.err.println("Can't find address at: " + _geometry.getLocation().getLat().doubleValue() + ", " + _geometry.getLocation().getLng().doubleValue());
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
