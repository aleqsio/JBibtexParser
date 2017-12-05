package com.JBibtexParser.reader;

import com.JBibtexParser.util.exceptions.BlockProviderException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Reads blocks from a single file
 * Only one instance exists at a time.
 */
public class FileReader implements IBlocksProvider{

    private static FileReader instance = null;
    private String filepath;
    private BufferedReader br;
    private String appendToNextEntry="";

    /**
     * @param filepath A path to the file to be read
     */
    public void setFilepath(String filepath){
        this.filepath=filepath;
    }
    public static FileReader getInstance() {
        if (instance == null) {
            instance = new FileReader();
        }
        return instance;
    }
    /**
     * Opens the file
     *
     * @throws BlockProviderException  if a file does not exist or can not be read
     */

    @Override
    public void openProvider() throws BlockProviderException {

        try {
            if(filepath==null || filepath.length()==0) throw new FileNotFoundException("filepath can not be empty");
                br = new BufferedReader(new java.io.FileReader(filepath));
        } catch (FileNotFoundException e) {
            throw new BlockProviderException("file does not exist: " + e.getMessage());
        }
    }
    /**
     * Provides a block containing one bibtex entry from file
     *
     * @return A block containing next entry
     * @throws BlockProviderException on inner IOException
     */

    public String nextEntry() throws BlockProviderException {
        StringBuilder sb = new StringBuilder();
        sb.append(appendToNextEntry);
        appendToNextEntry="";

            int bracketCounter=0;
            boolean currentEntry=true;
        try {
            while (br.ready() && currentEntry) {

                char r = (char) br.read();
                if (r == '{') {
                    bracketCounter++;
                }
                if (r == '}') {
                    bracketCounter--;
                    if(bracketCounter==0)
                    {
                        currentEntry=false;
                    }
                }
                if (r == '@') {
                    currentEntry=false;
                    appendToNextEntry="@";
                }else {
                    sb.append(r);
                }
            }
        } catch (IOException e) {
            throw new BlockProviderException("Failed to open File " + e.getMessage());
        }

        return sb.toString();
    }
    /**
     * @return A value indicating if there are any more entries in the opened file.
     * @throws BlockProviderException on exception while reading blocks
     */
    public boolean hasNextEntry() throws BlockProviderException {
        try {
            return br.ready();
        } catch (IOException e) {
            throw new BlockProviderException("Failed to read some entries from file" + e.getMessage());
        }
    }

    @Override
    public void closeProvider() {
        try {
            br.close();
        } catch (IOException e) {
            //assume file to be closed
        }
    }

}