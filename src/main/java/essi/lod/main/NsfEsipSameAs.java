package essi.lod.main;

import essi.lod.util.GetListOfFiles;
import essi.lod.util.Utils;

import essi.lod.util.FileWrite;
import java.util.Vector;
import java.io.File;
import essi.lod.entity.esip.Person;
import essi.lod.data.esip.rdf.EsipPeopleParser;
import essi.lod.rdf.SameAs;

public class NsfEsipSameAs
{
	
	public static void main (String[] args)
	{
		
		Vector <String> nsfFiles = new Vector <String> ();
		Vector <String> sameAsIds = new Vector <String> ();
		GetListOfFiles getFileList = new GetListOfFiles ();
		
		// parsers
		EsipPeopleParser esipParser = new EsipPeopleParser ();
		EsipPeopleParser nsfParser = new EsipPeopleParser ();
				
		// read data
		Vector <Person> esipPeople = null;
		String esipFile = "/Users/tnarock/Git/Work/ESIP/LOD/esip/ESIP_People.rdf";
		try {
		  esipPeople = esipParser.parse( esipFile );
		} catch (Exception e) { System.out.println(e); }
		
		File dir1 = new File ( "/Users/tnarock/Git/Work/ESIP/LOD/NSF-RDF/People/" );
		nsfFiles = getFileList.Process( dir1 );
	
		// loop over all people
		String[] parts;
		Vector <String> esipEmail = null;
		Vector <String> nsfEmail = null;	
		Vector <Person> nsfPeople = null;
		for (int k=0; k<nsfFiles.size(); k++)
		{
		
			System.out.println("Comparing person " + (k+1) + " of " + nsfFiles.size() );
			try {
				  nsfPeople = nsfParser.parse( nsfFiles.get(k) );
			} catch (Exception e) { System.out.println(e); }
				
			for ( int l=0; l<nsfPeople.size(); l++ )
			{
				
				for (int j=0; j<esipPeople.size(); j++)
				{
					Person esipPerson = esipPeople.get(j);
					esipEmail = esipPerson.getEmail();
						    
					Person nsfPerson = nsfPeople.get(l);
				    nsfEmail = nsfPerson.getEmail();
				    		
				    // compare email addresses and last name
				    if ( esipPerson.getLastName().trim().equals( nsfPerson.getLastName().trim() ) )  
				    { 
				    	for ( int m=0; m<esipEmail.size(); m++ )
				    	{
				    		if ( nsfEmail.contains( esipEmail.get(m) ) )
				    		{
				    			sameAsIds.add(esipPerson.getID() + ";" + nsfPerson.getID()); 
						    	break;
				    		}
				    	}
				    }
				} // end loop over esipPeople
		
			} // end loop over nsf people
			
		} // end loop over nsf files
		
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