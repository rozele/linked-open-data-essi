package essi.lod.util;

import java.io.File;
import java.util.Vector;

public class GetListOfFiles {
  
  Vector <String> files = new Vector <String> ();

  public void clear () { files.clear(); }
  
  public Vector <String> Process ( File aFile ) {
   
    if ( aFile.isFile() ) { files.add( aFile.toString() ); }
     
    if ( aFile.isDirectory() ) {
      File[] listOfFiles = aFile.listFiles();
      if ( listOfFiles != null ) {
        for (int i = 0; i < listOfFiles.length; i++) { Process(listOfFiles[i]); }
      } 
    }
    return files;
    
  }

}