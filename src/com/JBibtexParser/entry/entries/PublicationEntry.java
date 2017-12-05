package com.JBibtexParser.entry.entries;

import com.JBibtexParser.entry.IEntry;
import com.JBibtexParser.typemanager.IEntryTypesManager;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Main entry in use by the parser - represents a single publication with an id, it's fields and it's values
 */
public class PublicationEntry implements IEntry {

	private IEntryTypesManager.IEntryType entryType;

	/**
	 * @return Entry's identifier - a value without a key that is used by bibtex to reference entries
	 */
	public String getEntryName() {
		return entryName;
	}

	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}

	private String entryName;
	private Map<IEntryTypesManager.IEntryField, List<String>> fields;

	public PublicationEntry(IEntryTypesManager.IEntryType entryType) {
		this.entryType = entryType;
	}

	public Map<IEntryTypesManager.IEntryField, List<String>> getFields() {
		return fields;
	}

	public void setFields(Map<IEntryTypesManager.IEntryField, List<String>> fields) {
		this.fields = fields;
	}

	public IEntryTypesManager.IEntryType getEntryType() {
		return entryType;
	}

	public void setEntryType(IEntryTypesManager.IEntryType entryType) {
		this.entryType = entryType;
	}

	public String getName() {
		return this.getEntryType().getName() + " " + this.getEntryName();
	}

	@Override
	public String toString() {
		return entryType.getName() + " " + entryName+"\n" + fields.keySet().stream().map(p -> 	"\t"+p.getName() + ":" +
				"\t"+((fields.get(p).size() == 1) ? fields.get(p).get(0) :"•"+fields.get(p).stream().collect(Collectors.joining("\n\t\t\t•")))).collect(Collectors.joining("\n"));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PublicationEntry that = (PublicationEntry) o;

		if (entryType != null ? !entryType.equals(that.entryType) : that.entryType != null) return false;
		if (entryName != null ? !entryName.equals(that.entryName) : that.entryName != null) return false;
		return fields != null ? fields.equals(that.fields) : that.fields == null;
	}

	@Override
	public int hashCode() {
		int result = entryType != null ? entryType.hashCode() : 0;
		result = 31 * result + (entryName != null ? entryName.hashCode() : 0);
		result = 31 * result + (fields != null ? fields.hashCode() : 0);
		return result;
	}
}