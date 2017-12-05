package com.JBibtexParser.tests;

import com.JBibtexParser.bibliography.KeywordBibliographyManager;
import com.JBibtexParser.entry.entries.PublicationEntry;
import com.JBibtexParser.typemanager.EntryField;
import com.JBibtexParser.typemanager.EntryType;
import com.JBibtexParser.typemanager.IEntryTypesManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Aleksander on 05.12.2017.
 */
public class KeywordBibliographyManagerTest {
    @org.junit.jupiter.api.Test
    public void ShouldCorrectlyAddEntries() {
        KeywordBibliographyManager manager = getManagerWithMockedEntry();
        IEntryTypesManager.IEntryType mockType = new EntryType("mock");
        assertEquals(1, manager.filterByType(mockType).getBibliography().size());
    }
    @org.junit.jupiter.api.Test
    public void ShouldFindByKeywords() {
        KeywordBibliographyManager manager = getManagerWithMockedEntry();
        assertEquals(1, manager.findEntriesContainingWords("mockValue2").getBibliography().size());
    }
    @org.junit.jupiter.api.Test
    public void ShouldFindByFieldValue() {
        IEntryTypesManager.IEntryField mockField = new EntryField("mock");
        KeywordBibliographyManager manager = getManagerWithMockedEntry();
        assertEquals(0, manager.findFieldsOfValue(mockField,".*nonexistentMock.*").getBibliography().size());
        assertEquals(1, manager.findFieldsOfValue(mockField,".*mock.*").getBibliography().size());
        assertEquals(0, manager.findFieldsOfValue(mockField,".*value2.*").getBibliography().size());
    }

    private KeywordBibliographyManager getManagerWithMockedEntry(){
        IEntryTypesManager.IEntryType mockType = new EntryType("mock");
        IEntryTypesManager.IEntryField mockField = new EntryField("mock");
        PublicationEntry mockEntry = new PublicationEntry(mockType);

        mockEntry.setFields(getMockKeyMap());
        KeywordBibliographyManager manager = new KeywordBibliographyManager();
        manager.add(mockEntry);
        return manager;
    }

    private Map<IEntryTypesManager.IEntryField, List<String>> getMockKeyMap() {
        String str = "mock:mockValue,mock2:mockValue2";
        Map<IEntryTypesManager.IEntryField, List<String>> myMap = new HashMap<>();
        String[] pairs = str.split(",");
        for (int i = 0; i < pairs.length; i++) {
            String pair = pairs[i];
            String[] keyValue = pair.split(":");
            ArrayList<String> value = new ArrayList<>();
            value.add(keyValue[1]);
            myMap.put(new EntryField(keyValue[0]), value);
        }
        return myMap;
    }
}
