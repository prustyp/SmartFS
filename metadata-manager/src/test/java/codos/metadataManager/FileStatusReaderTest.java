package codos.metadataManager;

import static org.junit.Assert.*;

import javafx.util.Pair;
import org.junit.Test;

public class FileStatusReaderTest {
  @Test
  public void fileStatusReaderTest()
  {
    FileStatusReader fileStatusReader=new FileStatusReader();
    Pair<String, String> statusOfFile=
        fileStatusReader.getStatusForFile("/Users/pprusty05/osf18exp/cronJobTest1/a.txt.10");
    System.out.println(statusOfFile);

  }

  @Test
  public void testStringManipulation() {
    String test1 = "/a/b/c.txt";
    String test2 = "/a/d";
    assertTrue(test1.startsWith(test2));

  }

}
