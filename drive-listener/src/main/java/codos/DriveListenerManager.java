package codos;

import codos.metadataManager.MetadataConstants;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * DriveListenerManager would look for large files in a folder
 */
public class DriveListenerManager {
  ArrayList<String> listOfFiles;

  public String directoryPath;


  /**
   * @param directoryPath: path to the folder where files are generating
   */
  public DriveListenerManager(String directoryPath)
  {
    this.directoryPath=directoryPath;
    listOfFiles = new ArrayList<>();
    File f = new File(directoryPath);
    this.listFilesForFolder(f);

  }
  public List<String> getListOfFilePaths()
  {
    return listOfFiles;
  }

  /**
   * listFilesForFolder would print the files > 100MB inside the folder
   * @param folder: path to the folder
   */
  public void listFilesForFolder(final File folder) {
    for (final File fileEntry : folder.listFiles()) {
      //check if there is a directory inside the folder
      if (fileEntry.isDirectory()) {
        listFilesForFolder(fileEntry);
      } else {
        if(fileEntry.length() > MetadataConstants.THRESHOLD_FILE_SIZE * 1024l * 1024l) {
          listOfFiles.add(fileEntry.getAbsolutePath());
          //System.out.println(fileEntry.getAbsolutePath());
        }
      }
    }
  }


}


