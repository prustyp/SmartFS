package codos.uploadManager;

import codos.metadataManager.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import org.junit.Test;
/*
Test for parallel upload by creating a temp file
 */
public class FileUploadWorkerTest {

  public File createTempFile() throws IOException {

    // Here we are creating a temporary file to use for download and upload to Blob storage
    File sampleFile = null;
    sampleFile = File.createTempFile("sampleFile", ".txt");
    //System.out.println(">> Creating a sample file at: " + sampleFile.toString());
    Writer output = new BufferedWriter(new FileWriter(sampleFile));
    output.write("Hello Azure!");
    output.close();
    return sampleFile;

  }





  @Test
  public void testParallelBlobUpload() throws Exception
  {
    ArrayList<String> filePathList = new ArrayList<>();
//    for(int i=0; i<5;i++){
//      filePathList.add(createTempFile().toString());
//    }
    filePathList.add("/Users/pprusty05/Desktop/f1.txt");
    filePathList.add("/Users/pprusty05/Desktop/f2.txt");
    filePathList.add("/Users/pprusty05/Desktop/f3.txt");
//    FileUploadWorker fileUploadWorker=new FileUploadWorker(filePathList);
//    fileUploadWorker.uploadFileChunks();
//    filePathList.stream().forEach(System.out::println);
  }
  @Test
  public void testParallelUploadOfRandomFile() throws Exception
  {
    //ArrayList<String> filePathList = new ArrayList<>();
    String filePath = "/Users/pprusty05/google_drive/OS_F2018/experiments_final/inputSourceFiles/a.txt";
    CreateRandomFile createRandomFile= new CreateRandomFile();
    createRandomFile.fileCreate(filePath, 250);

///    MetadataManager metadataManager = new MetadataManager();
//    List<Pair<String, Integer>> chunks = metadataManager.metadataBuilder(filePath);
//
//    FileSplitter fileSplitter =new FileSplitter(filePath,chunks.size());
//    List<String> listOfChunks=fileSplitter.splitFiles();

//    FileUploadWorker fileUploadWorker=new FileUploadWorker(listOfChunks);
//    fileUploadWorker.uploadFileChunks();
//    listOfChunks.stream().forEach(System.out::println);
  }
}
