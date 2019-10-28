package com.metaDataManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.DatabaseMetaData;
import java.sql.Statement;
public class Connector {
    public Connection connect(String databaseName){
        Connection conn = null;
        try{                                       //Change Path according to OS File System.
            String url = "jdbc:sqlite:C:/Users/Rohan/Desktop/SQLite/" + databaseName + ".db";
            conn = DriverManager.getConnection(url);                            //establish connection to database
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public void createDatabase(String filename){
        Connection con = null;
        try{
            con = this.connect(filename);
            if (con != null) {                                                  // create database;
                DatabaseMetaData meta = con.getMetaData();
                //System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("log: A new database has been created.");
            }
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }finally {
            try{
                if(con!=null){
                    con.close();
                }
            }catch(SQLException ex){
                System.out.println(ex.getMessage());
            }
        }
    }

    public void createNewTable(String fileHashName, String filename) {
        Connection con = null;
                                                                            // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS " +filename+ "(\n"
                + "	fileName text NOT NULL,\n"
                + "	filePath text NOT NULL,\n"
                + "	splitChunk number\n"
                + ");";

        try {
            con = this.connect(filename);
                                                                                // create a new table
            Statement stmt = con.createStatement();
            stmt.execute(sql);
            System.out.println("log: Table has been created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try{
                if(con!=null){
                    con.close();
                }
            }catch(SQLException ex){
                System.out.println(ex.getMessage());
            }
        }
    }

    public void insert(){
        /* write an insert function to insert values into to the table. */
    }
}
