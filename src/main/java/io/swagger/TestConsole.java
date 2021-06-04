package io.swagger;

import io.swagger.model.*;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.threeten.bp.LocalDate;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;
import springfox.documentation.oas.annotations.EnableOpenApi;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@EnableOpenApi
@ComponentScan(basePackages = { "io.swagger", "io.swagger.api" , "io.swagger.configuration"})
public class TestConsole implements CommandLineRunner {

    @Autowired
    UserService userService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountRepository accountRepository;

    @Override
    public void run(String ...args) throws Exception {
        addTransAction();


//        User user = new User();
//        user.setBirthDate(LocalDate.of(2000, 1, 1));
//        user.setDayLimit(50f);
//        user.setEmailAddress("test@test.com");
//        user.setLastName("Alixon");
//        user.setFirstName("Alice");
//        user.setPhone("+31 6 12345678");
//        user.setRole(Collections.singletonList(UserRole.CUSTOMER));
//        user.setId(6);
//        user.setPassword("idk");
//        user.setTransactionLimit(new BigDecimal(20));
//        userService.add(user);
//
//        Account account = new Account();
//        account.setBalance(1000f);
//        account.setAccountType(AccountType.CURRENT);
//        account.setIBAN("NL01INHO0000000002");
//        account.setMinimumLimit(50f);
//        account.setUserId(6);
//        accountRepository.save(account);
//
//        Account accountsave = new Account();
//        accountsave.setBalance(1000f);
//        accountsave.setAccountType(AccountType.SAVING);
//        accountsave.setIBAN("NL01INHO0000000004");
//        accountsave.setMinimumLimit(50f);
//        accountsave.setUserId(6);
//        accountRepository.save(accountsave);

        // 400 BAD_REQUEST "The balance would fall below the minimum limit defined by user, change the limit or amount of the transaction."
//        PostTransBody postTran = new PostTransBody();
//        postTran.setAmount(1050f);
//        postTran.setTransactionType(TransactionType.TRANSFER);
//        postTran.setTransferFrom("NL01INHO0000000002");
//        postTran.setTransferTo("NL01INHO0000000004");

        // 403 FORBIDDEN "You cannot transfer from other account other then your own."
//        PostTransBody postTran = new PostTransBody();
//        postTran.setAmount(1010f);
//        postTran.setTransactionType(TransactionType.TRANSFER);
//        postTran.setTransferFrom("NL01INHO0000000004");
//        postTran.setTransferTo("NL01INHO0000000010");

        // 400 BAD_REQUEST "The balance would fall below the minimum limit defined by user, change the limit or amount of the transaction."
//        PostTransBody postTran = new PostTransBody();
//        postTran.setAmount(1010f);
//        postTran.setTransactionType(TransactionType.TRANSFER);
//        postTran.setTransferFrom("NL01INHO0000000002");
//        postTran.setTransferTo("NL01INHO0000000010");

        // 400 BAD_REQUEST "The amount of the transaction overseeded the limit of a transaction defined by the user, change the limit or amount of the transaction."
//        PostTransBody postTran = new PostTransBody();
//        postTran.setAmount(50f);
//        postTran.setTransactionType(TransactionType.TRANSFER);
//        postTran.setTransferFrom("NL01INHO0000000002");
//        postTran.setTransferTo("NL01INHO0000000004");

//        PostTransBody postTran = new PostTransBody();
//        postTran.setAmount(50f);
//        postTran.setTransactionType(TransactionType.TRANSFER);
//        postTran.setTransferFrom("NL01INHO0000000002");
//        postTran.setTransferTo("NL01INHO0000000004");
//
//        PostTransBody postWith = new PostTransBody();
//        postWith.setAmount(50f);
//        postWith.setTransactionType(TransactionType.WITHDRAW);
//        postWith.setTransferFrom("NL01INHO0000000002");
//        postWith.setTransferTo("");
//
//        PostTransBody postDrop = new PostTransBody();
//        postDrop.setAmount(50f);
//        postDrop.setTransactionType(TransactionType.DEPOSIT);
//        postDrop.setTransferFrom("");
//        postDrop.setTransferTo("NL01INHO0000000002");
//
//        Transaction postTrans = transactionService.createTransaction(user.getEmailAddress(),postTran);
//        System.out.println(postTrans.toString());
//
//
//        Transaction postTransWith = transactionService.createTransaction(user.getEmailAddress(),postWith);
//        System.out.println(postTransWith.toString());
//
//        Transaction postTransDe = transactionService.createTransaction(user.getEmailAddress(),postDrop);
//        System.out.println(postTransDe.toString());
//
//        System.out.println("---- list get all ----");
//
//        List<Transaction> transactions = transactionService.getAllTransactions(1,50, user.getEmailAddress());
//        for (Transaction t : transactions) {
//            System.out.println(t.toString());
//        }
//        System.out.println("---- list by iban all ----");
//
//        List<Transaction> transactionsIBAN = transactionService.getTransActionsByIBAN(user.getEmailAddress(), "NL01INHO0000000002");
//        for (Transaction t : transactionsIBAN) {
//            System.out.println(t.toString());
//        }


    }


    private void addTransAction() {
        Transaction transaction = new Transaction();
        transaction.setUserPerforming(6);
        transaction.setTimestamp(OffsetDateTime.of(2020, 01, 01, 10, 10, 10, 0, ZoneOffset.UTC));
        transaction.setTransferTo("NL01INHO0000000002");
        transaction.setTransferFrom("NL01INHO0000000003");
        transaction.setAmount((float) 50);
        transaction.setType(TransactionType.TRANSFER);
        transactionRepository.save(transaction);

        Transaction transaction2 = new Transaction();
        transaction2.setUserPerforming(1);
        transaction2.setTimestamp(OffsetDateTime.of(2020, 01, 01, 10, 10, 10, 0, ZoneOffset.UTC));
        transaction2.setTransferTo("NL01INHO0000000003");
        transaction2.setTransferFrom("NL01INHO0000000002");
        transaction2.setAmount((float) 50);
        transaction2.setType(TransactionType.TRANSFER);
        transactionRepository.save(transaction2);
    }

    public static void main(String[] args) throws Exception {
        new SpringApplication(TestConsole.class).run(args);
    }

    class ExitException extends RuntimeException implements ExitCodeGenerator {
        private static final long serialVersionUID = 1L;

        @Override
        public int getExitCode() {
            return 10;
        }

    }
}
