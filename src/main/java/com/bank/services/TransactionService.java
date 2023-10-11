package com.bank.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bank.domain.transaction.Transaction;
import com.bank.domain.user.User;
import com.bank.dtos.TransactionDTO;
import com.bank.repositories.TransactionRepository;

@Service
public class TransactionService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TransactionRepository repo;
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private NotificationService notificationService;
	public Transaction createTransaction(TransactionDTO transaction) throws Exception{
		User sender = this.userService.findUserById(transaction.senderId());
		User receiver = this.userService.findUserById(transaction.receiverId());
		
		userService.validateTransaction(sender, transaction.value());
		
		boolean isAuthorized = this.authorizeTransaction(sender, transaction.value());
		if(!isAuthorized){
			throw new Exception("Transação não autorizada");
		}
		
		Transaction newTransaction = new Transaction();	
		newTransaction.setAmount(transaction.value());
		newTransaction.setSender(sender);
		newTransaction.setReceiver(receiver);
		newTransaction.setTimestamp(LocalDateTime.now());
		
		sender.setBalance(sender.getBalance().subtract(transaction.value()));
		receiver.setBalance(receiver.getBalance().add(transaction.value()));
		
		this.repo.save(newTransaction);
		this.userService.saveUser(sender);
		this.userService.saveUser(receiver);

		this.notificationService.sendNotification(sender, "Transação enviada com sucesso");

		this.notificationService.sendNotification(receiver, "Transação recebida com sucesso");

		return newTransaction;
		}
	
	public boolean authorizeTransaction(User sender, BigDecimal value){
		ResponseEntity<Map> authorizationResponse = restTemplate
				.getForEntity("https://run.mocky.io/v3/0300a41e-5c75-4ece-b99d-7c1af4e45399", Map.class);
				if(authorizationResponse.getStatusCode() == HttpStatus.OK) {
					String message = (String) authorizationResponse.getBody().get("message");
					return "Autorizado".equalsIgnoreCase(message);
				}else return false;
	}
}
