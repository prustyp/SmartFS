package codos;

public class BlueSkyMain {

  public static void main(String[] args) throws Exception {
    long fileSize = Long.parseLong(args[0]);
    String filePath = args[1];
    long numberOfChunks = fileSize /4;
    BlueSkyHelper blueSkyHelper = new BlueSkyHelper();
    long startTime = System.currentTimeMillis();

    for(int i = 0; i< numberOfChunks;i ++) {
      blueSkyHelper.uploadFile(filePath);
    }
    long endTime = System.currentTimeMillis();
    long timeForUpload = endTime - startTime;

    startTime = System.currentTimeMillis();

    for(int i = 0; i< numberOfChunks;i ++) {
      blueSkyHelper.uploadFile(filePath);
    }
    endTime = System.currentTimeMillis();
    long timeForDownload = endTime - startTime;
    System.out.println(timeForUpload + " " + timeForDownload);
  }

}
