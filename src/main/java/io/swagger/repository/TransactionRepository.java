package io.swagger.repository;

import io.swagger.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("SELECT t FROM Transaction t WHERE t.transferFrom =:iban OR t.transferTo =:iban ORDER BY t.timestamp DESC")
    List<Transaction> findByIban(@Param("iban") String iban);

    List<Transaction> findTransactionByTransferFrom(String iban);

    List<Transaction> findAll();


}

