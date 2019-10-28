package codos.downloadManager;


import codos.metadataManager.FileStatusWriter;
import codos.metadataManager.MetadataManager;
import codos.recentFileManager.RecentFileInfo;
import codos.recentFileManager.RecentFileManager;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

/**
 * Main function to test download of chunks
 * with following arguments:
 * filepath
 * number of threads:10
 */
public class Main {

  public static void main(String[] args) throws Exception {

    //input arguments
    String filepath = args[0];
    int numberOfThread = Integer.parseInt(args[1]);
    ArrayList<String> listOfChunks = new ArrayList<>();


    //decision on downloading
    RecentFileManager recentFileManager = new RecentFileManager(filepath);
    RecentFileInfo recentFileInfo = recentFileManager.getRecentFileInfo();
    if (recentFileInfo.status.equals("UPLOADED")) {


      //get file chunks info from metadata
      MetadataManager meta = new MetadataManager();
      List<Pair<String, Integer>> listOfChunksInfoPair = meta.getMetaDataForALargeFile(filepath);

      int chunksSize = listOfChunksInfoPair.size();


      //get list of chunks from pair
      for (int ii = 0; ii < chunksSize; ii++) {

        String output = listOfChunksInfoPair.get(ii).getKey();
        listOfChunks.add(output);

      }

      //start counting time
      long startTime = System.currentTimeMillis();

      //set status to downloading
      new FileStatusWriter(filepath, "DOWNLOADING");


      //download chunks parallely
      DownloadTask downloadTask = new DownloadTask(listOfChunks, numberOfThread);
      downloadTask.downloadChunk();

      //merge the chunks to get the original file
      FileMerger fileMerger = new FileMerger(listOfChunks, filepath);
      fileMerger.mergeFiles();

      //set status to downloaded
      new FileStatusWriter(filepath, "DOWNLOADED");


      //delete the chunks locally
      ChunksDelete chunksDelete = new ChunksDelete();
      chunksDelete.deleteChunks(listOfChunks);

      //end of counting time
      long endTime = System.currentTimeMillis();
      long timeInSec = (endTime - startTime) / 1000;
      System.out.println(" Threads : " + numberOfThread
          + " time taken : " + timeInSec);

    }
  }


}
