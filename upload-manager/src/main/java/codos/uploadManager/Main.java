package codos.uploadManager;

import codos.metadataManager.FileStatusWriter;
import codos.metadataManager.MetadataManager;
import codos.recentFileManager.RecentFileInfo;
import codos.recentFileManager.RecentFileManager;
import java.util.List;
import javafx.util.Pair;

/**
 * Main function to test upload of chunks
 * with following arguments:
 * filepath
 * number of threads:10
 */

public class Main {

  public static void main(String[] args) throws Exception {

    //input arguments
    String filePath = args[0];
    int threads = Integer.parseInt(args[1]);

    //uploading decision
    RecentFileManager recentFileManager = new RecentFileManager(filePath);
    RecentFileInfo recentFileInfo = recentFileManager.getRecentFileInfo();
    if (!shouldUpload(recentFileInfo)) {
      return;
    }


    //start counting time
    long startTime = System.currentTimeMillis();

    System.out.println("Starting filePath: " + filePath + " Threads : " + threads);

    //metadata give information of chunks by taking filepath as input
    MetadataManager metadataManager = new MetadataManager();
    List<Pair<String, Integer>> chunkDataList = metadataManager.metadataBuilder(filePath);

    //split the file using chunkdatalist
    FileSplitter fileSplitter = new FileSplitter(filePath, chunkDataList);
    List<String> listOfChunks = fileSplitter.splitFiles();

    //set status to uploading
    new FileStatusWriter(filePath, "UPLOADING");

    //upload the chunks parallely
    FileUploadWorker fileUploadWorker = new FileUploadWorker(listOfChunks);
    fileUploadWorker.uploadFileChunks();

    //set status to uploaded
    new FileStatusWriter(filePath, "UPLOADED");

    //delete the chunks and the file from local
    FilesDeleter.deleteChunks(listOfChunks);
    FilesDeleter.deleteFile(filePath);

    //end counting time
    long endTime = System.currentTimeMillis();
    long totalTimeInSec = (endTime - startTime) / 1000;
    System.out.println("Finished filePath: " + filePath + " Threads : " + threads
        + " Time taken : " + totalTimeInSec);


  }


  /**
   * condition to upload a file
   * @param recentFileInfo object containing status,timestamp,file open info
   * @return true means file should be uploaded
   */
  public static boolean shouldUpload(RecentFileInfo recentFileInfo) {
    if (recentFileInfo.status == "DOWNLOADED") {
      if (recentFileInfo.isFileOpen == false) {
        if (System.currentTimeMillis() - Long.parseLong(recentFileInfo.timestamp) > 1.8e+6) {
          return true;
        }
      }
    } else if (recentFileInfo.status == "NO_STATUS") {
      return true;
    }
    return false;
  }


}
