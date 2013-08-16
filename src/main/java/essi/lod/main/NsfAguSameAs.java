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

public class NsfAguSameAs
{
	
	public static void main (String[] args)
	{
		
		Vector <String> aguFiles = new Vector <String> ();
		Vector <String> nsfFiles = new Vector <String> ();
		Vector <String> sameAsIds = new Vector <String> ();
		GetListOfFiles getFileList = new GetListOfFiles ();
		
		// read data
		File dir1 = new File ( "/Users/tnarock/Git/Work/ESIP/LOD/AGU-RDF/" );
		Vector <String> tmpFiles = getFileList.Process( dir1 );
		for ( int i=0; i<tmpFiles.size(); i++ ) 
		{
		   if ( tmpFiles.get(i).contains("people.rdf") ) { aguFiles.addElement( tmpFiles.get(i) ); }
		}
		File dir2 = new File ( "/Users/tnarock/Git/Work/ESIP/LOD/NSF-RDF/People/" );
		getFileList.clear();
		nsfFiles = getFileList.Process( dir2 );
	
		// parsers
		AguPeopleParser aguParser = new AguPeopleParser ();
		EsipPeopleParser nsfParser = new EsipPeopleParser ();
		
		// loop over all agu people
		String[] parts;
		Vector <String> aguEmail = null;
		Vector <String> nsfEmail = null;
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
					//if ( aguPerson.getFirstName().length() > 1 ) // may be two intials, i.e. T W Narock
					//{
					//	parts = aguPerson.getFirstName().trim().split(" "); 
					//    aguName = parts[0] + aguPerson.getLastName();
					//} else {
					//  aguName = aguPerson.getFirstName() + aguPerson.getLastName();
					//}
				    
				    // loop over all the nsf people
				    for (int k=0; k<nsfFiles.size(); k++)
				    {
				    	Vector <Person> nsfPeople = nsfParser.parse( nsfFiles.get(k));
				    	for ( int l=0; l<nsfPeople.size(); l++ )
				    	{
				    		Person nsfPerson = nsfPeople.get(l);
				    		nsfEmail = nsfPerson.getEmail();
				    		//if ( nsfPerson.getFirstName().length() > 1 )
				    		//{
				    		//  firstInitial = nsfPerson.getFirstName().substring(0, 1);
				    		//  nsfName = firstInitial + nsfPerson.getLastName();
				    		//} else { nsfName = ""; }
				    		
				    		// compare email addresses and last name
				    		if ( aguPerson.getLastName().trim().equals( nsfPerson.getLastName().trim() ) )  
				    		{ 
				    			for ( int m=0; m<aguEmail.size(); m++ )
				    			{
				    				if ( nsfEmail.contains( aguEmail.get(m) ) )
				    				{
						    		  sameAsIds.add(aguPerson.getID() + ";" + nsfPerson.getID()); 
						    		  break;
				    				}
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