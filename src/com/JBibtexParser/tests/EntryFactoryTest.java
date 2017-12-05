package com.JBibtexParser.tests;

import com.JBibtexParser.entry.EntryFactory;
import com.JBibtexParser.entry.IEntry;
import com.JBibtexParser.entry.entries.CommentEntry;
import com.JBibtexParser.entry.entries.PreambleEntry;
import com.JBibtexParser.entry.entries.PublicationEntry;
import com.JBibtexParser.entry.entries.StringEntry;
import com.JBibtexParser.fieldparser.IFieldParser;
import com.JBibtexParser.typemanager.EntryField;
import com.JBibtexParser.typemanager.EntryType;
import com.JBibtexParser.typemanager.IEntryTypesManager;
import com.JBibtexParser.util.exceptions.ParseErrorException;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Aleksander on 05.12.2017.
 */
public class EntryFactoryTest {
    @org.junit.jupiter.api.Test
    public void ShouldCorrectlyDistinguishPublicationEntry() throws ParseErrorException {
        EntryFactory mockEntryFactory = getMockedFactory();
        String block =   "@article{m0001, author = mockAuthor, Title = mockTitle,publisher = mockPublisher,YEAR = 2000 }";
        IEntry entry = mockEntryFactory.getEntry(block);
        assertEquals("name m0001", entry.getName());
        assertTrue(entry instanceof PublicationEntry);
        assertEquals("mockAuthor", ((PublicationEntry) entry).getFields().get(new EntryField("author")).get(0).trim());
    }
    @org.junit.jupiter.api.Test
    public void ShouldHandleMalformedPublicationEntry() throws ParseErrorException {
        EntryFactory mockEntryFactory = getMockedFactory();
        String block =   "@article{m0001, author = mockAuthor, Title=test=test,publisher = mockPublisher,YEAR = 2000 }";
        Executable closureContainingCodeToTest = () ->{    IEntry entry = mockEntryFactory.getEntry(block); };
        assertThrows(ParseErrorException.class, closureContainingCodeToTest);
    }

    @org.junit.jupiter.api.Test
    public void ShouldCorrectlyDistinguishCommentEntry() throws ParseErrorException {
        EntryFactory mockEntryFactory = getMockedFactory();
        String block =   "@comment{mockContents}";
        IEntry entry = mockEntryFactory.getEntry(block);
        assertTrue(entry instanceof CommentEntry);
     }
    @org.junit.jupiter.api.Test
    public void ShouldCorrectlyDistinguishPreambleEntry() throws ParseErrorException {
        EntryFactory mockEntryFactory = getMockedFactory();
        String block =   "@preamble{mockContents}";
        IEntry entry = mockEntryFactory.getEntry(block);
        assertTrue(entry instanceof PreambleEntry);
    }
    @org.junit.jupiter.api.Test
    public void ShouldCorrectlyDistinguishStringEntry() throws ParseErrorException {
        EntryFactory mockEntryFactory = getMockedFactory();
        String block =   "@string{mar=march}";
        IEntry entry = mockEntryFactory.getEntry(block);
        assertTrue(entry instanceof StringEntry);
        assertEquals("mar",((StringEntry) entry).getSourceText());
        assertEquals("march",((StringEntry) entry).getReplacementText());
    }
    @org.junit.jupiter.api.Test
    public void ShouldCorrectlyHandleStringMalformedEntry() throws ParseErrorException {
        EntryFactory mockEntryFactory = getMockedFactory();
        String block =   "@string{mar=ma=rch}";
                Executable closureContainingCodeToTest = () ->{    IEntry entry = mockEntryFactory.getEntry(block); };
                assertThrows(ParseErrorException.class, closureContainingCodeToTest);
    }
    @org.junit.jupiter.api.Test
    public void ShouldCorrectlyHandleStringMalformedEntry2() throws ParseErrorException {
        EntryFactory mockEntryFactory = getMockedFactory();
        String block =   "@string{=rch}";
        Executable closureContainingCodeToTest = () ->{    IEntry entry = mockEntryFactory.getEntry(block); };
        assertThrows(ParseErrorException.class, closureContainingCodeToTest);
    }
    @org.junit.jupiter.api.Test
    public void ShouldCorrectlyHandleStringMalformedEntry3() throws ParseErrorException {
        EntryFactory mockEntryFactory = getMockedFactory();
        String block =   "@string{rch}";
        Executable closureContainingCodeToTest = () ->{    IEntry entry = mockEntryFactory.getEntry(block); };
        assertThrows(ParseErrorException.class, closureContainingCodeToTest);
    }


    public EntryFactory getMockedFactory() {
        IEntryTypesManager mockEntryManager = new IEntryTypesManager() {
            @Override
            public EntryType getType(String name) {
                return new EntryType("name");
            }

            @Override
            public boolean hasType(String name) {
                return true;
            }

            @Override
            public List<EntryType> getAllTypes() {
                return null;
            }

            @Override
            public List<EntryField> getAllFields() {
                return null;
            }

            @Override
            public EntryField getField(String name) {
                return new EntryField(name);
            }

            @Override
            public boolean hasField(String name) {
                return true;
            }
        };
        IFieldParser mockFieldParser = (fieldString, stringEntries, field) -> {
            ArrayList<String> list = new ArrayList<>();
            list.add(fieldString.getEntry());
            return  list;
        };
        return new EntryFactory(new LinkedList<StringEntry>(), new LinkedList<CommentEntry>(),mockEntryManager,mockFieldParser);
    }
}
