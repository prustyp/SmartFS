package codos.downloadManager;

import java.io.File;
import java.util.ArrayList;

public class ChunksDelete {

  public void deleteChunks(ArrayList<String> listOfChunks) {
    listOfChunks.stream().forEach(s -> (new File(String.valueOf(s))).delete());
  }

}
