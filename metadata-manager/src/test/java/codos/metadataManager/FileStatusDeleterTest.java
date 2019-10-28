package codos.metadataManager;

import static org.junit.Assert.*;

import javafx.util.Pair;
import org.junit.Test;

public class FileStatusDeleterTest {

  @Test
  public void testDeleteOfStatus() {
    new FileStatusWriter("testPath","DOWNLOADED");
    FileStatusDeleter.DeleteFileStatusFromDB("testPath");

    FileStatusReader fileStatusReader = new FileStatusReader();
    Pair<String, String> fileStatus = fileStatusReader.getStatusForFile("testPath");
    assertTrue(fileStatus.getKey().equals("NO_STATUS"));


  }

}