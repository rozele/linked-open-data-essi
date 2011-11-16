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
package org.agu.essi.data;

import java.io.File;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.agu.essi.abstracts.Abstract;
import org.agu.essi.abstracts.DefaultAbstract;
import org.agu.essi.Meeting;
import org.agu.essi.Session;
import org.agu.essi.Section;
import org.agu.essi.Keyword;
import org.agu.essi.Author;
import org.agu.essi.match.EntityMatcher;
import org.agu.essi.util.FileWrite;
import org.agu.essi.util.exception.SourceNotReadyException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.agu.essi.data.source.AbstractDataSource;

/**
 * Parser for AGU Abstracts encoded in XML
 * @author Eric Rozell and Tom Narock
 *
 */
public class XmlAbstractData implements AbstractDataSource {
	private String directory;
	private Vector<Abstract> abstracts;
	private boolean extracted;
	private EntityMatcher matcher;
	
	static final Logger log = Logger.getLogger(org.agu.essi.data.XmlAbstractData.class);  
	
	public XmlAbstractData()
	{
		abstracts = new Vector<Abstract>();
		extracted = false;
	}
	
	public XmlAbstractData(String dir) throws Exception
	{
		directory = dir;
		abstracts = new Vector<Abstract>();
		extracted = false;
		parseAbstractsFromDirectory();
		extracted = true;
	}
	
	/**
	 * Method to get the parsed abstracts
	 * @return Vector <Abstract> abstracts
	 */
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
	
	/**
	 * Method to set the Entity Matcher
	 * @param Entity Matcher matcher
	 */
	public void setEntityMatcher(EntityMatcher m)
	{
		matcher = m;
	}
	
	/**
	 * Method to get the Entity Matcher
	 * @return Entity Matcher matcher
	 */
	public EntityMatcher getEntityMatcher()
	{
		return matcher;
	}
	
	/**
	 * Method to set the directory to parse
	 * @param String directory
	 */
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
			nl = dom.getElementsByTagName("Keyword");
			for (int i = 0; i < nl.getLength(); ++i)
			{
				keywords.add(new Keyword(nl.item(i).getTextContent()));
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
			nl = dom.getElementsByTagName("Author");
			for (int i = 0; i < nl.getLength(); ++i)
			{
				String name = null, email = null;
				Vector<String> affiliations = new Vector<String>();
				NodeList info = nl.item(i).getChildNodes();
				for (int j = 0; j < info.getLength(); ++j)
				{
					if (info.item(j).getNodeType() == Node.ELEMENT_NODE)
					{
						if (info.item(j).getNodeName().equals("Name"))
						{
							name = info.item(j).getTextContent();
						}
						else if (info.item(j).getNodeName().equals("Email"))
						{
							email = info.item(j).getTextContent();
						}
						else if (info.item(j).getNodeName().equals("Affiliations"))
						{
							NodeList affiliationNodes = info.item(j).getChildNodes();
							for (int k = 0; k < affiliationNodes.getLength(); ++k)
							{
								if (affiliationNodes.item(k).getNodeType() == Node.ELEMENT_NODE &&
										affiliationNodes.item(k).getNodeName().equals("Affiliation"))
								{
									affiliations.add(affiliationNodes.item(k).getTextContent());
								}
							}
						}
					}
				}
				if (name != null)
				{
					Author a = new Author(name);
					if (email != null) a.getPerson().addEmail(email);
					for (int k = 0; k < affiliations.size(); ++k)
					{
						a.addAffiliation(affiliations.get(k));
					}
					authors.add(a);
				}
			}
			abs = new DefaultAbstract(title, abstr, id, hour, session, authors, keywords);
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
					abstracts.add(XmlAbstractData.parseAbstractXml(files[i]));
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
    	// Object to deal with command line options (Apache CLI)
	  	Options options = new Options();
	  	options.addOption("outputDirectory", true, "Directory in which to store the retrieved abstracts.");
	  	options.addOption("outputFormat",true,"Serialization format for the resulting data");
	  	options.addOption("inputDirectory",true,"Directory from which to retrieve existing abstract XML.");
	  	  
	  	// Parse the command line arguments
	  	CommandLine cmd = null;
	  	CommandLineParser parser = new PosixParser();
	  	try 
	  	{
	  		cmd = parser.parse( options, args);
	  	} 
	  	catch ( Exception pe ) { 
	  		log.error("Error parsing command line options: " + pe.toString()); 
	  		pe.printStackTrace();
	  	}
	  	  
	  	// Check if the correct options were set
	  	boolean error = false;
	  	//String format = null;
	  	String output = null;
	  	String input = null;
	  	
	  	// output directory
	    if ( !cmd.hasOption("outputDirectory") ) 
	    {
	    	error = true;
	  		log.error("--outputDirectory Not Set. Directory in which to store the retrieved abstracts.");
	  	}
	    else
	    {
	    	output = cmd.getOptionValue("outputDirectory");
	    }
	    
	    //input directory
	    if ( !cmd.hasOption("inputDirectory") )
	    {
	    	error = true;
	    	log.error("--inputDirectory Not Set. Directory from which to retrieve existing abstract XML.");
	    }
	    else
	    {
	    	input = cmd.getOptionValue("inputDirectory");
	    }
	    
	    // output format
	  	if ( cmd.hasOption("outputFormat")) 
	  	{ 
	  		//format = cmd.getOptionValue("outputFormat"); 
	  	} 
	    
	    if (!error)
	    {	
	    	try
	    	{
	    		XmlAbstractData data = new XmlAbstractData(input);
	    		Vector<Abstract> abstracts = data.getAbstracts();
	    		FileWrite fw = new FileWrite();
	    		for (int i = 0; i < abstracts.size(); ++i)
	    		{
	    			Abstract abstr = abstracts.get(i);
	    			String title = abstr.getId();
	    			String meeting = abstr.getMeeting().getName();
	    			title = title.replaceAll("\\s+", "_");
	    			title = title.replaceAll("\\s", "_");
	    			meeting = meeting.replaceAll("\\s", "_");
	    			String file = output + meeting + "_" + title + ".xml";
	    			fw.newFile(file, abstr.toString("xml"));
	    		}
	    	}
	    	catch (Exception e) 
	    	{
	    		e.printStackTrace();
	    	}
	    }
	}

	public boolean ready() 
	{
		return extracted;
	}
}
