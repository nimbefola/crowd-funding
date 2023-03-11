package com.pentspace.crowdfundingservice.service;

public interface HashManagerHandler {
    boolean validateData(String passwordPlainText, String databasePassword);
}
