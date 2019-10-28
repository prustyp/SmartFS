package codos.recentFileManager;

import codos.metadataManager.FileStatusReader;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import javafx.util.Pair;

public class RecentFileManager {
  public String filePath;

  public RecentFileManager(String filePath){
  this.filePath=filePath;

  }
  public RecentFileInfo getRecentFileInfo() {


    FileStatusReader fileStatusReader = new FileStatusReader();
    Pair<String,String> fileInfo = fileStatusReader.getStatusForFile(this.filePath);
    Set<File> setOfOpenFiles=lsof();
    boolean isFileOpen =false;
    for(File openFilePath : setOfOpenFiles)
    {
      if(openFilePath.getAbsolutePath().equals(this.filePath))
      {
        isFileOpen=true;
      }


    }
    if(isFileOpen){
      //System.out.println("OPEN");
    }
    else{
      //System.out.println("CLOSED");
    }

    return new RecentFileInfo(fileInfo.getKey(),fileInfo.getValue(),isFileOpen);

  }
  public static Set<File> lsof() {
    Set<File> files = new LinkedHashSet<File>();
    File fds = new File("/proc/self/fd");
    if (fds.exists()) {
      for (File fd : fds.listFiles()) {
        try {
          files.add(fd.getCanonicalFile());
        }
        catch (IOException ignore) {
        }
      }
    }
    return files;
  }


    //file is open or not




  }


