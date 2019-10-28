package codos.uploadManager;

import com.microsoft.azure.storage.blob.BlockBlobURL;
import com.microsoft.azure.storage.blob.ContainerURL;
import com.microsoft.azure.storage.blob.PipelineOptions;
import com.microsoft.azure.storage.blob.ServiceURL;
import com.microsoft.azure.storage.blob.SharedKeyCredentials;
import com.microsoft.azure.storage.blob.StorageURL;
import com.microsoft.azure.storage.blob.models.ContainerCreateResponse;
import com.microsoft.rest.v2.RestException;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Create bloburl for each chunks of a file Upload the chunks in parallel
 */
public class FileUploadWorker {


  //input
  private static final int MYTHREADS = 10;
  public List<String> filePathList;
  public int numberOfThreads;


  /**
   * This constructor will take list of filepaths
   * @param filePathList : list of number of chunks of a file.
   */
  public FileUploadWorker(List<String> filePathList) {

    this.filePathList = filePathList;
    this.numberOfThreads = MYTHREADS;
  }


  /**
   * For experiment perpose taking this constructor to set number of threads in main
   * @param filePathList
   * @param numberOfThreads
   */
  public FileUploadWorker(List<String> filePathList, int numberOfThreads) {
    this.filePathList = filePathList;
    this.numberOfThreads = numberOfThreads;

  }


  /**
   * Method to create bloburl and use executor service for parallel upload.
   */
  public void uploadFileChunks() throws Exception {


    //used for parallel upload
    ExecutorService executor = Executors.newFixedThreadPool(this.numberOfThreads);


    //create bloburl for each chunk
    for (String filePath : filePathList) {
      File sourceFile = new File(filePath);
      String accountName = "codos1";
      String accountKey = "MVMF3m1GM/+ficIoLQtP/d1QbgVvTbvfXsjxUW6EBtgmO31NHnLDPCsTQJSc1IC4MZ1FeeWDySebhYdWtz0wKg==";
      // Create a ServiceURL to call the Blob service. We will also use this to construct the ContainerURL
      SharedKeyCredentials creds = new SharedKeyCredentials(accountName, accountKey);
      // We are using a default pipeline here, you can learn more about it at https://github.com/Azure/azure-storage-java/wiki/Azure-Storage-Java-V10-Overview
      final ServiceURL serviceURL = new ServiceURL(
          new URL("https://" + accountName + ".blob.core.windows.net"), StorageURL
          .createPipeline(creds, new PipelineOptions()));

      // Let's create a container using a blocking call to Azure Storage
      // If container exists, we'll catch and continue
      ContainerURL containerURL = serviceURL.createContainerURL("pp-quickstart1");
      try {
        ContainerCreateResponse response = containerURL.create(null, null, null).blockingGet();
        //System.out.println("Container Create Response was " + response.statusCode());
      } catch (RestException e) {
        if (e instanceof RestException && ((RestException) e).response().statusCode() != 409) {
          throw e;
        } else {
          //System.out.println("quickstart container already exists, resuming...");
        }
      }


      // Create a BlockBlobURL to run operations on Blobs
      BlockBlobURL blobURL = containerURL.createBlockBlobURL(filePath);


      //Create a Runnable object.
      Runnable worker = new BlobUploadWorker(blobURL, sourceFile);
      executor.execute(worker);

    }
    executor.shutdown();


    // Wait until all threads are finish
    while (!executor.isTerminated()) {

    }
    // System.out.println("\nFinished all threads");

  }
}
