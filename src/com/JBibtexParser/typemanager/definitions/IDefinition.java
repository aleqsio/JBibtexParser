package com.JBibtexParser.typemanager.definitions;

import com.JBibtexParser.typemanager.EntryField;
import com.JBibtexParser.typemanager.EntryType;
import com.JBibtexParser.typemanager.IEntryTypesManager;

import java.util.*;

/**
 * An interface for definition used for verification and filling types and fields in a {@link com.JBibtexParser.typemanager.StaticTypesManager}
 */
public interface IDefinition {
    Map<IEntryTypesManager.IEntryType, Map<IEntryTypesManager.IEntryField, FieldProperties>> getDefinition();
    Set<EntryField> getFields();
    Set<EntryType> getTypes();

    default <T1,T2> HashMap<T1, T2> buildMap(Object... data){
        HashMap<T1, T2> result = new HashMap<>();

        if(data.length % 2 != 0)
            throw new IllegalArgumentException("Odd number of arguments");

        T1 key = null;
        Integer step = -1;

        for(Object value : data){
            step++;
            switch(step % 2){
                case 0:
                    key = (T1) value;
                    continue;
                case 1:
                    result.put(key, (T2) value);
                    break;
            }
        }

        return result;
    }
    default <T1> Set<T1> buildSet(Object... data){

        Set<T1> result = new HashSet<>();
        result.addAll(Arrays.asList((T1[]) data));
        return result;
    }
}
