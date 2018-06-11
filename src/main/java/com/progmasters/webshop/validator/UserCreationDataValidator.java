package com.progmasters.webshop.validator;


import com.progmasters.webshop.domain.WebshopUser;
import com.progmasters.webshop.domain.dto.UserCreationData;
import com.progmasters.webshop.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class UserCreationDataValidator implements Validator{

    private UserService userService;

    public UserCreationDataValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserCreationData.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserCreationData userCreationData = (UserCreationData) target;
        WebshopUser webshopUser = userService.findByEmail(userCreationData.getEmail());
        if (!userCreationData.getEmail().matches(
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            errors.rejectValue("email", "email.invalidInput");
        } else if (webshopUser != null) {
            errors.rejectValue("email", "email.alreadyExists");
        }
        if (userCreationData.getPassword().length() < 8 || userCreationData.getPassword().length() > 18) {
            errors.rejectValue("password", "password.wrongLength");
        }
        if (!isPasswordProper(userCreationData.getPassword())){

            errors.rejectValue("password", "password.mustContain");

        }
    }

    private boolean isPasswordProper(String password){
        int property = 0;

        if (Pattern.compile("[A-Z]").matcher(password).find()) {

            property += 1;
        }if (Pattern.compile("[a-z]").matcher(password).find()){

            property += 1;
        }if(Pattern.compile("[0-9]").matcher(password).find()){
            ;
            property += 1;
        }if (Pattern.compile("[!:;.,?]").matcher(password).find()){

            property += 1;
        }
        System.err.println(property);
        if (property < 3){
            return false;
        }
        return true;
    }




}
