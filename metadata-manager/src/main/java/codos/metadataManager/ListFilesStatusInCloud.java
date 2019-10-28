package codos.metadataManager;


import java.util.List;
import javafx.util.Pair;

public class ListFilesStatusInCloud {

  String directoryPath;
  String fileName;

  public ListFilesStatusInCloud(String directoryPath) {
    this.directoryPath = directoryPath;

    FileStatusReader fileStatusReader = new FileStatusReader();
    List<Pair<String, String>> listOfFileStatusPair = fileStatusReader.getListFileStatusInCloud();

    for (int i = 0; i < listOfFileStatusPair.size(); i++) {
      if (listOfFileStatusPair.get(i).getKey().startsWith(directoryPath)) {
        System.out.println(
            listOfFileStatusPair.get(i).getKey() + " " + listOfFileStatusPair.get(i).getValue());
      }
    }
    //listOfFileStatusPair.stream().forEach(pairOfFileStatus -> );
  }

}
