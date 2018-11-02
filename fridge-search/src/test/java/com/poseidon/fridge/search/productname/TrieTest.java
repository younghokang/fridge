package com.poseidon.fridge.search.productname;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.SortedMap;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.junit.Test;

import com.poseidon.fridge.search.productname.ProductNameTrie;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TrieTest {
    
    String[] eng_sample = {
            "springboot", "springcloud", "springmvc", "sports"
    };
    
    String[] sample = {
            "사람",
            "사냥꾼",
            "사슴",
            "가자미",
            "가재",
            "가마",
            "사재기",
            "사계절",
            "가마",
            "가솔린",
            "나무",
            "나비",
            "다다미",
            "다슬기",
            "사루만",
            "가마꾼",
            "가마니",
            "나방"
    };
    
    @Test
    public void learningTrie() {
        Trie<String, Integer> trie = new PatriciaTrie<>();
        for(int i=0; i<sample.length; i++) {
            trie.putIfAbsent(sample[i], i);
        }
        log.info("firstKey: {}", trie.firstKey());
        trie.forEach((key, value) -> log.info("key: {}, value: {}", key, value));
        System.out.println();
        trie.prefixMap("사").forEach((key, value) -> log.info("key: {}, value: {}", key, value));
    }
    
    @Test
    public void productNameTrie() {
        ProductNameTrie trie = new ProductNameTrie();
        
        for(int i=0; i<sample.length; i++) {
            trie.put(sample[i], 0);
        }
        
        SortedMap<String, Long> prefixMap = trie.prefixMap("가마");
        assertThat(prefixMap.size()).isEqualTo(3);
        assertThat(prefixMap.firstKey()).isEqualTo("가마");
        assertThat(prefixMap.lastKey()).isEqualTo("가마니");
        try {
            prefixMap.put("나팔", 0L);
            fail();
        } catch(UnsupportedOperationException e) {
        }
        
        String query = "가마";
        String[] result = trie.search(query);
        log.info("result: {}", Arrays.toString(result));
        assertThat(result.length).isEqualTo(2);
        assertThat(result[0]).isEqualTo("가마꾼");
        int value = 1;
        Long replaced = trie.increaseScore("가마니");
        assertThat(replaced).isNotNull();
        assertThat(replaced.intValue()).isEqualTo(value - 1);
        
        result = trie.search(query);
        trie.prefixMap(query).forEach((k, v) -> log.info("key: {}, value: {}", k, v));
        log.info("result: {}", Arrays.toString(result));
        assertThat(result[result.length - 1]).isEqualTo("가마꾼");
    }
    
}
