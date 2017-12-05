package com.JBibtexParser.typemanager;

public class EntryType implements IEntryTypesManager.IEntryType {
    String name;

    public EntryType(String name) {
        this.name=name.toLowerCase();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntryType entryType = (EntryType) o;

        return name != null ? name.equals(entryType.name) : entryType.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

}
