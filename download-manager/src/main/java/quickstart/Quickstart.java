package quickstart;

import codos.downloadManager.DownloadTask;
import com.microsoft.azure.storage.blob.BlobRange;
import com.microsoft.azure.storage.blob.BlockBlobURL;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.ContainerURL;
import com.microsoft.azure.storage.blob.ListBlobsOptions;
import com.microsoft.azure.storage.blob.PipelineOptions;
import com.microsoft.azure.storage.blob.ServiceURL;
import com.microsoft.azure.storage.blob.SharedKeyCredentials;
import com.microsoft.azure.storage.blob.StorageURL;
import com.microsoft.azure.storage.blob.TransferManager;
import com.microsoft.azure.storage.blob.models.BlobItem;
import com.microsoft.azure.storage.blob.models.ContainerCreateResponse;
import com.microsoft.azure.storage.blob.models.ContainerListBlobFlatSegmentResponse;
import com.microsoft.rest.v2.RestException;
import com.microsoft.rest.v2.util.FlowableUtil;
import io.reactivex.Single;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Scanner;

public class Quickstart {

	static long totalSize = 0;
	static File createTempFile() throws IOException {

		// Here we are creating a temporary file to use for download and upload to Blob
		// storage
		File sampleFile = null;
		sampleFile = File.createTempFile("sample2", ".txt");
		System.out.println(">> Creating a sample file at: " + sampleFile.toString());
		Writer output = new BufferedWriter(new FileWriter(sampleFile));
		output.write("Hello Azure!");
		output.close();

		return sampleFile;
	}

	static void uploadFile(BlockBlobURL blob, File sourceFile) throws IOException {

		AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(sourceFile.toPath());

		// Uploading a file to the blobURL using the high-level methods available in
		// TransferManager class
		// Alternatively call the PutBlob/PutBlock low-level methods from BlockBlobURL
		// type
		TransferManager.uploadFileToBlockBlob(fileChannel, blob, 8 * 1024 * 1024, null).subscribe(response -> {
			System.out.println("Completed upload request.");
			System.out.println(response.response().statusCode());
		});
	}

	static void listBlobs(ContainerURL containerURL) {
		// Each ContainerURL.listBlobsFlatSegment call return up to maxResults
		// (maxResults=10 passed into ListBlobOptions below).
		// To list all Blobs, we are creating a helper static method called
		// listAllBlobs,
		// and calling it after the initial listBlobsFlatSegment call
		ListBlobsOptions options = new ListBlobsOptions();
		//options.withMaxResults(10);
		containerURL.listBlobsFlatSegment(null, options, null)
				.flatMap(containerListBlobFlatSegmentResponse -> listAllBlobs(containerURL,
						containerListBlobFlatSegmentResponse))
				.subscribe(response -> {
					System.out.println("Completed list blobs request. " + totalSize);
					System.out.println(response.statusCode());
				});

	}

	private static Single<ContainerListBlobFlatSegmentResponse> listAllBlobs(ContainerURL url,
			ContainerListBlobFlatSegmentResponse response) {
		// Process the blobs returned in this result segment (if the segment is empty,
		// blobs() will be null.
		if (response.body().segment() != null) {
			for (BlobItem b : response.body().segment().blobItems()) {
				totalSize = totalSize + b.properties().contentLength();
				//String output = "Blob name: " + b.name();
				if (b.snapshot() != null) {
					//output += ", Snapshot: " + b.snapshot();
				}
				//System.out.println(output);
			}
		} else {
			//System.out.println("There are no more blobs to list off.");
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

			return url.listBlobsFlatSegment(nextMarker, new ListBlobsOptions().withMaxResults(10), null).flatMap(
					containersListBlobFlatSegmentResponse -> listAllBlobs(url, containersListBlobFlatSegmentResponse));
		}
	}

	static void deleteBlob(BlockBlobURL blobURL) {
		// Delete the blob
		blobURL.delete(null, null, null).subscribe(response -> System.out.println(">> Blob deleted: " + blobURL),
				error -> System.out.println(">> An error encountered during deleteBlob: " + error.getMessage()));
	}

	static void getBlob(BlockBlobURL blobURL, File sourceFile) {
		try {
			// Get the blob using the low-level download method in BlockBlobURL type
			// com.microsoft.rest.v2.util.FlowableUtil is a static class that contains
			// helpers to work with Flowable
			// BlobRange is defined from 0 to 4MB

			blobURL.download(new BlobRange().withOffset(0).withCount(4 * 1024 * 1024L), null, false, null)
					.flatMapCompletable(response -> {
						AsynchronousFileChannel channel = AsynchronousFileChannel.open(Paths.get(sourceFile.getPath()),
								StandardOpenOption.CREATE, StandardOpenOption.WRITE);

						return FlowableUtil.writeFile(response.body(null), channel);
					})
					.doOnComplete(
							() -> System.out.println("All blobs was downloaded to " + sourceFile.getAbsolutePath()))
					// To call it synchronously add .blockingAwait()

					.blockingAwait();
			
			//DownloadTask download = new DownloadTask();
			//download.downloadChunk();
			

		} catch (Exception ex) {

			System.out.println(ex.toString());
		}
	}

