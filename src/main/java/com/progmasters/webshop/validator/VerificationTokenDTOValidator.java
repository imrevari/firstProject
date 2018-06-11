package com.progmasters.webshop.validator;


import com.progmasters.webshop.domain.VerificationToken;
import com.progmasters.webshop.domain.WebshopUser;
import com.progmasters.webshop.domain.dto.VerificationTokenDTO;
import com.progmasters.webshop.service.UserService;
import com.progmasters.webshop.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class VerificationTokenDTOValidator implements Validator {

    private VerificationTokenService verificationTokenService;
    private UserService userService;


    @Autowired
    public VerificationTokenDTOValidator(VerificationTokenService verificationTokenService, UserService userService) {
        this.verificationTokenService = verificationTokenService;
        this.userService = userService;

    }

    @Override
    public boolean supports(Class clazz) {
        return VerificationTokenDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        VerificationTokenDTO verificationTokenDTO = (VerificationTokenDTO) target;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        VerificationToken verificationToken = verificationTokenService.findByToken(verificationTokenDTO.getConfirmationCode());

        if (verificationToken == null ) {
            errors.rejectValue("confirmationCode", "confirmationCode.invalid");
        } else if (verificationToken.getWebshopUser().isVerified()) {
            errors.rejectValue("confirmationCode", "confirmationCode.used");
        }

    }
}
