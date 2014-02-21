package essi.lod.main;

import java.util.Vector;

import essi.lod.util.OutputTurtlePeople;
import essi.lod.data.agu.AguPre2000AbstractsParser;
import essi.lod.data.agu.Pre2000Data;

public class Pre2kPeople {
	
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
		
		OutputTurtlePeople turtle = new OutputTurtlePeople ();
		turtle.write(args[1], args[2], abstracts);
		
	}
	
}