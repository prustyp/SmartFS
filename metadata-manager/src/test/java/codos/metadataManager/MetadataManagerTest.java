package  codos.metadataManager;

import org.junit.Test;

public class MetadataManagerTest {
  @Test
  public void testMetadataBuilder() throws Exception
  {
    String souceFilePath="/Users/pprusty05/osf18exp/TestFolder/100MB.bin";
    MetadataManager metadataManager= new MetadataManager();
    metadataManager.metadataBuilder(souceFilePath);
    MetadataUploader metadataUploader=new MetadataUploader(metadataManager.getDbPath(souceFilePath));

  }

  @Test
  public void testMetadataReader()
  {
    MetadataManager metadataManager= new MetadataManager();
    metadataManager.getMetaDataForALargeFile("/Users/pprusty05/osf18exp/TestFolder/100MB.bin").stream()
        .forEach(System.out::println);

  }


}