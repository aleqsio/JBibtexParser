package com.JBibtexParser.util;

import com.JBibtexParser.util.exceptions.ParseErrorException;
import javafx.util.Pair;

import java.util.*;
import java.util.function.IntBinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * An util class for handling text in brackest or quotes, allowing for splitting, joining etc. while maintaining correct bracket depth level
 */
public class LeveledString {
    /**
     * String contents
     */
    private String entry;
    /**
     * Levels indicating bracket depth of each char of entry
     */
    private int[] levels;

    public LeveledString(String entry) throws ParseErrorException {
        setEntry(entry);
    }

    public LeveledString(String entry, int[] levels) {
        this.entry = entry;
        this.levels = levels;
    }

    public LeveledString() {
        this.entry="";
        levels=new int[0];
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) throws ParseErrorException {
        this.entry = entry;
        parseLevels(entry);
    }
    public LeveledString join(LeveledString leveledString){
        entry=entry+leveledString.entry;
        levels= IntStream.concat(Arrays.stream(levels), Arrays.stream(leveledString.levels)).toArray();
        return new LeveledString(entry,levels);
    }
    private void parseLevels(String entry) throws ParseErrorException {
        levels = new int[entry.length()];
        Stack<Character> previousbrackets = new Stack<>();
        boolean insideQuotes = false;
        int pos = 0;
        char prevChar = 0;
        try {
            for (char c : entry.toCharArray()) {
                levels[pos] = previousbrackets.size();
                if (prevChar != '\\') {
                    if (c == '{' || (c == '(' && levels[pos] <= 1)) {
                        previousbrackets.push(c);
                    }
                    if (c == '}' || (c == ')' && levels[pos] <= 1)) {
                        if (c != rotateBracket(previousbrackets.pop())) {
                            throw new ParseErrorException("Mismatched brackets at pos " + pos + " in " + entry);
                        }
                    }
                    if (c == '"') {
                        insideQuotes = !insideQuotes;
                        if (insideQuotes) {
                            previousbrackets.push(c);
                        } else {
                            if (c != previousbrackets.pop()) {
                                throw new ParseErrorException("Mismatched quotes");
                            }
                        }
                    }
                }
                levels[pos] = Math.min(previousbrackets.size(), levels[pos]);
                pos++;
                prevChar = c;
            }
            if (previousbrackets.size()>0) throw new ParseErrorException("Mismatched quotes or brackets: "+ previousbrackets.pop());
        }catch (EmptyStackException e){
            throw new ParseErrorException("Mismatched quotes or brackets");
        }
    }

    private char rotateBracket(char bracket) {
        if (bracket == '{') return '}';
        if (bracket == '}') return '{';
        if (bracket == '(') return ')';
        if (bracket == ')') return '(';
        return bracket;
    }

    public LeveledString substituteOnLevel(int level, String [] finds, String replace) throws ParseErrorException {
        List<Pair> positionsandlevels = new ArrayList<>();
        String newEntry = entry;
        for(String find:finds){
            positionsandlevels.addAll( findAllOccurences(newEntry, find).stream().filter(p -> levels[p] <= level).map(p -> new Pair(p, find.length())).collect(Collectors.toList()));
        }
        positionsandlevels.sort(Comparator.comparing(p0 -> -((Integer) p0.getKey())));
        for (Pair<Integer, Integer> position : positionsandlevels) {
            newEntry = newEntry.substring(0, position.getKey()) + replace + newEntry.substring(position.getKey()+position.getValue(), newEntry.length());
        }
            return new LeveledString(newEntry);
    }

    @SuppressWarnings("unused")
    public LeveledString substituteUnascaped(String findEntriesContainingWords, String replace) {
        String newEntry = entry;
        List<Integer> positions = findAllOccurences(entry, findEntriesContainingWords).stream().filter(p -> p.equals(0) || entry.charAt(p-1)!='\\').collect(Collectors.toList());
        for (Integer position : positions) {
            newEntry = newEntry.substring(0, position) + replace + newEntry.substring(position + findEntriesContainingWords.length(), newEntry.length());
        }
        try {
            return new LeveledString(newEntry);
        } catch (ParseErrorException e) {
            return new LeveledString(newEntry, new int[newEntry.length()]);
        }
    }
    public static List<Integer> findAllOccurences(String string, String pattern) {
        int lastIndex = 0;
        List<Integer> result = new ArrayList<Integer>();
        while (lastIndex != -1) {
            lastIndex = string.indexOf(pattern, lastIndex);
            if (lastIndex != -1) {
                result.add(lastIndex);
                lastIndex += 1;
            }
        }
        return result;
    }

