/*
package codos.downloadManager;
import org.junit.Test;
import java.io.File;
import java.util.ArrayList;
public class DownloadTaskTest {
    
    @Test
    public void  TestDownloadManagerFunctionality() throws Exception {
        ArrayList<String> filePath=new ArrayList<>();
        filePath.add("C:/Users/ADMIN/Desktop/SmartSysPro/1GB.zip");
        filePath.add("C:/Users/ADMIN/Desktop/SmartSysPro/sb003.txt");
        filePath.add("C:/Users/ADMIN/Desktop/SmartSysPro/sb004.txt");
        filePath.add("SampleBlob.txt");
        filePath.add("SampleBlob1.txt");
        filePath.add("SampleBlob2.txt");
        filePath.add("SampleBlob3.txt");
        filePath.add("SampleBlob4.txt");
        */
/*download task begins*//*

        //DownloadTask d=new DownloadTask(filePath);
      //  d.downloadChunk();
        String destinationPath ="C:\\Users\\ADMIN\\Desktop\\SmartSysPro\\Merger.docx";
        */
/*merging task*//*

        FileMerger fileMerger= new FileMerger(filePath,destinationPath);
        fileMerger.mergeFiles();
        System.out.println("successfully merged files");
        System.out.println("new merged file is->MergedNew19oct.docx");
        int count=0;
        */
/* starting time*//*

        long start = System.currentTimeMillis();
        for(int i=0;i<filePath.size();i++) {

            boolean file = new File(filePath.get(i)).delete();
            count++;
            if(file)
            {
                System.out.println("successfully deleted unmerged files count:->"+count);

            }
            else
                {
                System.out.println("error in deleting files!");

               }
        }
        */
/*end time*//*

        long end = System.currentTimeMillis();
        System.out.println("deleting takes" +"\n"+
                (end - start) + "ms");

        System.out.println("Download manager operations finish");
    }
}*/
