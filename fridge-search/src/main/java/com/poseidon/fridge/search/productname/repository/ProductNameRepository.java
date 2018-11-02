package com.poseidon.fridge.search.productname.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.poseidon.fridge.search.productname.model.ProductName;

@RepositoryRestResource
public interface ProductNameRepository extends JpaRepository<ProductName, String> {

}
