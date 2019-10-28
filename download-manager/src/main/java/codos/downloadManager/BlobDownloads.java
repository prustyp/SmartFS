package codos.downloadManager;

import com.microsoft.azure.storage.blob.BlobRange;
import com.microsoft.azure.storage.blob.BlockBlobURL;
import com.microsoft.rest.v2.util.FlowableUtil;
import java.io.File;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * download a single blob
 */
public class BlobDownloads implements Runnable {

  public BlockBlobURL blob;
  public File sourceFile;

  public BlobDownloads(BlockBlobURL blob, File sourceFile) {

    this.blob = blob;
    this.sourceFile = sourceFile;
  }


  /**
   *method to download a blob
   * @param blobURL path where the chunks stored in cloud
   * @param sourceFile filepath to be downloaded
   */

  public void getBlob(BlockBlobURL blobURL, File sourceFile) {
    try {

      blobURL.download(new BlobRange().withOffset(0).withCount(4 * 1024 * 1024L), null, false, null)
          .flatMapCompletable(response -> {
            AsynchronousFileChannel channel = AsynchronousFileChannel
                .open(Paths.get(sourceFile.getPath()),
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE);

            return FlowableUtil.writeFile(response.body(null), channel);
          })
          //wait until download complete
          .blockingAwait();


    } catch (Exception ex) {

    }
  }


  /**
   * will be called by runnable object in Downloadtask
   */
  @Override
  public void run() {

    //System.out.println("inside thread run for parallel operations");
    try {

      getBlob(this.blob, this.sourceFile);

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
