package codos.downloadManager;


import java.util.ArrayList;
import org.junit.Test;

public class FileMergerTest {

  @Test
  public void testFileMerge() throws Exception {
    ArrayList<String> filePath = new ArrayList<>();
    filePath.add("C:\\Users\\ADMIN\\Desktop\\SmartSysPro\\sb2.txt");
    filePath.add("C:\\Users\\ADMIN\\Desktop\\SmartSysPro\\sb3.txt");
    filePath.add("C:\\Users\\ADMIN\\Desktop\\SmartSysPro\\sb4.txt");
    String destinationPath = "C:\\Users\\ADMIN\\Desktop\\SmartSysPro\\Merged.docx";
    //FileMerger fileMerger= new FileMerger(filePath,destinationPath);
    //  fileMerger.mergeFiles();

  }

}


