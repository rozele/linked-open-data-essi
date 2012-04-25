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
package essi.lod.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

import essi.lod.entity.agu.Keyword;

public class KeywordParser {
	
	public static String generate(String loc, String format)
	{
		Vector<Keyword> terms = parseTerms(loc);
		return writeTerms(terms, format);
	}
	
	public static Vector<Keyword> parseTerms(String loc)
	{
		File f = new File(loc);
		Vector<Keyword> terms = null;
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader(f));
			terms = parseFromReader(br);
		} 
		catch (IOException ex1) 
		{
			try 
			{
				URL url = new URL(loc);
				URLConnection conn = url.openConnection();
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				terms = parseFromReader(br);
			} 
			catch (IOException ex2) {
				System.err.println("Error parsing index terms.");
				ex2.printStackTrace();
			}
		}
		return terms;
	}
	
	public static String writeTerms(Vector<Keyword> terms, String format)
	{
		StringWriter sw = new StringWriter();
		sw.write(Utils.writeDocumentEntities());
		sw.write(Utils.writeRdfHeader());
		for (int i = 0; i < terms.size(); ++i)
		{
			sw.write(terms.get(i).toString(format));
		}
		sw.write(Utils.writeRdfFooter());
		return sw.toString();
	}
	
	private static Vector<Keyword> parseFromReader(BufferedReader br) throws IOException
	{
		Vector<Keyword> terms = new Vector<Keyword>();
		String line;
		Keyword parent = null;
		br.readLine(); br.readLine();//skip first two lines
		line = br.readLine();
		while (line != null)
		{
			if (line.startsWith(" "))
			{
				Keyword k = new Keyword(line.trim());
				k.setParent(parent);
				terms.add(k);
			}
			else 
			{
				parent = new Keyword(line);
				terms.add(parent);
			}
			line = br.readLine();
		}
		return terms;
	}
}
