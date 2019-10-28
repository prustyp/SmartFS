package codos;

import static org.junit.Assert.*;

import org.junit.Test;

public class SegmentMakerTest {
  @Test
  public void segmentTest()
  {
    SegmentMaker segmentMaker=new SegmentMaker();
    segmentMaker.compressTozipFile("/Users/pprusty05/osf18exp/TestFolder/30mbfile.txt",
        "/Users/pprusty05/osf18exp/TestFolder/30mbfile.txt"+"zip");

  }

}