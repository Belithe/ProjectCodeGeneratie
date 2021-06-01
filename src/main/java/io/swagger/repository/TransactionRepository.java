package io.swagger.repository;

import io.swagger.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Transaction findTransactionByTransferTo(String transferTo);
    //    @Transactional
//    @Modifying
//    @Query("select transaction from Transaction user where transaction.IBAN =:IBAN")
//    Transaction findTransactionByIBAN(@Param("lastName") String lastName);
//
//    @Query("select transaction from Transaction transaction where transaction.transactionId =:transactionId")
//    Transaction findById(@Param("id") Integer id);
}

