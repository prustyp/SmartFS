package quickstart;

import java.io.File;
import java.io.FileFilter;

public class FilefilterClass implements FileFilter {


    private final String[] fileExt = new String[] {"txt", "mp3", "mp4"};

    @Override
    public boolean accept(File file) {
        for (String extension : fileExt)
        {
            if (file.getName().toLowerCase().endsWith(extension))
            {

                return true;
            }
        }
        return false;
    }
}
