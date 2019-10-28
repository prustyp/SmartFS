package codos.metadataManager;

import static java.lang.Math.ceil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import org.apache.commons.codec.digest.DigestUtils;



public class MetadataManager {


  /**
   * metadataBuilder will be called by UploadManager
   *
   * It does the followings:
   * 1-see the size of the sourcefile and decide the number of chunks.
   * 2-create the db with unique name and the table within it
   * 3-put chunkpath and chuncksize in
   * table
   *
   * @param sourceFilePath :Filepath given by UploadManager
   * @return Pair<>(chunkpath, CHUNK_SIZE)
   */
  public List<Pair<String, Integer>> metadataBuilder(String sourceFilePath) {
    List<Pair<String, Integer>> chunkDataList = new ArrayList<>();
    File sourceFile = new File(sourceFilePath);


    //filesize in MB
    long fileSize = (long) (ceil(sourceFile.length() / 1024 / 1024));
    long lastChunkSize = fileSize;



    //condition to get chunk from file
    if (fileSize >= MetadataConstants.THRESHOLD_FILE_SIZE) {
      int numberOfChunks = (int) ceil((fileSize) / MetadataConstants.CHUNK_SIZE);



      //get chunk names and size
      for (int i = 1; i <= numberOfChunks; i++) {
        String chunkPath = sourceFilePath + "." + i;

        if (lastChunkSize < MetadataConstants.CHUNK_SIZE) {
          chunkDataList.add(new Pair<>(chunkPath, (int) lastChunkSize));
        } else {
          chunkDataList.add(new Pair<>(chunkPath, MetadataConstants.CHUNK_SIZE));
        }
        lastChunkSize = lastChunkSize - MetadataConstants.CHUNK_SIZE;
      }
    }



    //create a db with unique name to keep chunk info
    ChunksSqliteManager chunksSqliteManager = new ChunksSqliteManager(
        this.getDbPath(sourceFilePath));                    // /a/b/1234.db
    chunksSqliteManager.createDb();



    //write the chunk info to db
    chunksSqliteManager.writeOnDB(chunkDataList);

    return chunkDataList;

  }



  /**
   * get db name
   */
  public String getDbPath(String sourceFilePath) {
    //Create a Db with a unique name i.e. MD5 of entire sourceFilePath
    String dbName = DigestUtils.md5Hex(sourceFilePath);
    String dbPath = MetadataConstants.DB_FILE_PREPEND_PATH + dbName + ".db";
    return dbPath;

  }



/**
 * getMetaDataForALargeFile is called by DownloadManager
 *
 * It does the followings:
 * 1-generate Md5Hash name from Sourcefilepath to get the unique Db name
 * 2-Read from Db and return Records of chunkDataList
 *
 * @param sourceFilePath: Filepath to be downloaded
 * @return Records of chunkDataList
 */
 public List<Pair<String, Integer>> getMetaDataForALargeFile(String sourceFilePath) {
    ChunksSqliteManager chunksSqliteManager = new ChunksSqliteManager(getDbPath(sourceFilePath));
    return chunksSqliteManager.readFromDB();
  }


}
