package essi.lod.main;

import essi.lod.util.GetListOfFiles;
import essi.lod.util.Utils;

import essi.lod.util.FileWrite;
import java.util.Vector;
import java.io.File;
import essi.lod.entity.esip.Person;
import essi.lod.data.agu.rdf.AguPeopleParser;
import essi.lod.data.esip.rdf.EsipPeopleParser;
import essi.lod.rdf.SameAs;

public class EsipAguSameAs
{
	
	public static void main (String[] args)
	{
		
		Vector <String> aguFiles = new Vector <String> ();
		Vector <String> sameAsIds = new Vector <String> ();
		GetListOfFiles getFileList = new GetListOfFiles ();
		
		// parsers
		AguPeopleParser aguParser = new AguPeopleParser ();
		EsipPeopleParser esipParser = new EsipPeopleParser ();
				
		// read data
		File dir1 = new File ( "/Users/tnarock/Git/Work/ESIP/LOD/AGU-RDF/" );
		Vector <String> tmpFiles = getFileList.Process( dir1 );
		for ( int i=0; i<tmpFiles.size(); i++ ) 
		{
		   if ( tmpFiles.get(i).contains("people.rdf") ) { aguFiles.addElement( tmpFiles.get(i) ); }
		}
		Vector <Person> esipPeople = null;
		String esipFile = "/Users/tnarock/Git/Work/ESIP/LOD/esip/ESIP_People.rdf";
		try {
		  esipPeople = esipParser.parse( esipFile );
		} catch (Exception e) { System.out.println(e); }
		
		// loop over all agu people
		String[] parts;
		Vector <String> aguEmail = null;
		Vector <String> esipEmail = null;
		for ( int i=0; i<aguFiles.size(); i++ )
		{
	
			// read the next file
			try {
				
				Vector <Person> aguPeople = aguParser.parse( aguFiles.get(i) );
				
				// loop over all the people
				for (int j=0; j<aguPeople.size(); j++)
				{
					Person aguPerson = aguPeople.get(j);
					aguEmail = aguPerson.getEmail();
				    
				    // loop over all the esip people
				    for (int k=0; k<esipPeople.size(); k++)
				    {
				    		
				    	Person esipPerson = esipPeople.get(k);
				    	esipEmail = esipPerson.getEmail();
				    		
				    	// compare email addresses and last name
				    	if ( aguPerson.getLastName().trim().equals( esipPerson.getLastName().trim() ) )  
				    	{ 
				    		for ( int m=0; m<aguEmail.size(); m++ )
				    		{
				    			if ( esipEmail.contains( aguEmail.get(m) ) )
				    			{
				    				sameAsIds.add(aguPerson.getID() + ";" + esipPerson.getID()); 
						    		break;
				    			}
				    		}
				    	}
				    }
				}
			} catch (Exception e) { System.out.println(e); }
		
		} // end loop over agu files
		
		// loop over the sameAs references
		FileWrite fw = new FileWrite ();
		SameAs sameAs = new SameAs ();
		StringBuilder same = new StringBuilder();  
		same.append( Utils.writeXmlHeader() );
	    same.append( Utils.writeDocumentEntities() );
		same.append( Utils.writeRdfHeader() );
		for ( int i=0; i<sameAsIds.size(); i++ )
		{
			parts = sameAsIds.get(i).split(";");
			same.append( sameAs.writeSameAs( parts[0], parts[1] ) );
		}
		same.append( Utils.writeRdfFooter() );	  
		fw.newFile( args[0], same.toString() );
		
	}
	
}