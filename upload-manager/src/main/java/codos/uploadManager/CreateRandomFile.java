package codos.uploadManager;

import java.io.File;
import java.io.RandomAccessFile;

public class CreateRandomFile {

  public void fileCreate(String filePath) throws Exception {
    RandomAccessFile f = new RandomAccessFile(new File(filePath), "rw");
    f.setLength(1024 * 1024 * 1024 * 5L);
    f.close();
  }

  public void fileCreate(String filePath, long sizeInMbs) throws Exception {
    RandomAccessFile f = new RandomAccessFile(new File(filePath), "rw");
    f.setLength(1024L * 1024L * sizeInMbs);
    f.close();
  }
}



