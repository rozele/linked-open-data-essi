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