package com.JBibtexParser.verification;

import com.JBibtexParser.bibliography.Bibliography;
import com.JBibtexParser.entry.IEntry;
import com.JBibtexParser.entry.entries.PublicationEntry;
import com.JBibtexParser.typemanager.EntryField;
import com.JBibtexParser.typemanager.IEntryTypesManager;
import com.JBibtexParser.typemanager.definitions.FieldProperties;
import com.JBibtexParser.typemanager.definitions.IDefinition;

import java.util.Map;

/**
 * A simple implementation that verifies entries based on provided {@link IDefinition}
 */
public class SimpleVerifier implements IVerifier {
    private IVerificationReport verificationReport;
    private IDefinition verificationDefinition;
    public VerificationMode verificationMode;

    public SimpleVerifier(IDefinition verificationDefinition, VerificationMode verificationMode) {
        this.verificationDefinition = verificationDefinition;
        verificationReport = new SimpleVerificationReport();
        this.verificationMode = verificationMode;
    }

    public SimpleVerifier(IDefinition verificationDefinition, VerificationMode verificationMode, IVerificationReport verificationReport) {
        this.verificationDefinition = verificationDefinition;
        this.verificationReport = verificationReport;
        this.verificationMode = verificationMode;
    }

    public void verifyEntry(IEntry entry) {
        if ((entry instanceof PublicationEntry)) {
            //check if all fields in entry are allowed in definitions
            PublicationEntry publicationEntry = (PublicationEntry) entry;
            for (IEntryTypesManager.IEntryField field : publicationEntry.getFields().keySet()) {
                FieldProperties fieldProperties;
                if (verificationDefinition.getDefinition().containsKey(publicationEntry.getEntryType())) {

                    fieldProperties = verificationDefinition.getDefinition().get(publicationEntry.getEntryType()).get(field);
                } else {
                    fieldProperties = FieldProperties.NONE;
                }
                    if (fieldProperties == FieldProperties.NONE && (verificationMode == VerificationMode.FULL_VERIFY || verificationMode == VerificationMode.VERIFY_ONLY_ALLOWED)) {
                    verificationReport.addIssue(new Issue("Field is not allowed: ", entry, field));
                }
            }
            //check if all entries in definiton are present
            Map<IEntryTypesManager.IEntryField, FieldProperties> entryType = verificationDefinition.getDefinition().get(publicationEntry.getEntryType());
            if(entryType==null){
                verificationReport.addIssue(new Issue("Entry type is not defined: ", entry, new EntryField("")));
            }else {
                for (IEntryTypesManager.IEntryField field : entryType.keySet()) {
                    boolean exists = publicationEntry.getFields().containsKey(field);
                    if (entryType.get(field) == FieldProperties.REQUIRED && !exists && (verificationMode == VerificationMode.FULL_VERIFY || verificationMode == VerificationMode.VERIFY_ONLY_REQUIRED)) {
                        verificationReport.addIssue(new Issue("Field is required and missing: ", entry, field));
                    }
                }
            }
        }
    }
    @Override
    public void verifyBibliography(Bibliography bibliography) {
        for (PublicationEntry publicationEntry : bibliography.getEntries()) {
            verifyEntry(publicationEntry);
        }
    }

    @Override
    public IDefinition getVerificationDefinition() {
        return verificationDefinition;
    }

    @Override
    public IVerificationReport getVerificationReport() {
        return verificationReport;
    }
}
