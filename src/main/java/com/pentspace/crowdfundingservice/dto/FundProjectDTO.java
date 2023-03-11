package com.pentspace.crowdfundingservice.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FundProjectDTO {
    @NotBlank(message = " ProjectId can not be empty")
    private String projectId;
    @NotBlank(message = " SourceAccountId can not be empty")
    private String sourceAccountId;
    @NotBlank(message = " Amount can not be empty")
    private String amount;
    @NotBlank(message = " Transaction Pin can not be empty")
    private String transactionPin;
}
