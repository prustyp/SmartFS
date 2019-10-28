package codos.uploadManager;

import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

/**
 * Split file in to chunks
 */
public class FileSplitter {

  String filePath;
  //int numberOfChunks;
  //List<String> chunks;
  List<Pair<String, Integer>> chunkDataList;

  public FileSplitter(String filePath, List<Pair<String, Integer>> chunkDataList) {
    this.filePath = filePath;
    this.chunkDataList = chunkDataList;

  }



  /**
   * Method to split in to binary chunks
   * java NIO API
   * @return list of chunks
   */
  public List<String> splitFiles() throws Exception {
    ArrayList<String> listOfChunks = new ArrayList<>();

    //input file and number of chunk
    Path inputPath = Paths.get(this.filePath);
    int numberOfFiles = chunkDataList.size();

    try (FileChannel inputChannel = FileChannel.open(inputPath, StandardOpenOption.READ)) {
      for (int j = 0; j < numberOfFiles; j++) {
        String outputFilename = chunkDataList.get(j).getKey();
        //j=0, a/b/c-00001.txt
        listOfChunks.add(outputFilename);
        Path outputPath = inputPath.getParent().resolve(outputFilename);

        try (FileChannel outputChannel = FileChannel
            .open(outputPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
          inputChannel.transferTo((long)j * (long)chunkDataList.get(0).getValue() * 1024l * 1024l,
              (long)chunkDataList.get(j).getValue() * 1024l * 1024l,
              outputChannel);
        }
      }
    }
    return listOfChunks;

  }

}
