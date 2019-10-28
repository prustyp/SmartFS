package codos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class SegmentMaker {

  //splitfile
  public List<String> splitFiles(String filePath) throws Exception {
    ArrayList<String> listOfChunks = new ArrayList<>();

    //input file and number of chunk
    Path inputPath = Paths.get(filePath);
    int numberOfFiles = 3;

    try (FileChannel inputChannel = FileChannel.open(inputPath, StandardOpenOption.READ)) {
      for (int j = 0; j < numberOfFiles; j++) {
        String outputFilename = "block"+j;
        //j=0, a/b/c-00001.txt
        listOfChunks.add(outputFilename);
        Path outputPath = inputPath.getParent().resolve(outputFilename);

        try (FileChannel outputChannel = FileChannel
            .open(outputPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
          inputChannel.transferTo((long)j * 3 * 1024l * 1024l,
              3 * 1024l * 1024l,
              outputChannel);
        }
      }
    }
    return listOfChunks;

  }


  //compress file
  public void compressTozipFile(String inputFile, String gzipFile)  {
    try {

      //Check if file exist

      File file=new File(inputFile);
      System.out.println(file.length());
    FileInputStream fis = new FileInputStream(file);
    FileOutputStream fos = new FileOutputStream(gzipFile);
    GZIPOutputStream gzipOS = new GZIPOutputStream(fos);
    byte[] buffer = new byte[1024];
    int len;
    while ((len = fis.read(buffer)) != -1) {
      gzipOS.write(buffer, 0, len);
    }
    //close resources
    gzipOS.close();
    fos.close();
    fis.close();
    File gipFile=new File(gzipFile);
      System.out.println(gipFile.length());
  } catch (Exception e) {
    e.printStackTrace();
  }

}

  }


