package com.JBibtexParser.bibliography;

import com.JBibtexParser.entry.entries.PublicationEntry;
import com.JBibtexParser.typemanager.IEntryTypesManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * An Implementation of {@link IBibliographyManager} using a map of words in all fields for O(1) lookup of entries when provided with whole word queries.
 * You can chain find methods for filtering over multiple queries.
 *
 */
public class KeywordBibliographyManager implements IBibliographyManager{

	private Map<String, List<PublicationEntry>> keywords=new HashMap<>();
	private Bibliography bibliography = new Bibliography();


	private KeywordBibliographyManager(Map<String,List<PublicationEntry>> keywords,Bibliography bibliography)
	{
		this.keywords=keywords;
		this.bibliography = bibliography;
	}
	public KeywordBibliographyManager(){

	}

	/**
	 * Adds provided entry to this manager's bibliography. Also adds all keywords for fast lookup.
	 * @param publicationEntry
	 */
	@Override
	public void add(PublicationEntry publicationEntry) {
		bibliography.add(publicationEntry);
		addKeywords(publicationEntry);
	}

	/**
	 *
	 * @param field - type of fields to look trough for values matching regex
	 * @param regex - regular expression used for filtering fields of given type
	 * @return
	 */
	public KeywordBibliographyManager findFieldsOfValue(IEntryTypesManager.IEntryField field, String regex) {
		Pattern pattern = Pattern.compile(regex.toLowerCase());
		Bibliography b=new Bibliography();
		bibliography.getEntries().stream().filter(p->p.getFields().containsKey(field)).filter(p->p.getFields().get(field).stream().anyMatch(r->pattern.matcher(r.toLowerCase()).find())).forEach(b::add);
		return new KeywordBibliographyManager(keywords,b);
	}


	/**
	 * Best method for looking up entries - O(1) time complexity
	 * @param query - search query - must be a whole word - space to space
	 * @return
	 */
	public KeywordBibliographyManager findEntriesContainingWords(String query){
		Bibliography b=new Bibliography();
		if(!keywords.containsKey(query)) return new KeywordBibliographyManager(keywords,b);
		for(PublicationEntry entry:keywords.get(query)){
			if(bibliography.getEntries().contains(entry))
			b.add(entry);
		}
		return new KeywordBibliographyManager(keywords,b);
	}

	/**
	 * Allows filtering by bibtex entry type in accordance to provided {@link com.JBibtexParser.typemanager.definitions.IDefinition}
	 * @param type
	 * @return
	 */
	public KeywordBibliographyManager filterByType(IEntryTypesManager.IEntryType type) {
		Bibliography b=new Bibliography();
		bibliography.getEntries().stream().filter(p->p.getEntryType().equals(type)).forEach(b::add);
		return new KeywordBibliographyManager(keywords,b);
	}

	/**
	 * Returns {@link Bibliography} - a set of bibliography items
	 * @return
	 */
	@Override
	public Bibliography getBibliography() {
		return bibliography;
	}

	private void addKeywords(PublicationEntry publicationEntry) {

		for(List<String> field:publicationEntry.getFields().values()){
			for(String subField:field){
				for(String word:subField.split(" ")){
                    word=word.replaceAll("[{|}|\\\\|/]","").toLowerCase();
					if(keywords.containsKey(word)){
						keywords.get(word).add(publicationEntry);
					}else {
						List<PublicationEntry> list = new ArrayList<>();
						list.add(publicationEntry);
						keywords.put(word,list);
					}

				}
			}
		}

	}

}