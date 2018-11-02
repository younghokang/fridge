package com.poseidon.fridge.search.productname;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.poseidon.fridge.search.productname.model.ProductName;
import com.poseidon.fridge.search.productname.repository.ProductNameRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductTests {
    
    @Autowired
    ProductNameRepository repository;
    
    @Test
    public void newProduct() {
        ProductName product = ProductName.builder()
                .name("사과")
                .score(1)
                .build();
        assertThat(product.getName()).isEqualTo("사과");
        assertThat(product.getScore()).isEqualTo(1);
        
        repository.save(product);
        
        ProductName apple = repository.findById("사과")
                .orElse(null);
        assertThat(apple).isNotNull();
        assertThat(apple).isEqualTo(product);
    }

}
