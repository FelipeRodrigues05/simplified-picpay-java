package com.nucleus.picpay.services;

import com.nucleus.picpay.domain.transaction.Transaction;
import com.nucleus.picpay.domain.user.User;
import com.nucleus.picpay.dto.TransactionDTO;
import com.nucleus.picpay.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${picpay.authorizationURL}")
    public String authorizationURL;

    public Transaction createTransaction(TransactionDTO transaction) throws Exception {
        User sender     = this.userService.findUserByDocument(transaction.senderDocument());
        User receiver   = this.userService.findUserByDocument(transaction.receiverDocument());

        userService.validateTransaction(sender, transaction.amount());

        boolean isAuthorized = this.authorizeTransaction(sender, transaction.amount());
        if(!isAuthorized) throw new Exception("Unauthorized transaction");

        this.saveUserBalance(sender, receiver, transaction.amount());

        return this.saveTransaction(sender, receiver, transaction.amount());
    }

    private boolean authorizeTransaction(User sender, BigDecimal amount) {
        ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity(this.authorizationURL, Map.class);

        if(authorizationResponse.getStatusCode() == HttpStatus.OK) {
            String message = (String) authorizationResponse.getBody().get("message");

           return "Autorizado".equalsIgnoreCase(message);
        }

        return false;
    }

    private Transaction saveTransaction(User sender, User receiver, BigDecimal amount) {
        Transaction newTransaction = new Transaction();
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setAmount(amount);
        newTransaction.setTimestamps(LocalDateTime.now());

        return this.transactionRepository.save(newTransaction);
    }

    private void saveUserBalance(User sender, User receiver, BigDecimal amount) {
        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);
    }
}
