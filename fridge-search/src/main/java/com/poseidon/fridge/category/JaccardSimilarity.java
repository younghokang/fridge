package com.poseidon.fridge.category;

import java.util.regex.Pattern;

import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.multiset.HashMultiSet;
import org.apache.commons.lang.StringUtils;

public class JaccardSimilarity {
    
    public Double apply(String left, String right) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }
        left = StringUtils.lowerCase(left);
        right = StringUtils.lowerCase(right);
        return calculateJaccardSimilarity(left, right);
    }
    
    private Double calculateJaccardSimilarity(final CharSequence left, final CharSequence right) {
        final int leftLength = left.length();
        final int rightLength = right.length();
        if (leftLength == 0 || rightLength == 0) {
            return 0d;
        }
        final MultiSet<CharSequence> leftSet = new HashMultiSet<>();
        for (int i = 0; i < leftLength - 1; i++) {
            CharSequence word = left.subSequence(i, i+2);
            if(!isValidWord(word)) {
                continue;
            }
            leftSet.add(word);
        }
        final MultiSet<CharSequence> rightSet = new HashMultiSet<>();
        for (int i = 0; i < rightLength - 1; i++) {
            CharSequence word = right.subSequence(i, i+2);
            if(!isValidWord(word)) {
                continue;
            }
            rightSet.add(word);
        }
        if(leftSet.isEmpty() && rightSet.isEmpty()) {
            return 1.0d;
        }
        final MultiSet<CharSequence> unionSet = new HashMultiSet<>(leftSet);
        unionSet.addAll(rightSet);
        long intersection = unionSet.entrySet()
            .parallelStream().filter(entry -> {
                return entry.getCount() > 1;
            })
            .count();
        return 1.0d * intersection / unionSet.uniqueSet().size();
    }
    
    private boolean isValidWord(CharSequence word) {
        return Pattern.matches("^[a-zA-Z0-9ㄱ-ㅎ가-힣]*$", word);
    }
    
}