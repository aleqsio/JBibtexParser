package com.JBibtexParser.fieldparser;

import com.JBibtexParser.entry.entries.StringEntry;
import com.JBibtexParser.typemanager.EntryField;
import com.JBibtexParser.util.LeveledString;
import com.JBibtexParser.util.exceptions.ParseErrorException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Aleksander on 27.11.2017.
 */
public class SimpleFieldParser implements IFieldParser {
    /**
     * Parses a field, handles string substitution and dividing authors
     * @param fieldString - field's contents
     * @param stringEntries - a list of string entries used for substitutions
     * @param field         - field type from either {@link com.JBibtexParser.typemanager.definitions.BibtexDefinition} or a custom definition implementing {@link com.JBibtexParser.typemanager.definitions.IDefinition} provided to the parser
     * @return
     * @throws ParseErrorException
     */
    @Override
    public List<String> parseField(LeveledString fieldString, List<StringEntry> stringEntries, EntryField field) throws ParseErrorException {
        fieldString = fieldString.trim();
        fieldString = performSubstitutions(fieldString, stringEntries, field);
        if (fieldString.length() == 0) return new LinkedList<>();
        List<String> subfields = new LinkedList<>();


        if (field.getName().equals("author")) {
            subfields.addAll(split(fieldString.getEntry(), "and").stream().map(this::reverseByCommas).collect(Collectors.toList()));
            return subfields;
        } else {
            subfields.add(fieldString.getEntry());
            return subfields;
        }
        //TODO: block above is special behaviour, to be replaced with this:
          /*
        subfields.addAll(split(leveledString.getEntry(), "and"));
        return subfields;
        */
    }

    //to be removed
    private String reverseByCommas(String entry) {
        ArrayList<String> arr = split(entry, ",");
        for (int i = 0; i < arr.size(); i++) {
           arr.set(i,arr.get(i).trim());
        }
        java.util.Collections.reverse(arr);
        return String.join(" ", arr).trim();
    }

    private ArrayList<String> split(String fieldString, String regex) {
        return new ArrayList<>(Arrays.asList(fieldString.split(regex)));
    }

    private LeveledString performSubstitutions(LeveledString leveledString, List<StringEntry> stringEntries, EntryField field) throws ParseErrorException {
        for (StringEntry stringEntry : stringEntries) {
            leveledString = leveledString.substituteOnLevel(0, new String[]{stringEntry.getSourceText()}, stringEntry.getReplacementText());
        }
        List<LeveledString> stringsToBeJoined = leveledString.splitOnLevel('#', 0);
        stringsToBeJoined = stringsToBeJoined.stream().map(this::removeUnnecessaryBracketsAndBraces).collect(Collectors.toList());
        if (stringsToBeJoined.size() > 1) {
            leveledString = new LeveledString();
            for (LeveledString partialString : stringsToBeJoined) {
                partialString = partialString.trim();
                leveledString = leveledString.join(partialString);
            }

            return removeUnnecessaryBracketsAndBraces(leveledString);
        }
        return removeUnnecessaryBracketsAndBraces(leveledString);
    }

    private LeveledString removeUnnecessaryBracketsAndBraces(LeveledString l) {
        if (l.getSubstringsOfLevel(1, (a, b) -> a - b).size() == 1) {
            l = l.getSubstringsOfLevel(1, (a, b) -> a - b).get(0);
        }
        return l;
    }
}
