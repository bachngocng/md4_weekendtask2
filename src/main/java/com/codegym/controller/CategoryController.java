package com.codegym.controller;

import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.service.category.ICategoryService;
import com.codegym.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/list")
    public ModelAndView showCategoryList(@RequestParam(name="q")Optional<String> q,@PageableDefault(value = 5) Pageable pageable) {
        Page<Category> categories;
        ModelAndView modelAndView = new ModelAndView("/category/list");
        if(!q.isPresent()){
            categories=categoryService.findAll(pageable);
        } else {
            modelAndView.addObject("q", q.get());
            categories = categoryService.findByNameContaining(q.get(), pageable);
        }
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView showCategoryDetail(@PathVariable Long id) {
        Optional<Category> categoryOptional = categoryService.findById(id);
        if (!categoryOptional.isPresent()) {
            return new ModelAndView("error-404");
        }
        Iterable<Product> products = productService.findAllByCategory(categoryOptional.get());
        ModelAndView modelAndView = new ModelAndView("category/view", "products", products);
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/category/delete");
        Optional<Category> category = categoryService.findById(id);
        if (!category.isPresent()) {
            return new ModelAndView("error-404");
        }
        modelAndView.addObject("category", category.get());
        return modelAndView;
    }

    @PostMapping("/delete/{id}")
    public ModelAndView deleteCategory(@PathVariable Long id) {
        categoryService.deleteByProcedure(id);
        ModelAndView modelAndView = new ModelAndView("redirect:/categories/list");
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showCreateCategory(){
        ModelAndView modelAndView = new ModelAndView("/category/create");
        Category category = new Category();
        modelAndView.addObject("category",category);
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView createCategory(@ModelAttribute Category category){
        Category category1 = new Category(category.getName());
        categoryService.save(category1);
        ModelAndView modelAndView = new ModelAndView("/category/create");
        modelAndView.addObject("category",category1);
        modelAndView.addObject("message","Add Successful!");
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable Long id){
        Optional<Category> category = categoryService.findById(id);
        if (!category.isPresent()) {
            return new ModelAndView("error-404");
        }
        ModelAndView modelAndView = new ModelAndView("/category/edit", "category",category.get());
        return modelAndView;
    }

    @PostMapping("/edit/{id}")
    public ModelAndView editCategory(@ModelAttribute Category category){
        categoryService.save(category);
        ModelAndView modelAndView = new ModelAndView("/category/edit");
        modelAndView.addObject("message", "Add Successful!");
        return modelAndView;
    }
}
