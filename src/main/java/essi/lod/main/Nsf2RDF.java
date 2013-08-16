package essi.lod.main;

import essi.lod.util.GetListOfFiles;
import java.util.Vector;
import java.io.File;
import essi.lod.data.nsf.NsfData;
import essi.lod.entity.nsf.*;
import essi.lod.rdf.NSF;
import essi.lod.util.FileWrite;
import essi.lod.rdf.FOAF;
import essi.lod.util.Utils;

public class Nsf2RDF { 
	
	private String createFullRDF ( String input ) 
	{
		String rdf = Utils.writeRdfHeader();
		rdf = rdf + input;
		rdf = rdf + Utils.writeRdfFooter();
		return rdf;
	}
	
	public static void main (String[] args) {
	
		// inputs
		String xmlDir = args[0];
		String outputDir = args[1];
				
		Nsf2RDF nsf = new Nsf2RDF ();
		nsf.createRDF( xmlDir, outputDir );
		
	}
	
	public void createRDF( String xmlDir, String outputDir ) {
		
		// file writer
		FileWrite fw = new FileWrite ();
		
		// RDF ID indicies
		int sourceIndex = 0;
		int programIndex = 0;
		int projectIndex = 0;
		int personIndex = 0;
		
		// Vectors to hold the NSF Data
		Vector <NsfFundingSource> fundingSources = new Vector <NsfFundingSource> ();
		Vector <NsfProgram> programs = new Vector <NsfProgram> ();
		Vector <NsfProject> projects = new Vector <NsfProject> ();
		Vector <Person> people = new Vector <Person> ();
		
		// Get a listing of all the xml files
		GetListOfFiles fileList = new GetListOfFiles();
		Vector <String> xmlFiles = fileList.Process( new File (xmlDir) );
		
		// Loop over all the xml files
		for (int i=0; i<xmlFiles.size(); i++) {
			
			// output status
			int count = i+1;
			System.out.println("Parsing " + xmlFiles.get(i) + " (" + count + " of " + xmlFiles.size() + ")");
			
			// parse the file
			NsfData nsfParser = new NsfData ();
			try {
				nsfParser.parse( xmlFiles.get(i), sourceIndex, programIndex, projectIndex);
			} catch (Exception e) { System.out.println(e); }
			
			try {
				NsfFundingSource fs = nsfParser.getFundingSource();
				NsfProgram program = nsfParser.getProgram();
				NsfProject project = nsfParser.getProject();

				// create a person object from the project data
				Person person = new Person ( project.getPIfirstName(), project.getPIlastName(), project.getAffiliation(), 
						project.getEmail(), "" );
				
				// if the person does exist then the exists method sets the PIid
				if ( !person.exists( people, project.getAffiliation(), project.getEmail(), project) ) {
				   person.createID(personIndex);
				   personIndex++;
				   people.add(person);
				   project.setPIid(person.getID());
				}

				// increment the indexes
				projects.add( project );
				projectIndex++;

				// if the program code doesn't exist in the data (it is not consistently reported)
				// then don't bother checking for it's existence in our list of programs
				if ( program.getProgramCode() != null ) {
					if ( !program.exists( programs ) ) {
						programIndex++;
						programs.add( program );
						project.setProgramID( program.getID() );
					} else { project.setProgramID(null); }
				}
				
				// if the funding source doesn't exist in the data (it is not consistently reported)
				// then don't bother checking for it's existence in our list of funding sources
				if ( fs.getDivisionName() != null ) {
					// if the funding source does exist then a link to the project will be added
					if ( !fs.exists( fundingSources, project.getID() ) ) {
						sourceIndex++;
						fs.addProject( project.getID() );
						fundingSources.add( fs );
					} 
				}

			} catch (Exception e) { System.out.println(e); }
			
		} // end loop over all XML files
		
		// Output RDF/XML
		xmlFiles = null;
		System.out.println("There are " + people.size() + " people.");
		System.out.println("There are " + programs.size() + " NSF Programs.");
		System.out.println("There are " + projects.size() + " NSF Projects.");
		System.out.println("There are " + fundingSources.size() + " NSF Funding Sources.");
		NSF nsf = new NSF ();
		for ( int i=0; i<fundingSources.size(); i++ ) {
			NsfFundingSource fs = fundingSources.get(i);
			String[] parts = fs.getID().split("/");
			String fname = parts[parts.length-1] + ".rdf";
			String rdf = createFullRDF( nsf.writeFundingBody(fs.getID(), fs.getProjects(), fs.getDivisionName()) );
			fw.newFile(outputDir + "FundingSources/" + fname, rdf);
		}
		for ( int i=0; i<projects.size(); i++ ) {
			NsfProject project = projects.get(i);
			String[] parts = project.getID().split("/");
			String fname = parts[parts.length-1] + ".rdf";
			String rdf = createFullRDF( nsf.writeProject(project.getID(), 
					project.getPIid(), project.getAwardID(), project.getStartDate(), 
					project.getEndDate(), project.getAbstract(), project.getProgramID()) );
			fw.newFile(outputDir + "Projects/" + fname, rdf);
		}
		for ( int i=0; i<programs.size(); i++ ) {
			NsfProgram program = programs.get(i);
			String[] parts = program.getID().split("/");
			String fname = parts[parts.length-1] + ".rdf";
			String rdf = createFullRDF( nsf.writeProgram(program.getID(), program.getProgramName()) );
			fw.newFile(outputDir + "Programs/" + fname, rdf);
		}
		for ( int i=0; i<people.size(); i++ ) {
			FOAF foaf = new FOAF ();
			Person p = people.get(i);
			String[] parts = p.getID().split("/");
			String fname = parts[parts.length-1] + ".rdf";
			String rdf = createFullRDF( foaf.writePerson(p) );
			fw.newFile(outputDir + "People/" + fname, rdf);
		}
		
		
	}
	
}