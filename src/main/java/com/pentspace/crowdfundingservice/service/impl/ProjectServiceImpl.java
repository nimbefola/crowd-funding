package com.pentspace.crowdfundingservice.service.impl;

import com.pentspace.crowdfundingservice.clients.AccountServiceClient;
import com.pentspace.crowdfundingservice.clients.TransactionServiceClient;
import com.pentspace.crowdfundingservice.dto.Account;
import com.pentspace.crowdfundingservice.dto.FundProjectDTO;
import com.pentspace.crowdfundingservice.dto.Transaction;
import com.pentspace.crowdfundingservice.entities.Project;
import com.pentspace.crowdfundingservice.entities.enums.Status;
import com.pentspace.crowdfundingservice.entities.enums.TransactionSource;
import com.pentspace.crowdfundingservice.entities.enums.TransactionType;
import com.pentspace.crowdfundingservice.entities.repositories.ProjectRepository;
import com.pentspace.crowdfundingservice.service.FileUploadService;
import com.pentspace.crowdfundingservice.service.HashManagerHandler;
import com.pentspace.crowdfundingservice.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private TransactionServiceClient transactionServiceClient;
    @Autowired
    private AccountServiceClient accountServiceClient;
    @Autowired
    private HashManagerHandler hashManagerHandler;

    @Override
    public Project createProject(Project project) {
        project.setStatus(Status.PENDING);
        return projectRepository.save(project);
    }

    @Override
    public String fundProject(FundProjectDTO fundProjectDTO) {
        try{
            Project project = getById(fundProjectDTO.getProjectId());
            Account sourceAccount = accountServiceClient.getAccount(fundProjectDTO.getSourceAccountId());
            Account beneficiaryAccount = accountServiceClient.getAccount(project.getAccountId());
            if(sourceAccount.getBalance().compareTo(new BigDecimal(fundProjectDTO.getAmount())) < 0 ){
                throw new RuntimeException("Account balance is lesser than amount");
            }
            validatePin(sourceAccount.getPin(), fundProjectDTO.getTransactionPin());
            Transaction transaction = prepareTransaction(fundProjectDTO.getProjectId(), fundProjectDTO.getSourceAccountId(), fundProjectDTO.getAmount());
            transaction = transactionServiceClient.create(transaction);
            if(Objects.isNull(transaction.getId())){
                return "Failed";
            }
            log.info(" Contributing to Project [{}] with initial contribution [{}]", project.getId(), project.getAmountContributed());
            project.setAmountContributed(project.getAmountContributed().add(new BigDecimal(fundProjectDTO.getAmount())));
            projectRepository.save(project);
            log.info(" Current contribution [{}]", project.getAmountContributed());
            sourceAccount.setBalance(sourceAccount.getBalance().subtract(new BigDecimal(fundProjectDTO.getAmount())));
            beneficiaryAccount.setBalance(beneficiaryAccount.getBalance().add(new BigDecimal(fundProjectDTO.getAmount())));
            List<Account> accounts = new ArrayList<>(Arrays.asList(sourceAccount, beneficiaryAccount));
            accountServiceClient.updateBalances(accounts);
            return "Successful";
        }catch (Exception exception){
            log.error(" An error occurred [{}]", exception.getMessage(), exception);
            return "Failed";
        }
    }

    @Override
    public Project getById(String id) {
        Project project = projectRepository.findById(id).orElseThrow(()->new NoSuchElementException("Project not found"));
        project.setProjectBase64Image(fileUploadService.readAndConvertImageToBase64Read(project.getId()));
        return project;
    }

    @Override
    public List<Project> getByAccountId(String id) {
        return projectRepository.findByAccountId(id);
    }

    @Override
    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    @Override
    public Project updateStatus(String id, String status) {
        Project project = getById(id);
        project.setStatus(Status.valueOf(status));
        return projectRepository.save(project);
    }

    @Override
    public Project uploadProjectPicture(String id, MultipartFile multipartFile) {
        Project project = getById(id);
        project.setSupportingImageUrl(fileUploadService.uploadFile(id, multipartFile));
        return projectRepository.save(project);
    }

    private Transaction prepareTransaction(String projectId, String sourceAccount, String amount){
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.DONATION);
        transaction.setAmount(new BigDecimal(amount));
        transaction.setDestinationAccount(projectId);
        transaction.setStatus(Status.COMPLETED);
        transaction.setSourceAccount(sourceAccount);
        transaction.setTransactionSource(TransactionSource.PROJECT);
        return transaction;
    }

    public void validatePin(String accountPin, String transactionPin){
        if(!hashManagerHandler.validateData(transactionPin, accountPin)){
            throw new RuntimeException("Invalid Pin");
        }
    }

}
