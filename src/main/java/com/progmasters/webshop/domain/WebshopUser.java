package com.progmasters.webshop.domain;


import com.progmasters.webshop.domain.dto.UserCreationData;

import javax.persistence.*;

@Entity
public class WebshopUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    private boolean verified;

    @Enumerated(EnumType.STRING)
    private Role role;

    public WebshopUser() {

    }

    public WebshopUser(UserCreationData userCreationData) {
        this.email = userCreationData.getEmail();
        this.password = userCreationData.getPassword();
        this.name = userCreationData.getName();
        this.verified = false;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebshopUser that = (WebshopUser) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
