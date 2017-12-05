package com.JBibtexParser.verification;

import com.JBibtexParser.bibliography.Bibliography;
import com.JBibtexParser.entry.IEntry;
import com.JBibtexParser.typemanager.definitions.IDefinition;

/**
 * Verification according to provided {@link IDefinition} and correct {@link VerificationMode}.
 * It generates a report of all missing and/or not allowed fields in generated bibliography or a single entry.
 */
public interface IVerifier {
    void verifyEntry(IEntry entry);
    void verifyBibliography(Bibliography bibliography);
    /**
     * @return A complete report of all issues
     */
    IVerificationReport getVerificationReport();
    IDefinition getVerificationDefinition();
}
