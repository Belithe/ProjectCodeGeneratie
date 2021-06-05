package io.swagger.repository;

import io.swagger.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findTransactionsByTransferToOrTransferFromOrderByTimestampDesc(String iban, String secondIban);

    List<Transaction> findTransactionByTransferFrom(String iban);

    List<Transaction> findAll();


}

