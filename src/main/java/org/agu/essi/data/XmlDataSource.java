package org.agu.essi.data;

import java.io.File;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.agu.essi.Abstract;
import org.agu.essi.Meeting;
import org.agu.essi.Session;
import org.agu.essi.Section;
import org.agu.essi.Keyword;
import org.agu.essi.Author;
import org.agu.essi.match.EntityMatcher;
import org.agu.essi.util.exception.SourceNotReadyException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XmlDataSource implements DataSource {
	private String directory;
	private Vector<Abstract> abstracts;
	private boolean extracted;
	private EntityMatcher matcher;
	
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
	
	public Vector<Abstract> getAbstracts() throws SourceNotReadyException 
	{	
		if (!extracted)
		{
			throw new SourceNotReadyException();
		}
		else
		{
			return abstracts;
		}
	}
	
	public void setEntityMatcher(EntityMatcher m)
	{
		matcher = m;
	}
	
	public EntityMatcher getEntityMatcher()
	{
		return matcher;
	}
	
	public void setDirectory(String dir) throws Exception
	{
		directory = dir;
		parseAbstractsFromDirectory();
		extracted = true;
	}
	
	private static Abstract parseAbstractXml(File xml) throws Exception
	{
		//Variables to build abstract from
		Meeting meeting = null;
		Section section = null;
		Session session = null;
		Vector<Author> authors = new Vector<Author>();
		Vector<Keyword> keywords = new Vector<Keyword>();
		String title = null;
		String hour = null;
		String id = null;
		String abstr = null;
		Abstract abs = null;
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = db.parse(xml);
		
			//get meeting
			NodeList nl = dom.getElementsByTagName("Meeting");
			for (int i = 0; i < nl.getLength(); ++i)
			{
				meeting = new Meeting(nl.item(i).getTextContent());
			}
		
			//get section
			nl = dom.getElementsByTagName("Section");
			for (int i = 0; i < nl.getLength(); ++i)
			{
				section = new Section(nl.item(i).getTextContent(), meeting);
			}
			
			//get session
			nl = dom.getElementsByTagName("Session");
			for (int i = 0; i < nl.getLength(); ++i)
			{
				session = new Session(nl.item(i).getTextContent(), section);
			}
			
			//get keywords
			nl = dom.getElementsByTagName("Keywords");
			for (int i = 0; i < nl.getLength(); ++i)
			{
				NodeList childNodes = nl.item(i).getChildNodes();
				for (int j = 0; j < childNodes.getLength(); ++j)
				{
					keywords.add(new Keyword(childNodes.item(j).getTextContent()));
				}
			}
			
			//get abstract
			nl = dom.getElementsByTagName("Abstract");
			for (int i = 0; i < nl.getLength(); ++i)
			{
				abstr = nl.item(i).getTextContent();
			}
			
			//get title
			nl = dom.getElementsByTagName("Title");
			for (int i = 0; i < nl.getLength(); ++i)
			{
				title = nl.item(i).getTextContent();
			}
			
			//get id
			nl = dom.getElementsByTagName("Id");
			for (int i = 0; i < nl.getLength(); ++i)
			{
				id = nl.item(i).getTextContent();
			}
			
			//get hour
			nl = dom.getElementsByTagName("Hour");
			for (int i = 0; i < nl.getLength(); ++i)
			{
				hour = nl.item(i).getTextContent();
			}
	
			//get authors
			nl = dom.getElementsByTagName("Authors");
			for (int i = 0; i < nl.getLength(); ++i)
			{
				NodeList childNodes = nl.item(i).getChildNodes();
				for (int j = 0; j < childNodes.getLength(); ++j)
				{
					String name = null, email = null;
					Vector<String> affiliations = new Vector<String>();
					NodeList info = childNodes.item(j).getChildNodes();
					for (int k = 0; k < info.getLength(); ++k)
					{
						if (info.item(k).getLocalName().equals("Name"))
						{
							name = info.item(k).getTextContent();
						}
						else if (info.item(k).getLocalName().equals("Email"))
						{
							email = info.item(k).getTextContent();
						}
						else if (info.item(k).getLocalName().equals("Affiliations"))
						{
							NodeList affiliationNodes = info.item(k).getChildNodes();
							for (int l = 0; l < affiliationNodes.getLength(); ++l)
							{
								affiliations.add(affiliationNodes.item(l).getTextContent());
							}
						}
					}
					if (name != null)
					{
						Author a = new Author(name);
						if (email != null) a.addEmail(email);
						for (int k = 0; k < affiliations.size(); ++k)
						{
							a.addAffiliation(affiliations.get(k));
						}
						authors.add(a);
					}
				}
			}
			abs = new Abstract(title, abstr, id, hour, session, authors, keywords);
		}
		catch (Exception e)
		{
			System.err.println("Error parsing XML file at " + xml.getAbsolutePath());
			e.printStackTrace();
		}
		return abs;
	}
	
	private void parseAbstractsFromDirectory() throws Exception
	{
		File f = new File(directory);
		if (f.isDirectory())
		{
			File[] files = f.listFiles();
			for (int i = 0; i < files.length; ++i)
			{
				if (files[i].isFile())
				{
					abstracts.add(XmlDataSource.parseAbstractXml(files[i]));
				}
			}
			extracted = true;
		}
		else
		{
			throw new Exception("File: " + directory + " is not a directory.");
		}
	}
	
	public static void main(String[] args)
	{
    	String dir = "/Users/rozele/Projects/linked-open-data-essi/trunk/resources/data/xml/";
    	//String output = "/Users/rozele/Projects/linked-open-data-essi/trunk/resources/data/compare/";
    	try
    	{
    		XmlDataSource data = new XmlDataSource(dir);
    		Vector<Abstract> abstracts = data.getAbstracts();
    		System.out.println(abstracts.get(0).toString("xml"));
    	}
    	catch (Exception e) {}
	}

	public boolean ready() 
	{
		return extracted;
	}
}