	/*
	 * static void getBlobVal() {
	 * 
	 * DownloadTask dtasks=new DownloadTask(dow) }
	 */

	public static void main(String[] args)throws Exception{
		ContainerURL containerURL;
		CloudBlobContainer cloudCont = null;
		// Creating a sample file to use in the sample
		File sampleFile = null;

		try {
			sampleFile = createTempFile();

			File downloadedFile = File.createTempFile("blobDownloads", ".txt");

			// Retrieve the credentials and initialize SharedKeyCredentials
			String accountName = "codos1";
			String accountKey = "MVMF3m1GM/+ficIoLQtP/d1QbgVvTbvfXsjxUW6EBtgmO31NHnLDPCsTQJSc1IC4MZ1FeeWDySebhYdWtz0wKg==";

			// Create a ServiceURL to call the Blob service. We will also use this to
			// construct the ContainerURL
			SharedKeyCredentials creds = new SharedKeyCredentials(accountName, accountKey);
			// We are using a default pipeline here, you can learn more about it at
			// https://github.com/Azure/azure-storage-java/wiki/Azure-Storage-Java-V10-Overview
			final ServiceURL serviceURL = new ServiceURL(new URL("https://" + accountName + ".blob.core.windows.net"),
					StorageURL.createPipeline(creds, new PipelineOptions()));

			// Let's create a container using a blocking call to Azure Storage
			// If container exists, we'll catch and continue
			containerURL = serviceURL.createContainerURL("pd-quickstart1");

			try {
				ContainerCreateResponse response = containerURL.create(null, null, null).blockingGet();
				System.out.println("Container Create Response was " + response.statusCode());
			} catch (RestException e) {
				if (e instanceof RestException && ((RestException) e).response().statusCode() != 409) {
					throw e;
				} else {
					System.out.println("quickstart container already exists, resuming...");
				}
			}

			// Create a BlockBlobURL to run operations on Blobs
			final BlockBlobURL blobURL = containerURL.createBlockBlobURL("C:\\Users\\ADMIN\\Desktop\\SmartSysPro\\1GB.zip");

			// Listening for commands from the console
			System.out.println("Enter a command");
			System.out.println("(P)utBlob | (L)istBlobs | (G)etBlob | (D)eleteBlobs | (T)est Functionality | (E)xitSample");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			ArrayList<String> list ;
			//list=(ArrayList<String>)sampleFile;

			//String accountName = "<your-storage-account-name>";
			//String accountKey = "<your-storage-account-key>";

			while (true) {

				System.out.println("# Enter a command : ");
				String input = reader.readLine();

				switch (input) {
				case "P":
					System.out.println("Uploading the sample file into the container: " + containerURL);
					uploadFile(blobURL, sampleFile);
					break;
				case "L":
					System.out.println("Listing blobs in the container: " + containerURL);
					listBlobs(containerURL);
					break;
				case "G":
					System.out.println("Get the blob: " + blobURL.toString());

					long size = 0L;

//					Iterable<ListBlobItem> blobItems = container.getMetadata().entrySet();
//					for (ListBlobItem blobItem : blobItems) {
//						if (blobItem instanceof CloudBlob) {
//							CloudBlob blob = (CloudBlob) blobItem;
//							size += blob.getProperties().getLength();
//						}
//					}
//					System.out.println("Size: "+ size);

					 getBlob(blobURL, downloadedFile);
					break;
				case "D":
					System.out.println("Delete the blob: " + blobURL.toString());
					deleteBlob(blobURL);
					System.out.println();
					break;
				case "E":
					System.out.println("Cleaning up the sample and exiting!");
					containerURL.delete(null, null).blockingGet();
					downloadedFile.delete();
					System.exit(0);
					break;
					case "T":
						/*String fname;
						ArrayList<String> filePath=new ArrayList<>();
						Scanner scan=new Scanner(System.in);
						System.out.println("testing code functionality");
						System.out.println("enter the filename");

						fname = scan.next();
						System.out.println("metadata information to download file" + fname);
						filePath.add(fname);
						DownloadTask d=new DownloadTask(filePath);
						d.downloadChunk();
						System.out.println("Downloaded file to destination->F:\\OSproject\\osf18repo\\storage-blobs-java-v10-quickstart-master");*/
						break;
				default:
					break;
				}
			}
		} catch (InvalidKeyException e) {
			System.out.println("Invalid Storage account name/key provided");
		} catch (MalformedURLException e) {
			System.out.println("Invalid URI provided");
		} catch (RestException e) {
			System.out.println("Service error returned: " + e.response().statusCode());
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}