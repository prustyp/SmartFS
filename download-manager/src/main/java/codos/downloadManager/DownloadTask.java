package codos.downloadManager;

import com.microsoft.azure.storage.blob.BlockBlobURL;
import com.microsoft.azure.storage.blob.ContainerURL;
import com.microsoft.azure.storage.blob.PipelineOptions;
import com.microsoft.azure.storage.blob.ServiceURL;
import com.microsoft.azure.storage.blob.SharedKeyCredentials;
import com.microsoft.azure.storage.blob.StorageURL;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * download all the chucks using bloburl
 */
public class DownloadTask {

  private static final int COUNTTHREADS = 10;
  List<String> filePathList;
  int threads;


  public DownloadTask(List<String> filePathList, int threads) {

    this.filePathList = filePathList;
    this.threads = threads;
  }

  /**
   * method to download chunks(blobs) parallely using executor service
   * @throws InvalidKeyException
   * @throws IOException
   */
  public void downloadChunk() throws InvalidKeyException, IOException {

    ContainerURL containerURL;
    //parallel download using thread pool
    ExecutorService exe = Executors.newFixedThreadPool(this.threads);


    //create bloburl
    for (int jj = 0; jj < filePathList.size(); jj++) {
      String accountName = "codos1";
      String accountKey = "MVMF3m1GM/+ficIoLQtP/d1QbgVvTbvfXsjxUW6EBtgmO31NHnLDPCsTQJSc1IC4MZ1FeeWDySebhYdWtz0wKg==";

      File sourceFile = new File(String.valueOf(filePathList.get(jj)));//added string.valueof()
      SharedKeyCredentials creds = new SharedKeyCredentials(accountName, accountKey);
      // We are using a default pipeline here, you can learn more about it at
      // https://github.com/Azure/azure-storage-java/wiki/Azure-Storage-Java-V10-Overview
      final ServiceURL serviceURL = new ServiceURL(
          new URL("https://" + accountName + ".blob.core.windows.net"),
          StorageURL.createPipeline(creds, new PipelineOptions()));
      containerURL = serviceURL.createContainerURL("pp-quickstart1");



      //create blockbloburl
      BlockBlobURL blobURL = containerURL
          .createBlockBlobURL(String.valueOf(filePathList.get(jj)));



      //create runnable object
      Runnable worker = new BlobDownloads(blobURL, sourceFile);


      //parallel download using thread pool
      exe.execute(worker);

    }

    exe.shutdown();

    //// Wait until all threads are finish
    while (!exe.isTerminated()) {

    }

  }
}

