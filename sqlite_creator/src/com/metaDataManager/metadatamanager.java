package com.metaDataManager;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import java.io.File;

public class metadatamanager {

    public static void main(String args[]) {
        File f = new File("C:\\Users\\Rohan\\Documents\\abc.txt"); //hardcoded path. Change when implementing listeners;

        String file = f.getName();                              //filename will come from event listeners rightnow it is hardcoded;
        String filename = FilenameUtils.removeExtension(file);  //removing extension for table name;

        System.out.println("log: Filename "+filename);
        String fileHashName = DigestUtils.md5Hex(file);        //converts file name to md5 hash name;

        Connector conn = new Connector();
            conn.createDatabase(fileHashName);                  //database name will be md5hash of filename;
            conn.createNewTable(fileHashName, filename);        //table name will be filename without extension;
    }
}
