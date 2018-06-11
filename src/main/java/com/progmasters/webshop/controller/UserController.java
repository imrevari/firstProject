package com.progmasters.webshop.controller;

import com.progmasters.webshop.domain.Role;
import com.progmasters.webshop.domain.WebshopUser;
import com.progmasters.webshop.domain.dto.UserCreationData;
import com.progmasters.webshop.domain.dto.UserListItem;
import com.progmasters.webshop.domain.dto.VerificationTokenDTO;
import com.progmasters.webshop.security.AuthenticatedUserDetails;
import com.progmasters.webshop.service.UserService;
import com.progmasters.webshop.validator.UserCreationDataValidator;
import com.progmasters.webshop.validator.VerificationTokenDTOValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.repository.query.Param;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;
    private UserCreationDataValidator userCreationDataValidator;
    private VerificationTokenDTOValidator verificationTokenDTOValidator;

    @Autowired
    public UserController(UserService userService, UserCreationDataValidator userCreationDataValidator, VerificationTokenDTOValidator verificationTokenDTOValidator) {
        this.userService = userService;
        this.userCreationDataValidator = userCreationDataValidator;
        this.verificationTokenDTOValidator = verificationTokenDTOValidator;
    }

    @InitBinder("userCreationData")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(userCreationDataValidator);
    }

    @InitBinder("verificationTokenDTO")
    protected void initTokenBinder(WebDataBinder binder) {
        binder.addValidators(verificationTokenDTOValidator);
    }

    @PostMapping("/register")
    public ResponseEntity<?> createNewWebshopUser(@Valid @RequestBody UserCreationData userCreationData) {
        WebshopUser webshopUser = new WebshopUser(userCreationData);
        if (userService.findByEmail(webshopUser.getEmail()) == null) {
            userService.saveUserAndSendToken(webshopUser);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @GetMapping("/me")
    public ResponseEntity<AuthenticatedUserDetails> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();
        WebshopUser webshopUser = userService.findByEmail(user.getUsername());
        String userName = webshopUser.getName();
        if (!webshopUser.isVerified()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(new AuthenticatedUserDetails(user, userName), HttpStatus.OK);
    }

    @GetMapping("/verification")
    public ResponseEntity<?> verifyWithURL(@Valid VerificationTokenDTO verificationTokenDTO) {

        userService.finalizationUser(verificationTokenDTO.getConfirmationCode());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/verification")
    public ResponseEntity<?> verifyWithToken(@Valid @RequestBody VerificationTokenDTO verificationTokenDTO) {
        userService.finalizationUser(verificationTokenDTO.getConfirmationCode());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/myAccount")
    public ResponseEntity<UserListItem> getWebshopUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();

        return new ResponseEntity<>(new UserListItem(userService.findByEmail(user.getUsername())), HttpStatus.OK);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserListItem>> listUsers() {


        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserListItem> getUserById(@PathVariable Long id) {
        WebshopUser user = userService.findById(id);

        if (user != null) {
            return new ResponseEntity<>(new UserListItem(user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserListItem userListItem) {

        WebshopUser user = userService.updateUser(userListItem);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    private Map<String, String> getRoles() {
        Map<String, String> roles = new LinkedHashMap<>();
        for (Role role : Role.values()) {
            roles.put(role.name(), role.getDisplayedname());
        }
        return roles;
    }


    @GetMapping("/roles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> getRolesOfUsers() {
        return new ResponseEntity<>(getRoles(), HttpStatus.OK);
    }

    @PostMapping("/newPassword")
    public ResponseEntity<?> sendNewPassword(@RequestBody String email) {

        String string = email;
        String ad = email.substring(10, string.length()-2);

        ResponseEntity<?> responseEntity = userService.sendNewPassword(ad);
        return responseEntity;
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody String email) {

        String [] strings = email.split(",");
        String oldPassword = strings[0];
        String oldPasswordCut = oldPassword.substring(20,oldPassword.length()-1);
        String newPassword = strings[1];
        String newPasswordCut = newPassword.substring(15,newPassword.length()-1);
        String newPasswordAgain = strings[2];
        String newPasswordAgainCut = newPasswordAgain.substring(20,newPasswordAgain.length()-2);

        ResponseEntity<?> responseEntity = userService.changePassword(oldPasswordCut,newPasswordCut,newPasswordAgainCut);
        return responseEntity;
    }

}
