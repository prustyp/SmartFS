import com.microsoft.azure.storage.blob.BlobRange;
import com.microsoft.azure.storage.blob.BlockBlobURL;
import com.microsoft.azure.storage.blob.ContainerURL;
import com.microsoft.azure.storage.blob.PipelineOptions;
import com.microsoft.azure.storage.blob.ServiceURL;
import com.microsoft.azure.storage.blob.SharedKeyCredentials;
import com.microsoft.azure.storage.blob.StorageURL;
import com.microsoft.azure.storage.blob.TransferManager;
import com.microsoft.azure.storage.blob.models.ContainerCreateResponse;
import com.microsoft.rest.v2.RestException;
import com.microsoft.rest.v2.util.FlowableUtil;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class CoralSendFileToAzure {


  public void uploadFile(String filePath) throws Exception {

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

    AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open((new File(filePath).toPath()));

    // Uploading a file to the blobURL using the high-level methods available in
    // TransferManager class
    // Alternatively call the PutBlob/PutBlock low-level methods from BlockBlobURL
    // type
    TransferManager.uploadFileToBlockBlob(fileChannel, blobURL, 25 * 1024 * 1024, null)
        .blockingGet();
  }

  public void downloadFile(String filePath) throws Exception {

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


    try {
      // Get the blob using the low-level download method in BlockBlobURL type
      // com.microsoft.rest.v2.util.FlowableUtil is a static class that contains
      // helpers to work with Flowable
      // BlobRange is defined from 0 to 4MB

      File file = new File(filePath);
      blobURL.download(new BlobRange().withOffset(0).withCount(30 * 1024L * 1024L), null, false, null)
          .flatMapCompletable(response -> {
            AsynchronousFileChannel channel = AsynchronousFileChannel.open(Paths.get(file.getPath()),
                StandardOpenOption.CREATE, StandardOpenOption.WRITE);

            return FlowableUtil.writeFile(response.body(null), channel);
          })
          .doOnComplete(
              () -> System.out.println("All blobs was downloaded to "))
          .blockingAwait();

      //DownloadTask download = new DownloadTask();
      //download.downloadChunk();


    } catch (Exception ex) {

      System.out.println(ex.toString());
    }

  }


}
