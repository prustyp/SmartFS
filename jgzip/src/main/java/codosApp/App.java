package codosApp;

import codos.recentFileManager.RecentFileInfo;
import codos.recentFileManager.RecentFileManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/**
 * Hello world!
 */
public class App {

  public static void main(String[] args) {

    String inputFile = args[0];
    String outputFile = args[0] + ".gzip";
    long startTime = System.currentTimeMillis();
    compressGzipFile(inputFile, outputFile);
    long endTime = System.currentTimeMillis();
    long totalTimeInSec = (endTime - startTime) / 1000;
    System.out.println("JGZIP File : " + inputFile + " Total Time: " + totalTimeInSec);

  }


  private static void compressGzipFile(String inputFile, String gzipFile)  {
    try {

      //Check if file exist

      File file=new File(inputFile);
      if(!file.exists())
      {
        RecentFileManager recentFileManager=new RecentFileManager(inputFile);
        RecentFileInfo recentFileInfo=recentFileManager.getRecentFileInfo();
        String status=recentFileInfo.status;
        if(status.equals("NO_STATUS"))
        {
          System.out.println("File " + inputFile + " doesnot exist in local or remote.");
          System.exit(0);
        }
        else
        {
          codos.downloadManager.Main.main(new String[]{inputFile,"10"});
        }
        while(!recentFileManager.getRecentFileInfo().status.equals("DOWNLOADED"))
        {
          Thread.sleep(1000);
          System.out.println("sleeping");
        }
      }


      FileInputStream fis = new FileInputStream(file);
      FileOutputStream fos = new FileOutputStream(gzipFile);
      GZIPOutputStream gzipOS = new GZIPOutputStream(fos);
      byte[] buffer = new byte[1024];
      int len;
      while ((len = fis.read(buffer)) != -1) {
        gzipOS.write(buffer, 0, len);
      }
      //close resources
      gzipOS.close();
      fos.close();
      fis.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
