package com.pentspace.crowdfundingservice.entities;

import com.pentspace.crowdfundingservice.entities.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Project extends Base {
    private String accountId;
    private String title;
    private String description;
    private String supportingImageUrl;
    private BigDecimal amountExpected;
    private BigDecimal amountContributed;
    private LocalDate endDate;
    @Enumerated(EnumType.STRING)
    private Status status;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmountExpected() {
        return amountExpected;
    }

    public void setAmountExpected(BigDecimal amountExpected) {
        this.amountExpected = amountExpected;
    }

    public BigDecimal getAmountContributed() {
        return amountContributed;
    }

    public void setAmountContributed(BigDecimal amountContributed) {
        this.amountContributed = amountContributed;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getSupportingImageUrl() {
        return supportingImageUrl;
    }

    public void setSupportingImageUrl(String supportingImageUrl) {
        this.supportingImageUrl = supportingImageUrl;
    }
}
