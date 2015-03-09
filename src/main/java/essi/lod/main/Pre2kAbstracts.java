package essi.lod.main;

import java.util.Vector;

import essi.lod.util.OutputTurtle;
import essi.lod.util.OutputTurtlePeople;
import essi.lod.data.agu.AguPre2000AbstractsParser;
import essi.lod.data.agu.AguPre2000SessionParser;
import essi.lod.data.agu.Pre2000Data;
import essi.lod.data.agu.Pre2000Session;

public class Pre2kAbstracts {
	
	public static void main (String[] args)
	{
		String inputFile = args[0];
		String outputFile = args[1];
		String year = args[2];
		boolean sessionsFromAbstracts = Boolean.valueOf(args[3]);
		Pre2kAbstracts p = new Pre2kAbstracts ();
		p.createTurtle( inputFile, outputFile, year, sessionsFromAbstracts );		
	}
	
	public void createTurtle (String inputFile, String outputFile, String year, boolean createSessionsFromAbstracts ) {
		
		AguPre2000AbstractsParser p = new AguPre2000AbstractsParser ();
		String exception = null;
		try {
			exception = p.read( inputFile );
		} catch (Exception e) { System.out.println(e); }
		if ( exception != null ) { System.out.println(exception); }
		
		Vector <Pre2000Data> abstracts = p.getData();
		System.out.println( "There are " + abstracts.size() + " abstracts for " + year );
		
		AguPre2000SessionParser sessionParser = new AguPre2000SessionParser ();
		try {
			exception = sessionParser.read(inputFile);
		} catch (Exception e) { System.out.println(e); }
		if ( exception != null ) { System.out.println(exception); }
		
		Vector <Pre2000Session> sessions = sessionParser.getData();
		System.out.println( "There are " + sessions.size() + " session for " + year );
		
		OutputTurtle turtle = new OutputTurtle ();
		turtle.write(outputFile, year, sessions, abstracts, createSessionsFromAbstracts);
		
		// As 3-9-2015 we write the people to the same file
		OutputTurtlePeople turtlePeople = new OutputTurtlePeople ();
		turtlePeople.write(outputFile, year, abstracts, sessions);
		
	}
	
}