package com.JBibtexParser.reader;

import com.JBibtexParser.util.exceptions.BlockProviderException;


/**
 * Handles providing blocks containing bibtex entries for the {@link com.JBibtexParser.Parser}
 */
public interface IBlocksProvider {
    /**
     * Opens the blocks stream (for example a file)
     *
     * @throws BlockProviderException when it is impossible to open block stream
     */
    void openProvider() throws BlockProviderException;

    /**
     * Provides a block containing one bibtex entry
     *
     * @return A block containing next entry
     * @throws BlockProviderException
     */
    String nextEntry() throws BlockProviderException;

    /**
     * @return A value indicating if a next entry is available
     * @throws BlockProviderException
     */
    boolean hasNextEntry() throws BlockProviderException;

    /**
     * Closes provider, handles releasing resources
     */
    void closeProvider();
}
