package org.agu.essi.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import org.agu.essi.Abstract;
import org.agu.essi.Meeting;
import org.agu.essi.Session;
import org.agu.essi.Section;
import org.agu.essi.Keyword;
import org.agu.essi.Author;
import org.apache.tools.ant.filters.StringInputStream;
import org.codehaus.plexus.util.xml.XmlStreamReader;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import com.sun.org.apache.xml.internal.resolver.readers.XCatalogReader;

public class XmlDataSource implements DataSource {
	private String directory;
	private Vector<Abstract> abstracts;
	private boolean extracted;
	
	public XmlDataSource()
	{
		abstracts = new Vector<Abstract>();
		extracted = false;
	}
	
	public XmlDataSource(String dir) throws Exception
	{
		directory = dir;
		abstracts = new Vector<Abstract>();
		extracted = false;
		parseAbstractsFromDirectory();
		extracted = true;
	}
	
	public Vector<Abstract> getAbstracts() {
		return null;
	}
	
	public void setDirectory(String dir) throws Exception
	{
		directory = dir;
		parseAbstractsFromDirectory();
		extracted = true;
	}
	
	public static Abstract parseAbstractXml(String xml) throws Exception
	{
		//Variables to build abstract from
		String meeting = null;
		String  session = null;
		String section = null;
		Vector<Author> authors = new Vector<Author>();
		Vector<Keyword> keywords = new Vector<Keyword>();
		String title = null;
		String hour = null;
		String id = null;
		String abstr = null;
		Abstract abs = null;
		
		//This is temporary, we should find a better DOM implementation
		XmlStreamReader reader = new XmlStreamReader(new StringInputStream(xml));
		Xpp3Dom dom = Xpp3DomBuilder.build(reader);
		Xpp3Dom[] nodes = dom.getChildren();
		for (int i = 0; i < nodes.length; ++i)
		{
			if (nodes[i].getName().equals("Meeting"))
			{
				meeting = nodes[i].getValue();
			}
			else if (nodes[i].getName().equals("Section"))
			{
				section = nodes[i].getValue();
			}
			else if (nodes[i].getName().equals("Keywords"))
			{
				Xpp3Dom[] keywordNodes = nodes[i].getChildren();
				for (int j = 0; j < keywordNodes.length; ++j)
				{
					keywords.add(new Keyword(keywordNodes[j].getValue()));
				}
			}
			else if (nodes[i].getName().equals("Abstract"))
			{
				abstr = nodes[i].getValue();
			}
			else if (nodes[i].getName().equals("Title"))
			{
				title = nodes[i].getValue();
			}
			else if (nodes[i].getName().equals("Id"))
			{
				id = nodes[i].getValue();
			}
			else if (nodes[i].getName().equals("Session"))
			{
				session = nodes[i].getValue();
			}
			else if (nodes[i].getName().equals("Hour"))
			{
				hour = nodes[i].getValue();
			}
			else if (nodes[i].getName().equals("Authors"))
			{
				Xpp3Dom[] authorNodes = nodes[i].getChildren();
				for (int j = 0; j < authorNodes.length; ++j)
				{
					Xpp3Dom[] info = authorNodes[j].getChildren();
					String name = null, email = null;
					Vector<String> affiliations = new Vector<String>();
					for (int k = 0; k < info.length; ++k)
					{
						if (info[k].getName().equals("Name"))
						{
							name = info[k].getValue();
						}
						else if (info[k].getName().equals("Email"))
						{
							email = info[k].getValue();
						}
						else if (info[k].getName().equals("Affiliations"))
						{
							Xpp3Dom[] affiliationNodes = info[k].getChildren();
							for (int l = 0; l < affiliationNodes.length; ++l)
							{
								affiliations.add(affiliationNodes[l].getValue());
							}
						}
					}
					Author a = new Author(name, email);
					for (int k = 0; k < affiliations.size(); ++k)
					{
						a.addAffiliation(affiliations.get(k));
					}
					authors.add(a);
				}
			}
			Meeting m = new Meeting(meeting);
			Section sec = new Section(section,m);
			Session s = new Session(session,sec);
			abs = new Abstract(title, abstr, id, hour, s, authors, keywords);
		}
		return abs;
	}
	
	private void parseAbstractsFromDirectory() throws Exception
	{
		File f = new File(directory);
		if (f.isDirectory())
		{
			File[] files = f.listFiles();
			String content = "";
			for (int i = 0; i < files.length; ++i)
			{
				BufferedReader br = new BufferedReader(new FileReader(files[i]));
				String line = br.readLine();
				while (line != null)
				{
					content += line + "\n";
					line = br.readLine();
				}
				abstracts.add(XmlDataSource.parseAbstractXml(content));
			}
		}
		else
		{
			throw new Exception("File: " + directory + " is not a directory.");
		}
	}
	
	public static void main(String[] args)
	{
    	String dir = "/Users/rozele/Projects/linked-open-data-essi/trunk/resources/data/xml/";
    	String output = "/Users/rozele/Projects/linked-open-data-essi/trunk/resources/data/compare/";
    	try
    	{
    		XmlDataSource data = new XmlDataSource(dir);
    		Vector<Abstract> abstracts = data.getAbstracts();
    		System.out.println(abstracts.get(0).toString("xml"));
    	}
    	catch (Exception e) {}
	}

	public boolean hasUniqueIdentifiers() {
		return false;
	}
}
