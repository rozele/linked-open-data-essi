package org.agu.essi.util;

import java.util.HashMap;

public class Utils {
	
	public static HashMap <String, String> getAguDatabases() {
		
	  // create a HashMap of AGU meetings/data directory key/value pairs
	  // AGU nomenclature - FM = Fall Meeting, JA = Joint Assembly, SM = Spring Meeting (changed to Joint Assembly in 2008)
	  HashMap <String, String> databases = new HashMap <String, String> ();
	  databases.put("/data/epubs/wais/indexes/fm10/fm10", "fm10");
	  databases.put("/data/epubs/wais/indexes/ja10/ja10", "ja10");
	  databases.put("/data/epubs/wais/indexes/fm09/fm09", "fm09");
	  databases.put("/data/epubs/wais/indexes/ja09/ja09", "ja09");
	  databases.put("/data/epubs/wais/indexes/fm08/fm08", "fm08");
	  databases.put("/data/epubs/wais/indexes/ja08/ja08", "ja08");
	  databases.put("/data/epubs/wais/indexes/fm07/fm07", "fm07");
	  databases.put("/data/epubs/wais/indexes/sm07/sm07", "sm07");
	  databases.put("/data/epubs/wais/indexes/fm06/fm06", "fm06");
	  databases.put("/data/epubs/wais/indexes/sm06/sm06", "sm06");
	  databases.put("/data/epubs/wais/indexes/fm05/fm05", "fm05");
	  databases.put("/data/epubs/wais/indexes/sm05/sm05", "sm05");
	  return databases;
	  
	}
	
}