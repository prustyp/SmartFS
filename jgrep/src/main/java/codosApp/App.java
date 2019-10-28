package codosApp;

import codos.recentFileManager.RecentFileInfo;
import codos.recentFileManager.RecentFileManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class App {

  public static void main(String args[]) {
    if (args.length != 2) {
      System.err.println("Usage: Grep pattern file");
      System.exit(1);
    }

    Pattern cre = null;        // Compiled RE
    try {
      cre = Pattern.compile(args[0]);
    } catch (PatternSyntaxException e) {
      System.err.println("Invalid RE syntax: " + e.getDescription());
      System.exit(1);
    }

    long startTime = System.currentTimeMillis();
    BufferedReader in = null;
    try {
      //Need to change here -- can't read from local
      String filePath = args[1];
      File file = new File(filePath);
      if (!file.exists()) {
        RecentFileManager recentFileManager = new RecentFileManager(filePath);
        RecentFileInfo recentFileInfo = recentFileManager.getRecentFileInfo();
        String status = recentFileInfo.status;
        if (status.equals("NO_STATUS")) {
          System.out.println("File " + filePath  + " doesnot exist in local or remote.");
          System.exit(0);
        } else {
          codos.downloadManager.Main.main(new String[]{filePath, "10"});
        }
        while (!recentFileManager.getRecentFileInfo().status.equals("DOWNLOADED")) {
          Thread.sleep(1000);
          System.out.println("sleeping");
        }
      }

      in = new BufferedReader(new InputStreamReader(
          new FileInputStream(args[1])));
    } catch (Exception e) {
      System.err.println("Unable to open file " +
          args[1] + ": " + e.getMessage());
      System.exit(1);
    }

    try {
      String s;
      while ((s = in.readLine()) != null) {
        Matcher m = cre.matcher(s);
        if (m.find()) {
          System.out.println(s);
        }
      }
    } catch (Exception e) {
      System.err.println("Error reading line: " + e.getMessage());
      System.exit(1);
    }

    long endTime = System.currentTimeMillis();
    long totalTimeInSec = (endTime - startTime) / 1000;
    System.out.println("JGREP File : " + args[1] + " Total Time: " + totalTimeInSec);
  }
}
