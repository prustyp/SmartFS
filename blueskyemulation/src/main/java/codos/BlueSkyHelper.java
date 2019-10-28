package codos;

import com.microsoft.azure.storage.blob.BlobRange;
import com.microsoft.azure.storage.blob.BlockBlobURL;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.ContainerURL;
import com.microsoft.azure.storage.blob.PipelineOptions;
import com.microsoft.azure.storage.blob.ServiceURL;
import com.microsoft.azure.storage.blob.SharedKeyCredentials;
import com.microsoft.azure.storage.blob.StorageURL;
import com.microsoft.azure.storage.blob.TransferManager;
import com.microsoft.azure.storage.blob.models.ContainerCreateResponse;
import com.microsoft.rest.v2.util.FlowableUtil;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class BlueSkyHelper {


  static void uploadBlock(BlockBlobURL blob, File sourceFile) throws IOException {

    AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(sourceFile.toPath());

    TransferManager.uploadFileToBlockBlob(fileChannel, blob, 8 * 1024 * 1024, null)
        .subscribe(response -> {
          System.out.println("Completed upload request.");
          System.out.println(response.response().statusCode());
        });
  }


  static void getBlob(BlockBlobURL blobURL, File sourceFile) {
    try {
      // Get the blob using the low-level download method in BlockBlobURL type
      // com.microsoft.rest.v2.util.FlowableUtil is a static class that contains
      // helpers to work with Flowable
      // BlobRange is defined from 0 to 4MB

      blobURL.download(new BlobRange().withOffset(0).withCount(4 * 1024 * 1024L), null, false, null)
          .flatMapCompletable(response -> {
            AsynchronousFileChannel channel = AsynchronousFileChannel
                .open(Paths.get(sourceFile.getPath()),
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE);

            return FlowableUtil.writeFile(response.body(null), channel);
          })
          .doOnComplete(
              () -> System.out
                  .println("All blobs was downloaded to " + sourceFile.getAbsolutePath()))
          // To call it synchronously add .blockingAwait()

          .blockingAwait();

    } catch (Exception ex) {

      System.out.println(ex.toString());
    }
  }


  public void uploadFile(String filePath) throws Exception {
    ContainerURL containerURL;
    CloudBlobContainer cloudCont = null;
    String accountName = "codos1";
    String accountKey = "MVMF3m1GM/+ficIoLQtP/d1QbgVvTbvfXsjxUW6EBtgmO31NHnLDPCsTQJSc1IC4MZ1FeeWDySebhYdWtz0wKg==";

    // Create a ServiceURL to call the Blob service. We will also use this to
    // construct the ContainerURL
    SharedKeyCredentials creds = new SharedKeyCredentials(accountName, accountKey);
    // We are using a default pipeline here, you can learn more about it at
    // https://github.com/Azure/azure-storage-java/wiki/Azure-Storage-Java-V10-Overview
    final ServiceURL serviceURL = new ServiceURL(
        new URL("https://" + accountName + ".blob.core.windows.net"),
        StorageURL.createPipeline(creds, new PipelineOptions()));

    // Let's create a container using a blocking call to Azure Storage
    // If container exists, we'll catch and continue
    containerURL = serviceURL.createContainerURL("pp-quickstart1");
    //ContainerCreateResponse response = containerURL.create(null, null, null).blockingGet();
    final BlockBlobURL blobURL = containerURL.createBlockBlobURL(filePath);

    BlueSkyHelper.uploadBlock(blobURL, new File(filePath));

  }

  public static void downloadFile(String filePath) throws Exception {
    ContainerURL containerURL;
    CloudBlobContainer cloudCont = null;
    String accountName = "codos1";
    String accountKey = "MVMF3m1GM/+ficIoLQtP/d1QbgVvTbvfXsjxUW6EBtgmO31NHnLDPCsTQJSc1IC4MZ1FeeWDySebhYdWtz0wKg==";

    // Create a ServiceURL to call the Blob service. We will also use this to
    // construct the ContainerURL
    SharedKeyCredentials creds = new SharedKeyCredentials(accountName, accountKey);
    // We are using a default pipeline here, you can learn more about it at
    // https://github.com/Azure/azure-storage-java/wiki/Azure-Storage-Java-V10-Overview
    final ServiceURL serviceURL = new ServiceURL(
        new URL("https://" + accountName + ".blob.core.windows.net"),
        StorageURL.createPipeline(creds, new PipelineOptions()));

    containerURL = serviceURL.createContainerURL("pp-quickstart1");
    //ContainerCreateResponse response = containerURL.create(null, null, null).blockingGet();
    final BlockBlobURL blobURL = containerURL.createBlockBlobURL(filePath);
    BlueSkyHelper.getBlob(blobURL, new File(filePath));
  }


}

