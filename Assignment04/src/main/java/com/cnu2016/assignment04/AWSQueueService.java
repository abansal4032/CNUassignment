package com.cnu2016.assignment04;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AWSQueueService {

    private AmazonSQSClient sqs = new AmazonSQSClient(new DefaultAWSCredentialsProviderChain());;

    public AWSQueueService() {
    }

    public void sendMessage(String message) {
        String myQueueUrl = sqs.getQueueUrl("archit_bansal_queue").getQueueUrl();
        sqs.sendMessage(new SendMessageRequest(myQueueUrl, message));
    }
}