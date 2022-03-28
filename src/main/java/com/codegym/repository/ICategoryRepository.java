package com.codegym.repository;

import com.codegym.model.Category;
import com.codegym.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ICategoryRepository extends PagingAndSortingRepository<Category,Long> {
    @Query(value = "select * from products where category_id = ?1;", nativeQuery = true)
    Iterable<Product> productsFindByCategoryId(Long id);

    @Modifying
    @Query(value = "call delete_category(?1)", nativeQuery = true)
    void deleteByProcedure(Long category_id);

    Page<Category> findByNameContaining(String name, Pageable pageable);

}
