package com.JBibtexParser.bibliography;

import com.JBibtexParser.entry.entries.PublicationEntry;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A set of bibliography items
 */
public class Bibliography {

	private List<PublicationEntry> entries =new LinkedList<>();

	public Bibliography(List<PublicationEntry> entries) {
		this.entries = entries;
	}

	public Bibliography() {
	}

	/**
	 * @return A list of publication entry items
	 */
	public List<PublicationEntry> getEntries() {
		return entries;
	}

	/**
	 * @return Count of how many entries are in Bibliography
	 */
	public int size(){
		return  entries.size();
	}
	public void setEntries(List<PublicationEntry> entries) {
		this.entries = entries;
	}

	public void add(PublicationEntry e){
		entries.add(e);
	}

	/**
	 * @return A string representation of this bibliography - a list of all entries and its fields and values
	 */
	public String toString() {
		return entries.stream().map(PublicationEntry::toString).collect(Collectors.joining("\n"));
	}

}