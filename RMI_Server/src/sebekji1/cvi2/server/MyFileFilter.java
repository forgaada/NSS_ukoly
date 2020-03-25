package sebekji1.cvi2.server;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

public class MyFileFilter implements FilenameFilter{

	private final String okFileExtension = new String("dbcsv");
	
	public boolean accept(File arg0, String name) {

		if (!name.toLowerCase().endsWith(".dbcsv")){
      return false;
    }else{
      return true;
    }
	}

}
