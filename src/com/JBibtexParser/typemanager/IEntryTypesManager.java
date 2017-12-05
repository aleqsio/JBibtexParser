package com.JBibtexParser.typemanager;

import java.util.List;


/**
 * An interface for managing entry types (book, article,...) and fields (author,title,...)
 */
public interface IEntryTypesManager {
    /**
     * @param name - string name of type
     * @return A type object with a given name, if one exists or implementation allows for dynamic creation, throws a runtime error if no hasType verification is done, type doesn't exist and type creation is static
     */
    EntryType getType(String name);

    /**
     * @param name - type name
     * @return If type exists with given name
     */
    boolean hasType(String name);

    /**
     * @return A list of all types
     */
    List<EntryType> getAllTypes();

    /**
     * @return A list of all fields
     */
    List<EntryField> getAllFields();
    /**
     * @param name - string name of field
     * @return A field object with a given name, if one exists or implementation allows for dynamic creation, throws a runtime error if no hasField verification is done, field doesn't exist and field creation is static
     */
    EntryField getField(String name);
    /**
     * @param name - field name
     * @return If field exists with given name
     */
    boolean hasField(String name);

    interface IEntryField {
        String getName();
        boolean equals(Object other);
        int hashCode();
    }
    interface IEntryType {
        String getName();
        boolean equals(Object other);
        int hashCode();
    }
}

