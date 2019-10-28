import static org.junit.Assert.*;

import java.io.File;
import org.junit.Test;

public class CoralSendFileToAzureTest {

  @Test
  public void coraltest() throws Exception
  {
    CoralSendFileToAzure coralSendFileToAzure=new CoralSendFileToAzure();


//    for(int i = 0;i < 1;i++) {
//      long startTime = System.currentTimeMillis();
//      coralSendFileToAzure.uploadFile("/Users/pprusty05/osf18exp/TestFolder/random.file");
//      long endTime = System.currentTimeMillis();
//      long totalTimeInSec = (endTime - startTime) / 1000;
//      System.out.println(i + " Time taken for upload: " + totalTimeInSec);
//    }
    for(int i = 0;i < 1;i++) {
      long startTime1 = System.currentTimeMillis();
      //(new File("/Users/pprusty05/osf18exp/Coral/xaa")).delete();
      coralSendFileToAzure.downloadFile("/Users/pprusty05/osf18exp/TestFolder/random.file");

      long endTime1 = System.currentTimeMillis();
      long totalTimeInSec1 = (endTime1 - startTime1);
      System.out.println(i + " Time taken for download: " + totalTimeInSec1);
    }

  }

}