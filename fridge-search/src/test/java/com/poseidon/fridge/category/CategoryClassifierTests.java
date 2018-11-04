package com.poseidon.fridge.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import com.poseidon.fridge.category.model.Category;
import com.poseidon.fridge.category.repository.CategoryRepository;

@RunWith(Parameterized.class)
@DataJpaTest
public class CategoryClassifierTests {
    
    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();
    
    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();
    
    @Autowired
    private CategoryRepository repository;
    
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            {"[노브랜드] 사과 주스 1.5L", "음료"},
            {"맛있는 사과 5kg (23과내)", "과일"},
            {"횡성한우 양지 1등급 미절단(팩)100g", "정육"}
        });
    }
    
    public String query;
    public String categoryName;
    
    public CategoryClassifierTests(String query, String categoryName) {
        this.query = query;
        this.categoryName = categoryName;
    }
    
    @Test
    public void learningJaccardSimilarity() {
        String str1 = "handshake";
        String str2 = "shake hands";
        JaccardSimilarity jaccardSimilarity = new JaccardSimilarity();
        double answer = jaccardSimilarity.apply(str1, str2);
        assertThat(answer).isEqualTo(1.0);
    }
    
    @Test
    public void newCategoryClassifier() {
        List<Category> categories = repository.findAll();
        CategoryClassifier classifier = new CategoryClassifier();
        classifier.addAll(categories);
        assertThat(classifier.getCategories().size()).isEqualTo(23);
        
        Category category = classifier.process(query);
        assertThat(category.getName()).isEqualTo(categoryName);
    }
    
}
