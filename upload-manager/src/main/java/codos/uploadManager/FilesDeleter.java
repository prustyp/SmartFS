package codos.uploadManager;

import java.io.File;
import java.util.List;

/**
 * delete file and chunks from local
 */
public class FilesDeleter {

  /**
   * delete all chunks
   * @param fileChunks
   */
  public static void deleteChunks(List<String> fileChunks) {
    fileChunks.stream().forEach(fileChunk -> (new File(fileChunk)).delete());
  }


  /**
   * delete the file in local
   * @param filePath
   */
  public static void deleteFile(String filePath) {
    (new File(filePath)).delete();
  }

}
