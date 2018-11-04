package com.poseidon.fridge.category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.poseidon.fridge.category.model.Category;

@Component
public class CategoryClassifier {
    private List<Category> categories = Collections.synchronizedList(new ArrayList<>());
    private static final JaccardSimilarity jaccardSimilarity = new JaccardSimilarity();
    
    public void addAll(List<Category> categories) {
        this.categories.addAll(categories);
    }

    public List<Category> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public Category process(String query) {
        return categories.stream()
            .map(category -> {
                double score = category.getKeywords().parallelStream()
                        .map(keyword -> jaccardSimilarity.apply(keyword, query))
                        .max(Comparator.naturalOrder()).orElse(0.0);
                category.setScore(score);
                return category;
            }).collect(Collectors.maxBy(Comparator.comparing(Category::getScore)))
            .orElse(null);
    }

}
