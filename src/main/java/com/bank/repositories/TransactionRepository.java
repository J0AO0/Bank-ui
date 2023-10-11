package com.bank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.domain.transaction.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
