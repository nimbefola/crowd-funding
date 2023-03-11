package com.pentspace.crowdfundingservice.service.impl;

import com.pentspace.crowdfundingservice.service.HashManagerHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class HashManagerHandlerImpl implements HashManagerHandler {
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Override
    public boolean validateData(String passwordPlainText, String databasePassword) {
        return bCryptPasswordEncoder.matches(passwordPlainText,databasePassword);
    }


}