    public List<LeveledString> getSubstringsOfLevel(int level, IntBinaryOperator operator) {
        List<LeveledString> strings = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        List<Integer> lb = new LinkedList<>();

        for (int i = 0; i < entry.length(); i++) {
            if (operator.applyAsInt(levels[i], level) >= 0) {
                sb.append(entry.charAt(i));
                lb.add(levels[i] - level);
            } else if (sb.length() > 0) {
                LeveledString string = new LeveledString(sb.toString(), lb.stream().mapToInt(k -> k).toArray());
                strings.add(string);
                sb = new StringBuilder();
                lb.clear();
            }
        }
        return strings;
    }

    public String toString() {
        return entry;
    }
    @SuppressWarnings("unused")
    public String toStringWithLevels() {
        return entry + "\n" + String.join("", Arrays.stream(levels).mapToObj(String::valueOf).toArray(String[]::new)) + "\n";
    }
    @SuppressWarnings("unused")
    private String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1);
    }

    public List<LeveledString> splitOnLevel(char separator, int level) {

        List<LeveledString> strings = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        List<Integer> lb = new LinkedList<>();
        for (int i = 0; i < entry.length(); i++) {
            if (entry.charAt(i) != separator || levels[i] > level) {
                sb.append(entry.charAt(i));
                lb.add(levels[i] - level);
            } else {
                LeveledString string = new LeveledString(sb.toString(), lb.stream().mapToInt(k -> k).toArray());
                strings.add(string);
                sb = new StringBuilder();
                lb.clear();
            }
        }
        LeveledString string = new LeveledString(sb.toString(), lb.stream().mapToInt(k -> k).toArray());
        strings.add(string);
        return strings;
    }
    @SuppressWarnings("unused")
    public int[] getLevels() {
        return levels;
    }

    public List<Pair<LeveledString, LeveledString>> splitIntoKeyValuePairs() throws ParseErrorException {
        List<Pair<LeveledString, LeveledString>> pairs = new LinkedList<>();
        boolean foundName = false;
        List<LeveledString> substrings = getSubstringsOfLevel(1, (x, y) -> x - y);
        if (substrings.size() == 0) throw new ParseErrorException("Malformed bibtex");
        for (LeveledString leveledString : substrings.get(0).splitOnLevel(',', 0)) {
            List<LeveledString> keyValue = leveledString.splitOnLevel('=', 0);
            if(keyValue.size()>2) throw  new ParseErrorException("Too many keys in entry" + getEntry());
            if (keyValue.size() == 1 && leveledString.getEntry().trim().length() > 0) {
                pairs.add(new Pair<LeveledString, LeveledString>(new LeveledString("JBibtexParser_entry_id_specifier"), keyValue.get(0)));
                foundName = true;
            } else if (keyValue.size() == 2)
                pairs.add(new Pair<LeveledString, LeveledString>(keyValue.get(0), keyValue.get(1)));
        }
        if (!foundName)
            pairs.add(new Pair<LeveledString, LeveledString>(new LeveledString("JBibtexParser_entry_id_specifier"), new LeveledString("")));
        return pairs;
    }
    @SuppressWarnings("unused")
    public int length() {
        return entry.length();
    }
    public LeveledString trim(){
        String entry=this.entry;
        entry=entry.trim();
        try {
            return new LeveledString(entry);
        } catch (ParseErrorException e) {
            int[] zeros = new int[entry.length()];
            Arrays.fill(zeros,0);
            return new LeveledString(entry,zeros);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LeveledString that = (LeveledString) o;

        if (entry != null ? !entry.equals(that.entry) : that.entry != null) return false;
        return Arrays.equals(levels, that.levels);
    }

    @Override
    public int hashCode() {
        int result = entry != null ? entry.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(levels);
        return result;
    }
}
