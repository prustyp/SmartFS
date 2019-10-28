package codos.metadataManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

public class FileStatusReader {

  public Pair<String, String> getStatusForFile(String filePath) {
    String status = "NO_STATUS";
    String timestamp = "NO_TIMESTAMP";
    try {
      Class.forName("org.sqlite.JDBC");
      //String path= "/Users/Desktop/database1/student.db1"
      String url = "jdbc:sqlite:" + MetadataConstants.FILE_STATUS_DB_PATH;
      Connection sqlConnection = DriverManager.getConnection(url);

      String queryString = "SELECT * FROM File_Status WHERE FileName='" + filePath + "';";
      Statement sqlStatement = sqlConnection.createStatement();
      //PreparedStatement preparedStatement = sqlConnection.prepareStatement(queryString);
      //preparedStatement.setString(1, filePath);
      ResultSet rs = sqlStatement.executeQuery(queryString);

      while (rs.next()) {
        status = rs.getString(2);
        timestamp = rs.getString(3);

      }

      rs.close();
      sqlStatement.close();
      sqlConnection.close();


    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return new Pair<>(status, timestamp);
  }

  public List<String> getListOfDownloadedFiles() {
    ArrayList<String> downloadedFilePathList = new ArrayList<>();
    try {
      Class.forName("org.sqlite.JDBC");
      //String path= "/Users/Desktop/database1/student.db1"
      String url = "jdbc:sqlite:" + MetadataConstants.FILE_STATUS_DB_PATH;
      Connection sqlConnection = DriverManager.getConnection(url);
      //System.out.println("Open database successfully");

      String queryString = "SELECT * FROM File_Status WHERE Status='DOWNLOADED';";
      Statement sqlStatement = sqlConnection.createStatement();
      ResultSet rs = sqlStatement.executeQuery(queryString);

      while (rs.next()) {
        String downloadedfilePath = rs.getString(1);
        downloadedFilePathList.add(downloadedfilePath);

      }

      rs.close();
      sqlStatement.close();
      sqlConnection.close();
      //System.out.println("Closed database successfully");

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return downloadedFilePathList;
  }

  public List<Pair<String, String>> getListFileStatusInCloud() {
    ArrayList<Pair<String, String>> listOfFileStatusInDB = new ArrayList<>();
    try {
      Class.forName("org.sqlite.JDBC");
      //String path= "/Users/Desktop/database1/student.db1"
      String url = "jdbc:sqlite:" + MetadataConstants.FILE_STATUS_DB_PATH;
      Connection sqlConnection = DriverManager.getConnection(url);

      String queryString = "SELECT * FROM File_Status;";
      Statement sqlStatement = sqlConnection.createStatement();
      //PreparedStatement preparedStatement = sqlConnection.prepareStatement(queryString);
      //preparedStatement.setString(1, filePath);
      ResultSet rs = sqlStatement.executeQuery(queryString);

      while (rs.next()) {
        String fileName = rs.getString(1);
        String status = rs.getString(2);
        listOfFileStatusInDB.add(new Pair<>(fileName, status));

      }

      rs.close();
      sqlStatement.close();
      sqlConnection.close();


    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return listOfFileStatusInDB;
  }
}


