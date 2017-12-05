package com.JBibtexParser.verification;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A result of running {@link SimpleVerifier}. Contains descriptions of detected issues with the bibtex file.
 */

public class SimpleVerificationReport implements IVerificationReport {
    List<Issue> issues=new LinkedList<>();
    @Override
    public void addIssue(Issue issue) {
        issues.add(issue);
    }

    @Override
    public String toString() {
        return issues.stream().map(p-> p.getDescription()+p.getField().getName()+" in "+(p.getEntry().getName())).collect(Collectors.joining(",\n"));
    }
    @Override
    public boolean isSuccessful() {
        return issues.size()==0;
    }
}
