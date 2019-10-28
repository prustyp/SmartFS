package codos.remoteDeleteManager;


import codos.metadataManager.FileStatusDeleter;
import codos.metadataManager.FileStatusReader;
import codos.metadataManager.MetadataManager;
import java.io.File;
import java.util.List;
import javafx.util.Pair;

/**
 * This class remote deletes large file from cloud
 */
public class RemoteDeleteLargeFile {

  public RemoteDeleteLargeFile() throws Exception {
    this.deleteFileNotPresentInLocal();

  }

  public void deleteFileNotPresentInLocal() throws Exception {
    FileStatusReader fileStatusReader = new FileStatusReader();
    List<String> downloadedFilesList = fileStatusReader.getListOfDownloadedFiles();
    for (int i = 0; i < downloadedFilesList.size(); i++) {
      File file = new File(downloadedFilesList.get(i));
      if (!file.exists()) {
        FileStatusDeleter.DeleteFileStatusFromDB(downloadedFilesList.get(i));
        this.deleteFileInRemote(downloadedFilesList.get(i));

      }
    }
  }

  public void deleteFileInRemote(String filePath) throws Exception {
    MetadataManager metadataManager = new MetadataManager();
    List<Pair<String, Integer>> chunkInfo = metadataManager.getMetaDataForALargeFile(filePath);
    for (int i = 0; i < chunkInfo.size(); i++) {
      DeleteBlob deleteBlob = new DeleteBlob(chunkInfo.get(i).getKey());
    }
  }


}
