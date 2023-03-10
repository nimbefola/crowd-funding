package com.pentspace.crowdfundingservice.service.impl;

import com.pentspace.crowdfundingservice.clients.AccountServiceClient;
import com.pentspace.crowdfundingservice.clients.EmailServiceClient;
import com.pentspace.crowdfundingservice.clients.TransactionServiceClient;
import com.pentspace.crowdfundingservice.dto.Account;
import com.pentspace.crowdfundingservice.dto.Transaction;
import com.pentspace.crowdfundingservice.entities.Project;
import com.pentspace.crowdfundingservice.entities.enums.Status;
import com.pentspace.crowdfundingservice.entities.enums.TransactionType;
import com.pentspace.crowdfundingservice.entities.repositories.ProjectRepository;
import com.pentspace.crowdfundingservice.service.FileUploadService;
import com.pentspace.crowdfundingservice.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

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
    private EmailServiceClient emailServiceClient;
    @Override
    public Project createProject(Project project) {
        project.setStatus(Status.PENDING);
        return projectRepository.save(project);
    }

    @Override
    public String fundProject(String projectId, String sourceAccount, String amount) {
        try{
            Account account = accountServiceClient.getAccount(sourceAccount);
            if(account.getBalance().compareTo(new BigDecimal(amount)) < 0 ){
                throw new RuntimeException("Account balance is lesser than amount");
            }
            String debitResponse = accountServiceClient.debitBalance(sourceAccount, amount);
            if(debitResponse.equalsIgnoreCase("Successful")){
                Project project = getById(projectId);
                log.info(" Project initial contribution [{}]", project.getAmountContributed());
                project.setAmountContributed(project.getAmountContributed().add(new BigDecimal(amount)));
                projectRepository.save(project);
                log.info(" Current contribution [{}]", project.getAmountContributed());
                Transaction transaction = prepareTransaction(projectId, sourceAccount, amount);
                transaction = transactionServiceClient.create(transaction);
                if(Objects.isNull(transaction.getId())){
                    return "Failed";
                }
                emailServiceClient.sendEmail(account.getEmail(), transaction.getOtp(), " PROJECT FUNDING OTP ");
                return "Successful";
            }else{
                return "Failed";
            }
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
        transaction.setStatus(Status.PENDING);
        transaction.setSourceAccount(sourceAccount);
        return transaction;
    }
}
