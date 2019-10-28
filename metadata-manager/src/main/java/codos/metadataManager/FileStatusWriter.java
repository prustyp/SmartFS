package codos.metadataManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class FileStatusWriter {

  public String filePath;
  public String status;

  public FileStatusWriter(String filePath, String status) {
    this.filePath = filePath;
    this.status = status;
    this.createDb();
    this.writeOnDB();

  }

  public void createDb() {

    try {
      Class.forName("org.sqlite.JDBC");
      String url = "jdbc:sqlite:"
          + MetadataConstants.FILE_STATUS_DB_PATH;              //create a sqlitedb in location this.dbFilePath
      Connection sqlConnection = DriverManager.getConnection(url);
      if (sqlConnection != null) {
        //System.out.println("A new database has been created.");
      } else {
        //System.out.println("Error creating database !!!");
      }

      //Database created now we will create a table
      String sqlStatement = "CREATE TABLE IF NOT EXISTS File_Status (\n"
          + "	FileName TEXT,\n"
          + " Status TEXT,\n"
          + "	TimeStamp TEXT\n"
          + ");";

      Statement stmt = sqlConnection.createStatement();
      stmt.execute(sqlStatement);
      stmt.close();
      sqlConnection.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  public void writeOnDB() {

    try {
      Class.forName("org.sqlite.JDBC");
      String url = "jdbc:sqlite:" + MetadataConstants.FILE_STATUS_DB_PATH;
      Connection sqlConnection = DriverManager.getConnection(url);

      String sql = "DELETE FROM File_Status WHERE FileName = ?";
      PreparedStatement pstmt = sqlConnection.prepareStatement(sql);
      pstmt.setString(1, this.filePath);
      pstmt.executeUpdate();

      String sqlStmt = "INSERT INTO File_Status (FileName,Status,TimeStamp) VALUES(?,?,?)";
      {

        PreparedStatement preparedStatement = sqlConnection.prepareStatement(sqlStmt);
        preparedStatement.setString(1, this.filePath);
        preparedStatement.setString(2, this.status);
        preparedStatement.setString(3, "" + System.currentTimeMillis());
        preparedStatement.executeUpdate();
        preparedStatement.close();
      }
      sqlConnection.close();

    } catch (Exception ex) {
      ex.printStackTrace();
    }


  }


}
