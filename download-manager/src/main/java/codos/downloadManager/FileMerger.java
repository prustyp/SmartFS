package codos.downloadManager;


import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class FileMerger {

  public List<String> filePathList;
  public String destinationPath;

  public FileMerger(ArrayList<String> filePathList, String destinationPath) {
    this.filePathList = filePathList;
    this.destinationPath = destinationPath;
  }

  public void mergeFiles() throws Exception {


    Path outFile = Paths.get(destinationPath);
    System.out.println("TO " + outFile);
    try (FileChannel out = FileChannel.open(outFile, CREATE, WRITE)) {
      for (int ix = 0, n = filePathList.size(); ix < n; ix++) {
        Path inFile = Paths.get(String.valueOf(filePathList.get(ix)));
        try (FileChannel in = FileChannel.open(inFile, READ)) {
          for (long p = 0, l = in.size(); p < l; ) {
            p += in.transferTo(p, l - p, out);
          }
        }
      }
    }

  }


}
