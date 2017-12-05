package com.JBibtexParser.bibliography;

import com.JBibtexParser.entry.entries.PublicationEntry;
import com.JBibtexParser.typemanager.IEntryTypesManager;

public interface IBibliographyManager{
    void add(PublicationEntry PublicationEntry);
    IBibliographyManager findFieldsOfValue(IEntryTypesManager.IEntryField field, String regex);
    IBibliographyManager filterByType(IEntryTypesManager.IEntryType type);
    Bibliography getBibliography();
    IBibliographyManager findEntriesContainingWords(String query);
}
