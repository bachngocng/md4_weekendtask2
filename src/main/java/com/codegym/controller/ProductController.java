package com.codegym.controller;

import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.model.ProductForm;
import com.codegym.service.category.ICategoryService;
import com.codegym.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
public class ProductController {
    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @Value("E:/CodeGym/MODULE 4/BaiCuoiTuan2/image/")
    private String uploadPath;

    @GetMapping("/products/list")
    public ModelAndView showListProduct(@RequestParam(name="q")Optional<String> q,@PageableDefault(value = 5) Pageable pageable){
        Page<Product> products;
        ModelAndView modelAndView = new ModelAndView("/product/list");
        if(!q.isPresent()){
            products=productService.findAll(pageable);
        } else {
            modelAndView.addObject("q", q.get());
            products = productService.searchProductByPartOfName(q.get(), pageable);
        }
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @GetMapping("/products/delete/{id}")
    public ModelAndView showDeleteProduct(@PathVariable Long id) {
        Optional<Product> productOptional = productService.findById(id);
        if (!productOptional.isPresent()) {
            return new ModelAndView("error-404");
        }
        ModelAndView modelAndView = new ModelAndView("/product/delete");
        modelAndView.addObject("product", productOptional.get());
        return modelAndView;
    }

    @PostMapping("/products/delete/{id}")
    public ModelAndView deleteProduct(@PathVariable Long id) {
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            File file = new File(uploadPath + product.get().getImage());
            if (file.exists()) {
                file.delete();
            }
            productService.removeById(id);
            return new ModelAndView("redirect:/products/list");
        }
        return new ModelAndView("error-404");
    }
    @GetMapping("/products/create")
    public ModelAndView showCreateProduct() {
        ModelAndView modelAndView = new ModelAndView("/product/create");
        Iterable<Category> categories = categoryService.findAll();
        modelAndView.addObject("categories", categories);
        modelAndView.addObject("product", new ProductForm());//G???i 1 ?????i t?????ng product r???ng sang file view ????? t???o m???i
        return modelAndView;
    }

    @PostMapping("/products/create")
    public ModelAndView createProduct(@Validated @ModelAttribute("product") ProductForm productForm,BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return new ModelAndView("/product/create");
        }
        String fileName = productForm.getImage().getOriginalFilename();
        long currentTime = System.currentTimeMillis(); //X??? l?? l???y th???i gian hi???n t???i
        fileName = currentTime + fileName;
        try {
            FileCopyUtils.copy(productForm.getImage().getBytes(), new File(uploadPath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Product product = new Product(productForm.getId(), productForm.getName(), productForm.getPrice(), productForm.getDescription(), fileName);
        product.setCategory(productForm.getCategory());
        productService.save(product);
        return new ModelAndView("redirect:/products/list");
    }

    @GetMapping("/products/edit/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) {
        Optional<Product> productOptional = productService.findById(id);
        if (!productOptional.isPresent()) {
            return new ModelAndView("error-404");
        }
        ModelAndView modelAndView = new ModelAndView("/product/edit");
        modelAndView.addObject("product", productOptional.get());
        return modelAndView;
    }

    @PostMapping("/products/edit/{id}")
    public ModelAndView editProduct(@PathVariable Long id, @ModelAttribute ProductForm productForm) {
        MultipartFile img = productForm.getImage();
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            Product oldProduct = product.get();
            if (img.getSize() != 0) {
                String fileName = productForm.getImage().getOriginalFilename();
                long currentTime = System.currentTimeMillis(); //X??? l?? l???y th???i gian hi???n t???i
                fileName = currentTime + fileName;
                oldProduct.setImage(fileName);
                try {
                    FileCopyUtils.copy(img.getBytes(), new File(uploadPath + fileName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            oldProduct.setPrice(productForm.getPrice());
            oldProduct.setDescription(productForm.getDescription());
            oldProduct.setName(productForm.getName());
            productService.save(oldProduct);
            return new ModelAndView("redirect:/products/list");
        }
        return new ModelAndView("error-404");
    }

    @GetMapping("/products/{id}")
    public ModelAndView showProductDetail(@PathVariable Long id) {
        Optional<Product> productOptional = productService.findById(id);
        if (!productOptional.isPresent()) {
            return new ModelAndView("error-404");
        }
        ModelAndView modelAndView = new ModelAndView("/product/view");
        modelAndView.addObject("product", productOptional.get());
        return modelAndView;
    }

    @GetMapping("/products/search")
    public ModelAndView showProductSearch(@RequestParam("min") Double min, @RequestParam("max") Double max) {
        Iterable<Product> products = productService.findProductPriceBetween(min, max);
        ModelAndView modelAndView = new ModelAndView("/product/list");
        modelAndView.addObject("products", products);
        return modelAndView;
    }
}

