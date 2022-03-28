package com.codegym.model;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class ProductForm {
    private Long id;

    @NotEmpty(message = "Not nullable")
    @Size(min=6,max=20,message = "Product name must have 5 to 20 characters")
    private String name;

    @NotNull()
    private double price;

    @NotNull
    private String description;

    private MultipartFile image;
    private Category category;

    public ProductForm() {
    }

    public ProductForm(Long id, String name, double price, String description, MultipartFile image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
