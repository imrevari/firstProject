package com.progmasters.webshop.validator;

import com.progmasters.webshop.domain.Category;
import com.progmasters.webshop.domain.dto.CategoryCreationData;
import com.progmasters.webshop.service.CategoryService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CategoryCreationDataValidator implements Validator {

    private CategoryService categoryService;

    public CategoryCreationDataValidator(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CategoryCreationData.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CategoryCreationData categoryCreationData = (CategoryCreationData) target;
        Category category = categoryService.findByName(categoryCreationData.getName());
        if (categoryCreationData.getName().trim().length() < 1) {
            errors.rejectValue("name", "categoryName.notGiven");
        } else if (category != null) {
            errors.rejectValue("name", "categoryName.isAlreadyTaken");
        }
        if (categoryCreationData.getName().trim().equals("N/A")) {
            errors.rejectValue("name", "categoryName.isAlreadyTaken");
        }
    }

}
