package com.progmasters.webshop.service;

import com.progmasters.webshop.domain.Category;
import com.progmasters.webshop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category findByName(String name) {
        return categoryRepository.findByName(name);
    }

    public List<String> findAllName() {
        return categoryRepository.findAllName();
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }
}
