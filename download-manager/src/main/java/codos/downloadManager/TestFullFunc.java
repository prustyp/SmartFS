/*
package codos.downloadManager;

import org.junit.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Scanner;

public class TestFullFunc {


    String name;
    String choice;

    @Test
    public void testCode() throws IOException, InvalidKeyException {
                System.out.println("inside testCode()");
                String fname;
                ArrayList<String> filePath=new ArrayList<>();

                Scanner scan=new Scanner(System.in);
                choice=scan.next();

                switch(choice)
                {

                    case "T":

                        System.out.println("enter the filename");

                        fname = scan.next();
                        System.out.println("metadata information to download file" + fname);
                        filePath.add(fname);
                        DownloadTask d=new DownloadTask(filePath);
                        d.downloadChunk();
                        System.out.println("Downloaded file to destination->F:\\OSproject\\osf18repo\\storage-blobs-java-v10-quickstart-master");
                        break;

                    case "E":
                        System.out.println("Exit");
                        System.exit(0);
                    default:
                        break;

                }

    }


}
*/
