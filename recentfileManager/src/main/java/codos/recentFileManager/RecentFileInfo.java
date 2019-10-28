package codos.recentFileManager;

public class RecentFileInfo {
  /**
   * it will have three things
   * 1-status
   * 2-timestamp
   * 3-file is open or not
   */
  public String status;
  public String timestamp;
  public boolean isFileOpen;
  public RecentFileInfo(String status, String timestamp, boolean isFileOpen){
    this.status=status;
    this.timestamp=timestamp;
    this.isFileOpen=isFileOpen;
  }

}
