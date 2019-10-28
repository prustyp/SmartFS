package codos;


import com.microsoft.azure.storage.blob.ContainerURL;
import com.microsoft.azure.storage.blob.ListBlobsOptions;
import com.microsoft.azure.storage.blob.PipelineOptions;
import com.microsoft.azure.storage.blob.ServiceURL;
import com.microsoft.azure.storage.blob.SharedKeyCredentials;
import com.microsoft.azure.storage.blob.StorageURL;
import com.microsoft.azure.storage.blob.models.BlobItem;
import com.microsoft.azure.storage.blob.models.ContainerListBlobFlatSegmentResponse;
import io.reactivex.Single;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;

public class RemoteDriveListener {

  ContainerURL containerUrl;

  public long containerSize;
  public RemoteDriveListener() {

    this.containerSize = 0l;
    /**
     * Now set the containerUrl
     */
    String accountName = "codos1";
    String accountKey = "MVMF3m1GM/+ficIoLQtP/d1QbgVvTbvfXsjxUW6EBtgmO31NHnLDPCsTQJSc1IC4MZ1FeeWDySebhYdWtz0wKg==";

    SharedKeyCredentials creds = null;
    try {
      creds = new SharedKeyCredentials(accountName, accountKey);
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    }

    try {
      final ServiceURL serviceURL;
      serviceURL = new ServiceURL(new URL("https://" + accountName + ".blob.core.windows.net"),
          StorageURL.createPipeline(creds, new PipelineOptions()));
      this.containerUrl = serviceURL.createContainerURL("pp-quickstart1");
      /**
       * Container URL is done, now call the listBlobs method
       */
      this.listBlobs(this.containerUrl);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }

  }

  public void listBlobs(ContainerURL containerURL) {
    // Each ContainerURL.listBlobsFlatSegment call return up to maxResults
    // (maxResults=10 passed into ListBlobOptions below).
    // To list all Blobs, we are creating a helper static method called
    // listAllBlobs,
    // and calling it after the initial listBlobsFlatSegment call
    ListBlobsOptions options = new ListBlobsOptions();
    options.withMaxResults(10);
    containerURL.listBlobsFlatSegment(null, options, null)
        .flatMap(containerListBlobFlatSegmentResponse -> listAllBlobs(containerURL,
            containerListBlobFlatSegmentResponse))
        .blockingGet();
    /**
     * This code was giving error, basically you subscribed to the
     * response and without ``while(true){}`` it doesnot get executed
     * and with it, its stuck for ever.
     * It should be blockingGet, which blocks the child thread till GET is returned
     * and do all the computations in listAllBlobs
     */
        /*.subscribe(response -> {

          for (BlobItem blob : response.body().segment().blobItems()) {

            this.containerSize += blob.properties().contentLength();

          }
          System.out.println("size used by blobs is" + this.containerSize);
          //System.out.println("Completed list blobs request.");
          //System.out.println(response.statusCode());
        });*/
  }

  private Single<ContainerListBlobFlatSegmentResponse> listAllBlobs(ContainerURL url,
      ContainerListBlobFlatSegmentResponse response) {
    // Process the blobs returned in this result segment (if the segment is empty,
    // blobs() will be null.
    if (response.body().segment() != null) {
      for (BlobItem b : response.body().segment().blobItems()) {
        this.containerSize += b.properties().contentLength();
      }
    } else {
    }

    // If there is not another segment, return this response as the final response.
    if (response.body().nextMarker() == null) {
      return Single.just(response);
    } else {
      /*
       * IMPORTANT: ListBlobsFlatSegment returns the start of the next segment; you
       * MUST use this to get the next segment (after processing the current result
       * segment
       */

      String nextMarker = response.body().nextMarker();

      /*
       * The presence of the marker indicates that there are more blobs to list, so we
       * make another call to listBlobsFlatSegment and pass the result through this
       * helper function.
       */

      return url.listBlobsFlatSegment(nextMarker, new ListBlobsOptions().withMaxResults(10), null)
          .flatMap(
              containersListBlobFlatSegmentResponse -> listAllBlobs(url,
                  containersListBlobFlatSegmentResponse));
    }
  }

}