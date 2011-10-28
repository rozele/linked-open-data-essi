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
package org.esipfed.util;

import java.security.MessageDigest;

import org.apache.log4j.Logger;

/**
 * Class for the construction of SHA1 hash values
 * SHA1 hash values of email addresses are used in FOAF profiles
 * @author Tom Narock
 */
public class HashFunction {

	static final Logger log = Logger.getLogger(HashFunction.class);  
	
	/**
	 * Method to compute the SHA1 hash function of a string
	 * @param str the string to compute the SHA1 hash of
	 * @return string representation of SHA1 value
	 */
	public static String sha1 ( String str ) {
		
	  byte[] bytes = str.getBytes();
	  MessageDigest md = null;
	  try { 
	    md = MessageDigest.getInstance("SHA1"); 
	    return byteArrayToHexString( md.digest(bytes) );
	  } catch (Exception e) { 
		log.warn("Unable to generate SHA1 for: " + str);
		log.warn("Exception: " + e);
		log.warn(" ");
		return null;	  
	  } 

	}

	/**
	 * Method to convert byte representation of SHA1 to string
	 * @param b byte array representation of SHA1 hash value
	 * @return string representation of SHA1 value
	 */
	private static String byteArrayToHexString( byte[] b ) throws Exception {
		  
	  String result = "";
	  for (int i=0; i < b.length; i++) {
	    result +=
		Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
	  }
      return result;
      
	}

}