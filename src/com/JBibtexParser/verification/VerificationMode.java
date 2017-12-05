package com.JBibtexParser.verification;

/**
 * An enum determining chosen verification mode
 */
public enum VerificationMode{
    /**
     * No issues are reported
     */
    NO_VERIFICATION,
    /**
     * Only required fields are reported if they are missing.
     * Other entries are ignored, but not removed from bibliography
     * If needed you can verify with VERIFY_ONLY_ALLOWED and drop them from bibliography
     * Normal Bibtex mode
     */
    VERIFY_ONLY_REQUIRED,
    /**
     * Only existing but not allowed fields are reported
     */
    VERIFY_ONLY_ALLOWED,

    /**
     * All issues are reported
     */
    FULL_VERIFY
}
