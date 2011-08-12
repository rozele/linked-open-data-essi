package org.esipfed;

import java.io.*;

public class Annotator 
{
	/**
	 * @param args
	 */
    public static void main( String[] args )
    {
        //verifyArgs(args);
    }

    public static void verifyArgs( String[] args )
    {
    	if (args.length < 2) {
            usageError("at least 2 params are required");
        } else {
            if (!new File(args[0]).exists())
                usageError("file " + args[0] + " doesn't exist");
            File outdir = new File(args[1]);
            if (!outdir.exists() && !outdir.mkdirs())
                usageError("couldn't create output dir");
        }
    }
    
    public static void usageError( String error )
    {
    	System.err.println(error);
    	System.err.println("Usage: java " + (new Object() { }.getClass().getEnclosingClass()).getName() + "input_dir output_dir [annotator_type]");
    	System.exit(-1);
    }
}

