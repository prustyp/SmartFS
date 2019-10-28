package codos.recentFileManager;

import org.junit.Test;

public class RecentFileManagerTest {
  @Test
  public void recentFileManagerTest()
  {
      RecentFileManager recentFileManager=
        new RecentFileManager("/Users/pprusty05/osf18exp/TestFolder/a.txt.250");
    recentFileManager.getRecentFileInfo();
  }

}
