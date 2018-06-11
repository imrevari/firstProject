/*
 * Copyright © Progmasters (QTC Kft.), 2016.
 * All rights reserved. No part or the whole of this Teaching Material (TM) may be reproduced, copied, distributed,
 * publicly performed, disseminated to the public, adapted or transmitted in any form or by any means, including
 * photocopying, recording, or other electronic or mechanical methods, without the prior written permission of QTC Kft.
 * This TM may only be used for the purposes of teaching exclusively by QTC Kft. and studying exclusively by QTC Kft.’s
 * students and for no other purposes by any parties other than QTC Kft.
 * This TM shall be kept confidential and shall not be made public or made available or disclosed to any unauthorized person.
 * Any dispute or claim arising out of the breach of these provisions shall be governed by and construed in accordance with the laws of Hungary.
 */

package com.progmasters.webshop.service;

import com.progmasters.webshop.domain.WebshopUser;
import com.progmasters.webshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JPAUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public JPAUserDetailsService(UserRepository repository) {
        this.userRepository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        WebshopUser webshopUser = userRepository.findByEmail(userEmail);

        if (webshopUser == null) {
            throw new UsernameNotFoundException("No user found with name: " + userEmail);
        }

        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(webshopUser.getRole().toString());

        /*
        the previous line does the following, check the source of AuthorityUtils for details

        Set<GrantedAuthority> authorities = new HashSet<>();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(webshopUser.getRole().toString());
        authorities.add(authority);
        */

        UserDetails principal = User.withUsername(userEmail).authorities(authorities).password(webshopUser.getPassword()).build();

        /*
        the previous line is a shorthand for this (it converts our domain user, which is of type WebshopUser to
        Spring security's user which is of type UserDetails)

        org.springframework.security.core.userdetails.User principal =
                new org.springframework.security.core.userdetails.User(orcName, webshopUser.getPassword(), authorities);
         */
        return principal;
    }
}
