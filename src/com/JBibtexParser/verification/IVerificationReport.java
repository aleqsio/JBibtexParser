package com.JBibtexParser.verification;

public interface IVerificationReport {
    void addIssue(Issue issue);
    String toString();
    boolean isSuccessful();
}
