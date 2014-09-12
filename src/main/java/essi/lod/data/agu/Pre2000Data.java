package essi.lod.data.agu;

import java.util.Vector;
import essi.lod.entity.agu.Author;

public class Pre2000Data {
	
	private String time = null;
	private String abstractNumber = null;
	private String title = null;
	private Vector <Author> authors = new Vector <Author> ();
	private String abstractText = null;
	private Vector <String> keywords = new Vector <String> ();
	private String section = null;
	private String meeting = null;
	
	public void setTime (String i) { time = i; }
	public void setAbstractNumber (String i) { abstractNumber = i; }
	public void setTitle (String i) { title = i; }
	public void addAuthor (Author a) { authors.add(a); }
	public void setAbstractText (String i) { abstractText = i; }
	public void addKeyword (String i) { keywords.add(i); }
	public void setSection (String i) { section = i; }
	public void setMeeting (String i) { meeting = i; }
	
	public String getTime () { return time; }
	public String getAbstractNumber () { return abstractNumber; }
	public String getTitle () { return title; }
	public Vector <Author> getAuthors () { return authors; }
	public String getAbstractText () { return abstractText; }
	public Vector <String> getKeywords () { return keywords; }
	public String getSection () { return section; }
	public String getMeeting () { return meeting; }
	
}