package com.progmasters.webshop.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

public class AuthenticatedUserDetails {

    private String email;

    private String role;

    private String name;

    public AuthenticatedUserDetails() {
    }

    public AuthenticatedUserDetails(UserDetails user, String userName) {
        this.email = user.getUsername();
        this.role = findRole(user);
        this.name = userName;
    }

    private String findRole(UserDetails user) {
        String role = null;
        List<GrantedAuthority> roles = user.getAuthorities().stream()
                .filter(authority -> authority.getAuthority().startsWith("ROLE_"))
                .collect(Collectors.toList());
        if (!roles.isEmpty()) {
            role = roles.get(0).getAuthority();
        }

        return role;
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
