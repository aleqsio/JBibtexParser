package com.JBibtexParser.tests;

import com.JBibtexParser.reader.FileReader;
import com.JBibtexParser.util.exceptions.BlockProviderException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileReaderTest {
    @org.junit.jupiter.api.Test
    public void ShouldCorrectlyDivideIntoBlocks() throws BlockProviderException, IOException {
        FileReader fileReader = new FileReader();
        File tempFile = File.createTempFile("jbibtex-", "-test");
        String cnt = "@aaaaaaa{aaa05, aaaaaa = \"aa. a\", aaaaa = {aaaaaaaaa {}aaaaa}, aaaaaaaaa = \"aaa\" # \"aaa\", aaaa = 2005, } @aaaaaaa{aaa06, aaaaaa = \"aa. a aaa aaaaaa aaaa\", aaaaa = {aaaaaaaaa {}aaaaa}, aaaaaaaaa = \"aaa\" # \"aaa\", aaaa = 2005, } @aaaa{aaa07, aaaaaa = \"aa. a\", aaaaa = {aaaaaaaaa {\\{}aaaaa}, aaaaaaaaa = \"aaa\" # \"aaa\", aaaa = 2005, }@aaaa{aaa07, aaaaaa = \"aa. a aaa aaaaaa aaaaa\", aaaaa = {aaaa aa aaaaaaaaa aaaaa}, aaaaaaaaa = \"aaa\" # \"aaa\", aaaa = 2005, }@aaaa{aaa07, aaaaaa = \"aa. a aaa aaaaa, aaaaaa\", aaaaa = {aaaa aa aaaaaaaaa aaaaa}, aaaaaaaaa = \"aaa\" # \"aaa\", aaaa = 2005, }@aaaaaaa(aaa05,aaaaaa = aaaaaaaaaa,aaaaa = aaaaaaaaa,aaaaaaaaa = aaaaaaaaaaaaa,aaaa = 2000)";
        PrintWriter writer = new PrintWriter(tempFile.getAbsolutePath(), "UTF-8");
        writer.println(cnt);
        writer.close();
        tempFile.deleteOnExit();
        fileReader.setFilepath(tempFile.getAbsolutePath());
        fileReader.openProvider();
        int x=0;
        while(fileReader.hasNextEntry()){
            x++;
            fileReader.nextEntry();
        }
        assertEquals(11,x);

    }
}
