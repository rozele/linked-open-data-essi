package org.agu.essi.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Vector;

import org.agu.essi.Keyword;

public class KeywordParser {
	
	public static String generate(String loc, String format)
	{
		Vector<Keyword> terms = parseTerms(loc);
		return writeTerms(terms, format);
	}
	
	public static Vector<Keyword> parseTerms(String loc)
	{
		File f = new File(loc);
		StringWriter sw = new StringWriter();
		Vector<Keyword> terms = new Vector<Keyword>();
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader(f));
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
		} 
		catch (Exception e) 
		{
			System.err.println("Error parsing index terms.");
			e.printStackTrace();
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
}
