package com.pentspace.crowdfundingservice.dto;

import com.pentspace.crowdfundingservice.entities.enums.Status;
import com.pentspace.crowdfundingservice.entities.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Transaction {
    private String id;
    private Status status;
    private TransactionType transactionType;
    private String sourceAccount;
    private String destinationAccount;
    private BigDecimal amount;
    private String otp;
    private String otpStatus;
    private String metaData;
}
