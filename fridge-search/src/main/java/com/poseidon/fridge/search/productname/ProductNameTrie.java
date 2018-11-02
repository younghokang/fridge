package com.poseidon.fridge.search.productname;

import java.util.SortedMap;
import java.util.stream.Collectors;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.map.UnmodifiableSortedMap;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.springframework.stereotype.Component;

@Component
public class ProductNameTrie {
    private Trie<String, Long> trie = new PatriciaTrie<>();
    
    private static final int MAX_SIZE = 10;

    public synchronized Long put(String key, long value) {
        return trie.put(key, value);
    }
    
    public String[] search(String query) {
        return prefixMap(query)
                .entrySet().stream()
                .filter(entry -> {
                    return !entry.getKey().equals(query);
                })
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(MAX_SIZE)
                .map(entry -> {
                    return entry.getKey();
                })
                .collect(Collectors.toList())
                .toArray(new String[0]);
    }
    
    SortedMap<String, Long> prefixMap(String key) {
        return UnmodifiableSortedMap.unmodifiableSortedMap(trie.prefixMap(key));
    }

    public synchronized Long increaseScore(String key) {
        if(trie.containsKey(key)) {
            return trie.put(key, trie.get(key) + 1);
        }
        return null;
    }
    
}
