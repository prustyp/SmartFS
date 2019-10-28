package codos.uploadManager;

import com.microsoft.azure.storage.blob.BlockBlobURL;
import com.microsoft.azure.storage.blob.CommonRestResponse;
import com.microsoft.azure.storage.blob.TransferManager;
import io.reactivex.Single;
import java.io.File;
import java.nio.channels.AsynchronousFileChannel;


/**
 * Upload a single blob.
 */
public class BlobUploadWorker implements Runnable {


  public BlockBlobURL blob;
  public File sourceFile;

  public BlobUploadWorker(BlockBlobURL blob, File sourceFile) {
    this.blob = blob;
    this.sourceFile = sourceFile;

  }

  /**
   * Method to upload a blob
   *
   * @param blob : path where the file will store in cloud.
   * @param sourceFile : file we want to upload.
   */
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



  /**
   * this will be called inside FileUploadWorker by runnable object.
   */
  @Override
  public void run() {
    try {
      uploadBlob(this.blob, this.sourceFile);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
