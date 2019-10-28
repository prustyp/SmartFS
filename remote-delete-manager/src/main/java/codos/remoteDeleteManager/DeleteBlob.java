package codos.remoteDeleteManager;

import com.microsoft.azure.storage.blob.BlockBlobURL;
import com.microsoft.azure.storage.blob.ContainerURL;
import com.microsoft.azure.storage.blob.PipelineOptions;
import com.microsoft.azure.storage.blob.ServiceURL;
import com.microsoft.azure.storage.blob.SharedKeyCredentials;
import com.microsoft.azure.storage.blob.StorageURL;
import com.microsoft.azure.storage.blob.models.ContainerCreateResponse;
import com.microsoft.rest.v2.RestException;
import java.net.URL;

public class DeleteBlob {
  public String chunkBlobInCloud;

  public DeleteBlob(String chunkBlobInCloud ) throws Exception {

    ContainerURL containerURL;
    this.chunkBlobInCloud = chunkBlobInCloud;

    /**
     *
     * Create the container URL
     *
     *
     */
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

    /**
     * Create the blobUrl
     */
    final BlockBlobURL blobURL = containerURL.createBlockBlobURL(this.chunkBlobInCloud);

    /**
     * Actual delete of blobs
     */
    blobURL.delete(null, null, null).blockingGet();

  }

}
