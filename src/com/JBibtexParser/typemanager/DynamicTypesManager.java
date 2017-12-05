package com.JBibtexParser.typemanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Main manager to be used, can accept any field or type, can return a list of all document's types
 */
public class DynamicTypesManager implements IEntryTypesManager {
    Map<String, EntryType> types = new HashMap<>();
    Map<String, EntryField> fields = new HashMap<>();

    /**
     * @param name - string name of type
     * @return a new type of name
     */
    @Override
    public EntryType getType(String name) {
        if (!types.containsKey(name.toLowerCase())) types.put(name.toLowerCase(), new EntryType(name));
        return types.get(name.toLowerCase());
    }

    /**
     * @param name - type name
     * @return Always true
     */
    @Override
    public boolean hasType(String name) {
        return true;
    }

    /**
     * @return A list of all types used so far
     */
    @Override
    public List<EntryType> getAllTypes() {
        return new ArrayList<>(types.values());
    }

    /**
     * @return A list of all fields used so far
     */
    @Override
    public List<EntryField> getAllFields() {
        return new ArrayList<>(fields.values());
    }


    /**
     * @param name - string name of field
     * @return A new field of given name
     */
    @Override
    public EntryField getField(String name) {
        if (!fields.containsKey(name.toLowerCase())) fields.put(name.toLowerCase(), new EntryField(name));
        return fields.get(name.toLowerCase());
    }

    /**
     * @param name - field name
     * @return Always true
     */
    @Override
    public boolean hasField(String name) {
        return true;
    }


}
