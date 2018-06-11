package com.progmasters.webshop.service;


import com.progmasters.webshop.domain.VerificationToken;
import com.progmasters.webshop.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VerificationTokenService {
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    public VerificationToken findByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }
}
