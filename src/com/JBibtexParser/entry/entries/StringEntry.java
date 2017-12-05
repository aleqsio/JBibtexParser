package com.JBibtexParser.entry.entries;

import com.JBibtexParser.entry.IEntry;

public class StringEntry implements IEntry {

	private String sourceText;
	private String replacementText;

	public StringEntry(String key, String value) {
		sourceText=key;
		replacementText=value;

	}

	public String getSourceText() {
		return sourceText;
	}

	public void setSourceText(String sourceText) {
		this.sourceText = sourceText;
	}

	public String getReplacementText() {
		return replacementText;
	}

	public void setReplacementText(String replacementText) {
		this.replacementText = replacementText;
	}

	@Override
	public String toString() {
		return "String substitution: " +	sourceText +" -> " + replacementText;
	}

	@Override
	public String getName() {
		return toString();
	}
}