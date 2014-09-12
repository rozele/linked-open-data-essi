package essi.lod.data.agu;

public class Pre2000Session {

	private String sessionID = null;
	private String location = null;
	private String day = null;
	private String hour = null;
	private String sessionName = null;
	private String convenors = null;
	private String meeting = null;
	
	public void setSessionID (String i) { sessionID = i; }
	public void setLocation (String i) { location = i; }
	public void setDay (String i) { day = i; }
	public void setHour (String i) { hour = i; }
	public void setSessionName (String i) { sessionName = i; }
	public void setConvenors (String i) { convenors = i; }
	public void setMeeting (String i) { meeting = i; }
	
	public String getSessionID () { return sessionID; }
	public String getLocation () { return location; }
	public String getDay () { return day; }
	public String getHour () { return hour; }
	public String getSessionName () { return sessionName; }
	public String getConvenors () { return convenors; }
	public String getMeeting () { return meeting; }
	
}