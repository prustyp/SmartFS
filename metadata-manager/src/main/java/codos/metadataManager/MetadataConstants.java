package codos.metadataManager;

public class MetadataConstants {


  //Threshold of size of file to be uploaded
  public static long THRESHOLD_FILE_SIZE = 5;

  //size of each chunk in Mb
  public static int CHUNK_SIZE = 2;

  //location where all the metadata Db will be saved
  //This location will have multiple dbs
  public static String DB_FILE_PREPEND_PATH = "/Users/pprusty05/osf18exp/DBTestFolder/";


  //Location where the file status db is stored
  public static String FILE_STATUS_DB_PATH = "/Users/pprusty05/osf18exp/DBTestFolder/MetadataStatus/FileStatus.db";


}
