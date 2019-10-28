
package com.project.files.RemoteDriveListener;



import com.microsoft.azure.storage.blob.*;

import com.project.RemoteDriveListener;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RemoteDriverListnerTest {

    private static final int COUNTTHREADS = 20;

    @Test
    public void testRemoteListner() throws InvalidKeyException, MalformedURLException {
             ContainerURL containerURL;
            String accountName = "codos1";
            String accountKey = "MVMF3m1GM/+ficIoLQtP/d1QbgVvTbvfXsjxUW6EBtgmO31NHnLDPCsTQJSc1IC4MZ1FeeWDySebhYdWtz0wKg==";

            // Create a ServiceURL to call the Blob service. We will also use this to
            // construct the ContainerURL
            SharedKeyCredentials creds = new SharedKeyCredentials(accountName, accountKey);
            // We are using a default pipeline here, you can learn more about it at
            // https://github.com/Azure/azure-storage-java/wiki/Azure-Storage-Java-V10-Overview


             ServiceURL serviceURL = new ServiceURL(new URL("https://" + accountName + ".blob.core.windows.net"),
                    StorageURL.createPipeline(creds, new PipelineOptions()));

            // Let's create a container using a blocking call to Azure Storage
            // If container exists, we'll catch and continue
            containerURL = serviceURL.createContainerURL("pd-quickstart1");

            /*Runnable worker = new RemoteDriveListener(containerURL);

                exe.execute(worker);*/

                RemoteDriveListener.listBlobs(containerURL);






    }


}

