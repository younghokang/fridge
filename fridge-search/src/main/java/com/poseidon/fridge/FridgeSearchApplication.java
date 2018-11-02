package com.poseidon.fridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import com.poseidon.fridge.search.productname.ProductNameTrie;
import com.poseidon.fridge.search.productname.repository.ProductNameRepository;

@SpringBootApplication
@EnableDiscoveryClient
public class FridgeSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(FridgeSearchApplication.class, args);
	}
	
	@Bean
	public ProductNameTrie productNameTrie(ProductNameRepository productRepository) {
	    ProductNameTrie trie = new ProductNameTrie();
	    productRepository.findAll().stream()
            .forEach(product -> trie.put(product.getName(), product.getScore()));
	    return trie;
	}
	
}
