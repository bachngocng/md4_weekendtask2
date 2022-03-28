package com.codegym.service.product;

import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService extends IGeneralService<Product> {
    Page<Product> searchProductByPartOfName(String name, Pageable pageable);

    Iterable<Product> findProductPriceBetween(Double min, Double max);

    Iterable<Product> findAllByCategory(Category category);

    Page<Product> findAll(Pageable pageable);
}
