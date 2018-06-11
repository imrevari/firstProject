package com.progmasters.webshop.validator;

import com.progmasters.webshop.domain.dto.ProductCreationData;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProductCreationDataValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ProductCreationData.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductCreationData productCreationData = (ProductCreationData) target;
        if (productCreationData.getName().trim().length() < 1) {
            errors.rejectValue("name", "productName.notGiven");
        }
        if (productCreationData.getDescription().trim().length() < 3) {
            errors.rejectValue("description", "productDescription.notGiven");
        }
        if (productCreationData.getPrice() == null) {
            errors.rejectValue("price", "productPrice.notGiven");
        } else if (productCreationData.getPrice() > 100000) {
            errors.rejectValue("price", "productPrice.tooHigh");
        }
    }
}
