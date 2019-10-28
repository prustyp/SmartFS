package codos.cronJobManager;

import codos.DriveListenerManager;
import codos.remoteDeleteManager.RemoteDeleteLargeFile;
import java.util.List;

/**
 * Hello world!
 *
 */
public class Main
{
    public static void main( String[] args ) throws Exception
    {

        //input

      //call drive listener
      DriveListenerManager driveListenerManager= new DriveListenerManager(args[0]);
      List<String> listOfFilePaths=driveListenerManager.getListOfFilePaths();


      //call main in uploadmanager
      listOfFilePaths.stream().forEach(
          path -> {
            try {
              codos.uploadManager.Main.main(new String[]{path,"10"});
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
      );


      //Call remote-delete-manager
      RemoteDeleteLargeFile remoteDeleteLargeFile = new RemoteDeleteLargeFile();


    }
}
