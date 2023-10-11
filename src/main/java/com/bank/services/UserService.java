package com.bank.services;

import java.math.BigDecimal;
import java.util.List;

import com.bank.dtos.UserDTO;
import org.springframework.stereotype.Service;

import com.bank.domain.user.User;
import com.bank.domain.user.UserType;
import com.bank.repositories.UserRepository;



@Service
public class UserService {
	private UserRepository repo;

	public UserService(UserRepository repo) {
		this.repo = repo;
	}

	public void validateTransaction(User sender, BigDecimal amount) throws Exception{
		if(sender.getUserType() == UserType.MERCHANT){
			throw new Exception("Usuários do tipo lojista não estão autorizados a fazer transações ");
		}
		
		if(sender.getBalance().compareTo(amount) < 0) {
			throw new Exception("Saldo insuficiente");
		}
	}
	
	public User findUserById(Long id) throws Exception{
		return this.repo.findUserById(id).orElseThrow(() -> new Exception("Usuário não encontrado"));
	}

	public User createUser(UserDTO data){
		User newUser = new User(data);
		this.saveUser(newUser);
		return newUser;
	}

	public List<User> getAllUsers(){
		return this.repo.findAll();
	}

	public void saveUser(User user) {
		this.repo.save(user);
	}
}
