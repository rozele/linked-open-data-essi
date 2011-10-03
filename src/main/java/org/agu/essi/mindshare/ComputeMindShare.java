package org.agu.essi.mindshare;

import org.agu.essi.data.XmlDataSource;
import org.agu.essi.abstracts.Abstract;
import org.agu.essi.Author;
import org.agu.essi.Keyword;
import java.util.Vector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ComputeMindShare {
	
	// method 1 - for agu only 
	
	// method 2 - for DBpedia Spotlight
	
	public static void main ( String[] args ) {
	
		// read the AGU abstracts
		try {
			
		   XmlDataSource xml = new XmlDataSource (args[0]);
		   Vector <Abstract> abstracts = xml.getAbstracts();
		   HashMap <String,Integer> keywordMap = new HashMap <String,Integer> ();
		   HashMap <String,Vector<String>> authorMap = new HashMap <String,Vector<String>> ();
		   for ( int i=0; i<abstracts.size(); i++ ) {
			  
			  // create a hash map of keywords
			  Vector <Keyword> keywords = abstracts.get(i).getKeywords();
			  for ( int j=0; j<keywords.size(); j++ ) { 
				
				 Keyword k = keywords.get(j);
				 
				 // the hash map already contains this keyword
				 if ( keywordMap.containsKey( k.getName() ) ) {
				   Integer count = keywordMap.get( k.getName() );
				   count++;
				   keywordMap.put( k.getName(), count );
				 } 
				 
				 // hash map does not contiain this keyword - let's look at only the informatic keywords for now
				 if ( !keywordMap.containsKey( k.getName() ) && k.getName().contains("INFORMATICS") ) { 
				   keywordMap.put( k.getName(), 1 ); 
				 }
				 
				 // create a hash map for the authors
				 Vector <Author> authors = abstracts.get(i).getAuthors();
				 for ( int l=0; l<authors.size(); l++ ) {
					
					Author a = authors.get(l);
					  
					// the hash map already contains this author
					if ( authorMap.containsKey( a.getPerson().getName() ) && k.getName().contains("INFORMATICS") ) {
					  Vector <String> keywordCount = authorMap.get( a.getPerson().getName() );
					  boolean found = false;
					  for ( int m=0; m<keywordCount.size(); m++ ) {
						 String[] parts = keywordCount.get(m).split(":");
						 if ( parts[0].equals( k.getName() ) ) { 
						   Integer count = 1;
						   try { 
							  count = Integer.valueOf( parts[1] );
							  count++;
						   } catch (Exception e) { }  
						   keywordCount.set( m, k.getName() + ":" + count );
						   found = true;
						 } 
					  } // end for loop over all keywords attributed to this author
					  if ( !found ) { keywordCount.add( k.getName() + ":1" ); }
					  authorMap.put( a.getPerson().getName(), keywordCount );
					}
					
					// hash map does not contain this author - and keyword is from informatics
					if ( !authorMap.containsKey( a.getPerson().getName() ) && k.getName().contains("INFORMATICS") ) { 
					  Vector <String> kc = new Vector <String> ();
					  kc.add( k.getName() + ":1" );
					  authorMap.put( a.getPerson().getName(), kc );
				    }
					
				  } // end loop over all authors in this abstract
				  
			  } // end loop over all keywords in this abstract
			  
		   } // end loop over all abstracts
		   
		   Iterator <Map.Entry<String, Integer>> it = keywordMap.entrySet().iterator();
		   while ( it.hasNext() ) {
		     Map.Entry <String,Integer> pairs = (Map.Entry <String,Integer>) it.next();
		     System.out.println(pairs.getKey() + " = " + pairs.getValue());
		   }
		   
		   Iterator <Map.Entry<String, Vector <String>>> it2 = authorMap.entrySet().iterator();
		   while ( it2.hasNext() ) {
		     Map.Entry <String,Vector<String>> pairs = (Map.Entry <String,Vector<String>>) it2.next();
		     System.out.println(pairs.getKey() + ": " );
		     Vector <String> kCounts = pairs.getValue();
		     for ( int z=0; z<kCounts.size(); z++ ) { System.out.println( "  " + kCounts.get(z) ); }
		   }
		   
		} catch ( Exception e ) { System.out.println(e); }
		
	}
	
}