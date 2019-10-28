package codos.metadataManager;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;


public class ChunksSqliteManager {

  public String dbFilePath;

  public ChunksSqliteManager(String dbFilePath) {
    this.dbFilePath = dbFilePath;
  }

  /**
   * Create Db and corresponding table
   */
  public void createDb() {

    try {

      //Delete existing DB file
      File file = new File(this.dbFilePath);
      if (file.exists()) {
        file.delete();
      }



      // Create a new db
      Class.forName("org.sqlite.JDBC");
      String url = "jdbc:sqlite:"
          + this.dbFilePath;              //create a sqlitedb in location this.dbFilePath
      Connection sqlConnection = DriverManager.getConnection(url);
      if (sqlConnection != null) {
        //System.out.println("A new database has been created.");
      } else {
        //System.out.println("Error creating database !!!");
      }



      //Database created now we will create a table
      String sqlStatement = "CREATE TABLE IF NOT EXISTS chunk_info (\n"
          + "	chunk_path TEXT,\n"
          + "	size INTEGER\n"
          + ");";

      Statement stmt = sqlConnection.createStatement();
      stmt.execute(sqlStatement);
      stmt.close();
      sqlConnection.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }



  /**
   * Write Recods from chunkDataList in to table "chunk_info"
   * @param chunkDataList: list of chunkpath and chuncksize
   */
  public void writeOnDB(List<Pair<String, Integer>> chunkDataList) {

    try {
      Class.forName("org.sqlite.JDBC");
      String url = "jdbc:sqlite:" + this.dbFilePath;
      Connection sqlConnection = DriverManager.getConnection(url);
      String sqlStmt = "INSERT INTO chunk_info (chunk_path,size) VALUES(?,?)";
      for (Pair<String, Integer> chunkData : chunkDataList) {

        PreparedStatement preparedStatement = sqlConnection.prepareStatement(sqlStmt);
        preparedStatement.setString(1, chunkData.getKey());
        preparedStatement.setInt(2, chunkData.getValue());
        preparedStatement.executeUpdate();
        preparedStatement.close();
      }
      sqlConnection.close();

    } catch (Exception ex) {
      ex.printStackTrace();
    }

  }



  /**
   * Read Recods of chunkDataList from table "chunk_info"
   * @return list of chunkpath and chuncksize
   */
  public List<Pair<String, Integer>> readFromDB() {
    ArrayList<Pair<String, Integer>> chunkPathList = new ArrayList<>();
    try {
      Class.forName("org.sqlite.JDBC");
      //String path= "/Users/Desktop/database1/student.db1"
      String url = "jdbc:sqlite:" + this.dbFilePath;
      Connection sqlConnection = DriverManager.getConnection(url);
      //System.out.println("Open database successfully");
      Statement sqlStatement = sqlConnection.createStatement();
      String queryString = "SELECT * FROM chunk_info;";
      ResultSet rs = sqlStatement.executeQuery(queryString);

      while (rs.next()) {
        String chunkPath = rs.getString(1);

        int chunkSize = rs.getInt(2);
        chunkPathList.add(new Pair<>(chunkPath, chunkSize));

      }

      rs.close();
      sqlStatement.close();
      sqlConnection.close();
      //System.out.println("Closed database successfully");

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return chunkPathList;
  }


}







