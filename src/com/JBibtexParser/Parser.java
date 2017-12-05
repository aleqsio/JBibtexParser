package com.JBibtexParser;

import com.JBibtexParser.bibliography.IBibliographyManager;
import com.JBibtexParser.bibliography.KeywordBibliographyManager;
import com.JBibtexParser.entry.EntryFactory;
import com.JBibtexParser.entry.IEntry;
import com.JBibtexParser.entry.entries.CommentEntry;
import com.JBibtexParser.entry.entries.PreambleEntry;
import com.JBibtexParser.entry.entries.PublicationEntry;
import com.JBibtexParser.entry.entries.StringEntry;
import com.JBibtexParser.fieldparser.IFieldParser;
import com.JBibtexParser.fieldparser.SimpleFieldParser;
import com.JBibtexParser.reader.FileReader;
import com.JBibtexParser.reader.IBlocksProvider;
import com.JBibtexParser.typemanager.DynamicTypesManager;
import com.JBibtexParser.typemanager.IEntryTypesManager;
import com.JBibtexParser.typemanager.StaticTypesManager;
import com.JBibtexParser.typemanager.definitions.BibtexDefinition;
import com.JBibtexParser.util.exceptions.ParseErrorException;

import java.util.LinkedList;
import java.util.List;

/**
 * Main class handling parsing bibtex files
 * On default it uses {@link DynamicTypesManager}, {@link FileReader}, {@link KeywordBibliographyManager},{@link SimpleFieldParser},
 * but you can change any of the components to your implementation by using provided getters and setters.
 */
public class Parser {

    public IBlocksProvider getBlocksProvider() {
        return blocksProvider;
    }

    public void setBlocksProvider(IBlocksProvider blocksProvider) {
        this.blocksProvider = blocksProvider;
    }

    public IBibliographyManager getBibliographyManager() {
        return bibliographyManager;
    }

    public void setBibliographyManager(IBibliographyManager bibliographyManager) {
        this.bibliographyManager = bibliographyManager;
    }

    public IEntryTypesManager getEntryTypesManager() {
        return entryTypesManager;
    }

    public void setEntryTypesManager(IEntryTypesManager entryTypesManager) {
        this.entryTypesManager = entryTypesManager;
    }


    public IFieldParser getFieldParser() {
        return fieldParser;
    }

    public void setFieldParser(IFieldParser fieldParser) {
        this.fieldParser = fieldParser;
    }

    private IBlocksProvider blocksProvider;
    private IBibliographyManager bibliographyManager;
    private IEntryTypesManager entryTypesManager;
    private IFieldParser fieldParser;

    public List<StringEntry> getStringEntries() {
        return stringEntries;
    }

    public List<CommentEntry> getCommentEntries() {
        return commentEntries;
    }

    public List<PreambleEntry> getPreambleEntries() {
        return preambleEntries;
    }

    public List<PublicationEntry> getPublicationEntries() {
        return publicationEntries;
    }

    /**
     * @return A list of all entries in the following order: preamble,string,comment,publication
     */
    public List<IEntry> getAllEntries(){
        List<IEntry> entries =new LinkedList<>();
        entries.addAll(preambleEntries);
        entries.addAll(stringEntries);
        entries.addAll(commentEntries);
        entries.addAll(publicationEntries);
        return entries;
    }

    private List<StringEntry> stringEntries=new LinkedList<>();
    private List<CommentEntry> commentEntries =new LinkedList<>();
    private List<PreambleEntry> preambleEntries=new LinkedList<>();
    private List<PublicationEntry> publicationEntries=new LinkedList<>();
    private EntryFactory entryFactory;

    /**
     * @param filepath - filepath for the file to be parsed, passed to {@link FileReader}.
     */
    public Parser(String filepath) {
        //default blocksProvider parameters;
        FileReader.getInstance().setFilepath(filepath);
        initialiseDefaultValues();
    }
    public Parser(IBlocksProvider blocksProvider) {
        this.blocksProvider = blocksProvider;
        initialiseDefaultValues();
    }

    public Parser(IBibliographyManager bibliographyManager) {
        this.bibliographyManager = bibliographyManager;
        initialiseDefaultValues();
    }
    public Parser(IBlocksProvider blocksProvider, IBibliographyManager bibliographyManager) {
        this.blocksProvider = blocksProvider;
        this.bibliographyManager = bibliographyManager;
        initialiseDefaultValues();
    }
    private void initialiseDefaultValues(){
        if(blocksProvider==null) blocksProvider = FileReader.getInstance();
        if(fieldParser==null) fieldParser = new SimpleFieldParser();
        if(bibliographyManager==null) bibliographyManager = new KeywordBibliographyManager();
        if(entryTypesManager==null) entryTypesManager = new DynamicTypesManager();
    }

    /**
     * Main method to parse all entries from chosen {@link IBlocksProvider}.
     * @return A chosen {@link IBibliographyManager} with all parsed entries.
     * @throws ParseErrorException on a parse exception resulting from malformed bibtex file.
     */
    public IBibliographyManager parse() throws ParseErrorException {
        entryFactory=new EntryFactory(stringEntries, commentEntries, entryTypesManager,fieldParser);
        if(entryTypesManager ==null) entryTypesManager =new StaticTypesManager(new BibtexDefinition());
        if(fieldParser==null) fieldParser=new SimpleFieldParser();
        blocksProvider.openProvider();
        while (blocksProvider.hasNextEntry()) {
            parseBlock(blocksProvider.nextEntry());
        }
        blocksProvider.closeProvider();
        return bibliographyManager;
    }


    /**
     *
     * @param textblock A single method to be parsed, if there is no need to use {@link IBlocksProvider}
     * @throws ParseErrorException on a parse exception resulting from malformed bibtex block.
     */
    public void parseBlock(String textblock) throws ParseErrorException {
        IEntry newEntry = entryFactory.getEntry(textblock);
        if(newEntry instanceof StringEntry){
            stringEntries.add((StringEntry) newEntry);
        }
        if(newEntry instanceof CommentEntry){
           commentEntries.add((CommentEntry) newEntry);
        }
        if(newEntry instanceof PreambleEntry){
            preambleEntries.add((PreambleEntry) newEntry);
        }
        if(newEntry instanceof PublicationEntry){
            publicationEntries.add((PublicationEntry) newEntry);
            bibliographyManager.add((PublicationEntry) newEntry);
        }
    }



}