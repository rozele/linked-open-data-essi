/**
 * Copyright (C) 2011 Tom Narock and Eric Rozell
 *
 *     This software is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */
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

