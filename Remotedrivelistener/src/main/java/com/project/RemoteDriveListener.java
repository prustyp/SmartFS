package com.project;



import com.microsoft.azure.storage.blob.*;
import com.microsoft.azure.storage.blob.models.BlobItem;
import com.microsoft.azure.storage.blob.models.ContainerCreateResponse;
import com.microsoft.azure.storage.blob.models.ContainerListBlobFlatSegmentResponse;
import com.microsoft.rest.v2.RestException;
import com.microsoft.rest.v2.util.FlowableUtil;

import io.reactivex.Single;



    public class RemoteDriveListener {


			public static void listBlobs(ContainerURL containerURL) {
		// Each ContainerURL.listBlobsFlatSegment call return up to maxResults
		// (maxResults=10 passed into ListBlobOptions below).
		// To list all Blobs, we are creating a helper static method called
		// listAllBlobs,
		// and calling it after the initial listBlobsFlatSegment call
				System.out.println("inside list blobs");
		ListBlobsOptions options = new ListBlobsOptions();
		options.withMaxResults(100);
		containerURL.listBlobsFlatSegment(null, options, null)
				.flatMap(containerListBlobFlatSegmentResponse -> listAllBlobs(containerURL,
						containerListBlobFlatSegmentResponse))
				.subscribe(response -> {
					long size=0L;
					for(BlobItem blob: response.body().segment().blobItems())
					{

						size+=blob.properties().contentLength();

					}
					System.out.println("size used by blobs is"+size );
					System.out.println("Completed list blobs request.");
					System.out.println(response.statusCode());
				});
	}

	private  static Single<ContainerListBlobFlatSegmentResponse> listAllBlobs(ContainerURL url,
																			 ContainerListBlobFlatSegmentResponse response) {
		// Process the blobs returned in this result segment (if the segment is empty,
		// blobs() will be null.
		if (response.body().segment() != null) {
			for (BlobItem b : response.body().segment().blobItems()) {
				String output = "Blob name: " + b.name();
				if (b.snapshot() != null) {
					output += ", Snapshot: " + b.snapshot();
				}
				System.out.println(output);
			}
		} else {
			System.out.println("There are no more blobs to list off.");
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

			return url.listBlobsFlatSegment(nextMarker, new ListBlobsOptions().withMaxResults(100), null).flatMap(
					containersListBlobFlatSegmentResponse -> listAllBlobs(url, containersListBlobFlatSegmentResponse));
		}
	}

		 /* @Override
		  public void run() {
			  ContainerURL containerURL;
			  CloudBlobContainer cloudCont = null;
			  // Creating a sample file to use in the sample
			  File sampleFile = null;

			  //try {
			//  sampleFile = createTempFile();

			 // File downloadedFile = File.createTempFile("blobDownloads", ".txt");

			  // Retrieve the credentials and initialize SharedKeyCredentials
			  String accountName = "codos1";
			  String accountKey = "MVMF3m1GM/+ficIoLQtP/d1QbgVvTbvfXsjxUW6EBtgmO31NHnLDPCsTQJSc1IC4MZ1FeeWDySebhYdWtz0wKg==";

			  // Create a ServiceURL to call the Blob service. We will also use this to
			  // construct the ContainerURL
			  SharedKeyCredentials creds = null;
			  try {
				  creds = new SharedKeyCredentials(accountName, accountKey);
			  } catch (InvalidKeyException e) {
				  e.printStackTrace();
			  }
			  // We are using a default pipeline here, you can learn more about it at
			  // https://github.com/Azure/azure-storage-java/wiki/Azure-Storage-Java-V10-Overview



			  try {
				  final ServiceURL serviceURL;
				  serviceURL = new ServiceURL(new URL("https://" + accountName + ".blob.core.windows.net"),
						  StorageURL.createPipeline(creds, new PipelineOptions()));
				  containerURL = serviceURL.createContainerURL("pd-quickstart1");
				  System.out.println("tracking method!!");
				  listBlobs(containerURL);
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
			  } catch (MalformedURLException e) {
				  e.printStackTrace();
			  }


			  // Let's create a container using a blocking call to Azure Storage
			  // If container exists, we'll catch and continue
		  }*/
	  }