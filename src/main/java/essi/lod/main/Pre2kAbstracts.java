package essi.lod.main;

import java.util.Vector;

import essi.lod.util.OutputTurtle;
import essi.lod.data.agu.AguPre2000AbstractsParser;
import essi.lod.data.agu.AguPre2000SessionParser;
import essi.lod.data.agu.Pre2000Data;
import essi.lod.data.agu.Pre2000Session;

public class Pre2kAbstracts {
	
	public static void main (String[] args)
	{
		
		AguPre2000AbstractsParser p = new AguPre2000AbstractsParser ();
		String exception = null;
		try {
			exception = p.read(args[0]);
		} catch (Exception e) { System.out.println(e); }
		if ( exception != null ) { System.out.println(exception); }
		
		Vector <Pre2000Data> abstracts = p.getData();
		System.out.println( "There are " + abstracts.size() + " abstracts for " + args[2] );
		
		AguPre2000SessionParser sessionParser = new AguPre2000SessionParser ();
		try {
			exception = sessionParser.read(args[0]);
		} catch (Exception e) { System.out.println(e); }
		if ( exception != null ) { System.out.println(exception); }
		
		Vector <Pre2000Session> sessions = sessionParser.getData();
		System.out.println( "There are " + sessions.size() + " session for " + args[2] );
		
		OutputTurtle turtle = new OutputTurtle ();
		turtle.write(args[1], args[2], sessions, abstracts);
		
	}
	
}