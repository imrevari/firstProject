package com.progmasters.webshop.service;


import com.progmasters.webshop.domain.Role;
import com.progmasters.webshop.domain.VerificationToken;
import com.progmasters.webshop.domain.WebshopUser;

import com.progmasters.webshop.domain.dto.UserListItem;
import com.progmasters.webshop.repository.UserRepository;
import com.progmasters.webshop.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.Session;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;


@Service
@Transactional
public class UserService {

    private static final String ALPHA_NUM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int TOKEN_LENGTH = 5;

    private static final String NUMBERS = "0123456789";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private PasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;
    private VerificationTokenRepository verificationTokenRepository;
    private SimpleMailMessage emailTemplate;
    private SimpleMailMessage passwordTemplate;
    private EmailService emailService;

    @Autowired
    public UserService(PasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, VerificationTokenRepository verificationTokenRepository, EmailService emailService, @Qualifier("bean1") SimpleMailMessage emailTemplate, @Qualifier("bean2") SimpleMailMessage passwordTemplate) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.emailTemplate = emailTemplate;
        this.emailService = emailService;
        this.passwordTemplate = passwordTemplate;
    }

    public WebshopUser findByEmail(String webshopEmail) {
        return userRepository.findByEmail(webshopEmail);
    }

    public WebshopUser findById(Long id) {
        return userRepository.findById(id);
    }

    public WebshopUser save(WebshopUser user) {
        user.setRole(Role.ROLE_USER);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public void saveUserAndSendToken(WebshopUser user) {

        user.setRole(Role.ROLE_USER);
       user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        VerificationToken token = new VerificationToken();
        String tokenString;
        do {
            tokenString = generateVerificationToken();
        } while (verificationTokenRepository.findByToken(tokenString) != null);

        token.setToken(tokenString);
        token.setWebshopUser(user);

        verificationTokenRepository.save(token);

        String url = "http://localhost:3000/verification/" + token.getToken();

        String text = String.format(emailTemplate.getText(), user.getName(), token.getToken(), url);
        emailService.sendSimpleMessage(user.getEmail(), "Verification Token", text);
    }

    public WebshopUser finalizationUser(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        WebshopUser user = verificationToken.getWebshopUser();
        user.setVerified(true);
        return userRepository.save(user);
    }


    private String generateVerificationToken() {
        char[] temp = new char[TOKEN_LENGTH];
        char[] symbols = ALPHA_NUM.toCharArray();
        Random random = new Random();
        for (int i = 0; i < temp.length; i++) {
            temp[i] = symbols[random.nextInt(symbols.length)];
        }
        return new String(temp);
    }

    private String generateNewPassword() {
        Random random = new Random();
        char[] password = new char[9];
        for (int i = 0; i < password.length - 2; ) {
            password[i] = NUMBERS.charAt(random.nextInt(NUMBERS.length()));
            password[i + 1] = UPPERCASE.charAt(random.nextInt(UPPERCASE.length()));
            password[i + 2] = LOWERCASE.charAt(random.nextInt(LOWERCASE.length()));
            i += 2;
        }
        return new String(password);
    }

    public List<UserListItem> findAllUsers() {

        List<WebshopUser> listOfUsers = userRepository.findAllByOrderById();

        List<UserListItem> listToReturn = new ArrayList<>();

        for (WebshopUser user : listOfUsers) {
            UserListItem item = new UserListItem(user);
            listToReturn.add(item);
        }
        return listToReturn;
    }

    public WebshopUser updateUser(UserListItem userListItem) {
        WebshopUser user = userRepository.findOne(userListItem.getId());

        if (user != null) {
            user.setRole(Role.valueOf(userListItem.getRole()));

            userRepository.save(user);
            return user;
        } else {
            return null;
        }
    }

    public ResponseEntity<?> sendNewPassword(String email) {
        ResponseEntity<?> responseEntity;

        if (!email.matches(
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            String text = "{\"fieldErrors\":[{\"field\":\"email\",\"message\":\"Invalid input format\"}]}";

            responseEntity = new ResponseEntity<>(text, HttpStatus.BAD_REQUEST);
        } else {
            WebshopUser user = userRepository.findByEmail(email);

            if (user != null) {
                String newPassword = generateNewPassword();
               user.setPassword(bCryptPasswordEncoder.encode(newPassword));

                String text = String.format(passwordTemplate.getText(), user.getName(), newPassword);
                emailService.sendSimpleMessage(user.getEmail(), "Password", text);
                userRepository.save(user);
                responseEntity = new ResponseEntity<>(HttpStatus.OK);
            } else {
                String text = "{\"fieldErrors\":[{\"field\":\"email\",\"message\":\"There is no user for this email address\"}]}";


                responseEntity = new ResponseEntity<>(text, HttpStatus.BAD_REQUEST);
            }
        }
        return responseEntity;
    }

    public ResponseEntity<?> changePassword(String oldPasswordCut, String newPasswordCut, String newPasswordAgainCut) {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        User principal = (User) authentication.getPrincipal();
        WebshopUser user = userRepository.findByEmail(principal.getUsername());
        ResponseEntity responseEntity;
        if (!bCryptPasswordEncoder.matches(oldPasswordCut,user.getPassword())) {
            String text = "{\"fieldErrors\":[{\"field\":\"currentPassword\",\"message\"" +
                    ":\"The current password is wrong\"}]}";
            responseEntity = new ResponseEntity<>(text, HttpStatus.BAD_REQUEST);
        } else if (!newPasswordCut.equals(newPasswordAgainCut)) {
            String text = "{\"fieldErrors\":[{\"field\":\"newPassword\",\"message\"" +
                    ":\"The new passwords dont match\"}]}";
            responseEntity = new ResponseEntity<>(text, HttpStatus.BAD_REQUEST);
        } else if (!isPasswordProper(newPasswordCut)) {
            String text = "{\"fieldErrors\":[{\"field\":\"newPassword\",\"message\"" +
                    ":\"The new passwords are not acceptable\"}]}";
            responseEntity = new ResponseEntity<>(text, HttpStatus.BAD_REQUEST);
        } else {
           user.setPassword(bCryptPasswordEncoder.encode(newPasswordCut));

            userRepository.save(user);
            responseEntity = new ResponseEntity<>( HttpStatus.OK);
        }
        return responseEntity;
    }


    private boolean isPasswordProper(String password) {
        int property = 0;
        if (password.length() >= 8 && password.length() < 18) {
            property += 1;
        }
        if (Pattern.compile("[A-Z]").matcher(password).find()) {
            property += 1;
        }
        if (Pattern.compile("[a-z]").matcher(password).find()) {
            property += 1;
        }
        if (Pattern.compile("[0-9]").matcher(password).find()) {
            property += 1;
        }
        if (Pattern.compile("[!:;.,?]").matcher(password).find()) {
            property += 1;
        }
        System.err.println(property);
        if (property < 4) {
            return false;
        }
        return true;
    }
}
