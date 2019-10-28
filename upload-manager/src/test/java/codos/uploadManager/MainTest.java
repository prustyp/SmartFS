package codos.uploadManager;

import static org.junit.Assert.*;

import codos.recentFileManager.RecentFileInfo;
import org.junit.Test;

public class MainTest {
  @Test
  public void uploadDecisionTest()
  {

    //Main main=new Main();
    //main.shouldUpload(new RecentFileInfo("UPLOADED","1541978952339",false) );
    assertFalse(Main.shouldUpload
        (new RecentFileInfo("UPLOADED","1541978952339",false)));
    assertTrue(Main.shouldUpload
        (new RecentFileInfo("DOWNLOADED","1541978952339",false)));
    assertFalse(Main.shouldUpload
        (new RecentFileInfo("DOWNLOADED","1542156410000",false)));
    assertFalse(Main.shouldUpload
        (new RecentFileInfo("DOWNLOADED","1541978952339",true)));
    assertFalse(Main.shouldUpload
        (new RecentFileInfo("UPLOADING","1542156410000",true)));
    assertFalse(Main.shouldUpload
        (new RecentFileInfo("DOWNLOADING","1542156410000",true)));


  }


}