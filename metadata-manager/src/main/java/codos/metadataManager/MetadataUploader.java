package codos.metadataManager;

import com.microsoft.azure.storage.blob.BlockBlobURL;
import com.microsoft.azure.storage.blob.CommonRestResponse;
import com.microsoft.azure.storage.blob.ContainerURL;
import com.microsoft.azure.storage.blob.PipelineOptions;
import com.microsoft.azure.storage.blob.ServiceURL;
import com.microsoft.azure.storage.blob.SharedKeyCredentials;
import com.microsoft.azure.storage.blob.StorageURL;
import com.microsoft.azure.storage.blob.TransferManager;
import com.microsoft.azure.storage.blob.models.ContainerCreateResponse;
import com.microsoft.rest.v2.RestException;
import io.reactivex.Single;
import java.io.File;
import java.net.URL;
import java.nio.channels.AsynchronousFileChannel;

public class MetadataUploader {

  public String dbName;

  public MetadataUploader(String dbName) throws Exception {
    this.dbName = dbName;

    this.metadataUpload();
  }

  public void uploadBlob(BlockBlobURL blob, File sourceFile) throws Exception {

    AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(sourceFile.toPath());

    Single<CommonRestResponse> restResponseSingle = TransferManager
        .uploadFileToBlockBlob(fileChannel, blob, 8 * 1024 * 1024, null);
    if (restResponseSingle.blockingGet().response().statusCode() == 201) {
      // System.out.println("Upload complete for " + sourceFile.getAbsolutePath());
    } else {
      //System.out.println("Error in uploading");
    }
  }

  public void metadataUpload() throws Exception {
    File db = new File(dbName);
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
    BlockBlobURL blobURL = containerURL.createBlockBlobURL(dbName);

    this.uploadBlob(blobURL, db);


  }


}
