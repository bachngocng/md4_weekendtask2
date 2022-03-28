package com.codegym.repository;

import com.codegym.model.Category;
import com.codegym.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface IProductRepository extends PagingAndSortingRepository<Product, Long> {

    @Query(value = "select * from " + "products where (price between ?1 and ?2)" + "and image is not null", nativeQuery = true)
    Iterable<Product> findProductByPriceBetween(Double min, Double max);

    Iterable<Product> findAllByCategory(Category category);

    Page<Product> findByNameContaining(String name, Pageable pageable);
}
