package codos.metadataManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class FileStatusDeleter {

  public static void DeleteFileStatusFromDB(String filePath) {

    try {
      Class.forName("org.sqlite.JDBC");
      String url = "jdbc:sqlite:" + MetadataConstants.FILE_STATUS_DB_PATH;
      Connection sqlConnection = DriverManager.getConnection(url);

      String sql = "DELETE FROM File_Status WHERE FileName = '" + filePath + "';";
      Statement sqlStatement = sqlConnection.createStatement();

      sqlStatement.execute(sql);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }
}
