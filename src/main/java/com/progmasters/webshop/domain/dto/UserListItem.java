package com.progmasters.webshop.domain.dto;

import com.progmasters.webshop.domain.WebshopUser;

public class UserListItem {

    private Long id;

    private String email;

    private String name;

    private String role;

    public UserListItem(){

    }

    public UserListItem(WebshopUser webshopUser) {
        this.id = webshopUser.getId();
        this.email = webshopUser.getEmail();
        this.role = webshopUser.getRole().name();
        this.name = webshopUser.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserListItem that = (UserListItem) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
