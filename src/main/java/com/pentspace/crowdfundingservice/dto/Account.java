package com.pentspace.crowdfundingservice.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import java.math.BigDecimal;

@Data
public class Account {
    private String id;
    private String username;
    private BigDecimal balance;
    private String email;
}
