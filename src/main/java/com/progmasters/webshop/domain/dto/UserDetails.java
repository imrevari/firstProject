package com.progmasters.webshop.domain.dto;

import com.progmasters.webshop.domain.WebshopUser;

public class UserDetails {

    private Long id;

    private String email;

    private String role;

    private String name;

    public UserDetails() {
    }

    public UserDetails(WebshopUser webshopUser) {
        this.id = webshopUser.getId();
        this.email = webshopUser.getEmail();
        this.role = webshopUser.getRole().name();
        this.name = webshopUser.getName();
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }
}
