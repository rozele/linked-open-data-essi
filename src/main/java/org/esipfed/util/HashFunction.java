package org.esipfed.util;

import java.security.MessageDigest;

import org.apache.log4j.Logger;

public class HashFunction {

	static final Logger log = Logger.getLogger(HashFunction.class);  
	
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

	public static String byteArrayToHexString( byte[] b ) throws Exception {
		  
	  String result = "";
	  for (int i=0; i < b.length; i++) {
	    result +=
		Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
	  }
      return result;
      
	}

}