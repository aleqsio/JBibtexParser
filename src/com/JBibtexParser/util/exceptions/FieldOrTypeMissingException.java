package com.JBibtexParser.util.exceptions;

/**
 * An issue with missing fields or types in a static types manager.
 */
public class FieldOrTypeMissingException extends ParseErrorException{
    public FieldOrTypeMissingException(String message) {
        super(message);
    }
}
