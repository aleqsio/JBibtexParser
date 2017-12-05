package com.JBibtexParser.util.exceptions;

/**
 * An issue with a used block provider
 */
public class BlockProviderException extends ParseErrorException{
    public BlockProviderException(String message) {
        super(message);
    }
}
