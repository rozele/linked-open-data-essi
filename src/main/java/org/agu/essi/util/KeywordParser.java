package org.agu.essi.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
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
