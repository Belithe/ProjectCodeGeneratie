package io.swagger.api;

import io.swagger.model.PostTransBody;
import io.swagger.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.User;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;

import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-06-02T14:07:43.568Z[GMT]")
@RestController
public class TransactionsApiController implements TransactionsApi {

    private static final Logger log = LoggerFactory.getLogger(TransactionsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    TransactionService transactionService;

    @Autowired
    UserService userService;

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    // get transaction for user
    public ResponseEntity<List<Transaction>> transactionsGet(@Min(0) @Max(1000) @Parameter(in = ParameterIn.QUERY, description = "The number of transactions to return." ,schema=@Schema(allowableValues={  }, minimum="0", maximum="1000"
            , defaultValue="100")) @Valid @RequestParam(value = "limit", required = false, defaultValue="20") Integer limit,@Min(0)@Parameter(in = ParameterIn.QUERY, description = "The page of transactions to return." ,schema=@Schema(allowableValues={  }, minimum="0"
            , defaultValue="0")) @Valid @RequestParam(value = "page", required = false, defaultValue="0") Integer page) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailAddress = authentication.getName();
        User loggedInUser = userService.getUserByEmailAddress(emailAddress);

        if (loggedInUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authentication token was given.");
        }

        List<Transaction> transactions = transactionService.getAllTransactions(limit, page, emailAddress);
        return new ResponseEntity<List<Transaction>>(transactions, HttpStatus.OK);
    }

    // get transactions by iban
    public ResponseEntity<List<Transaction>> transactionsIbanGet(@Parameter(in = ParameterIn.PATH, description = "Get all transaction by IBAN number.", required=true, schema=@Schema()) @PathVariable("iban") String iban) throws Exception {
        String accept = request.getHeader("Accept");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailAddress = authentication.getName();
        User loggedInUser = userService.getUserByEmailAddress(emailAddress);

        if (loggedInUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authentication token was given.");
        }

        List<Transaction> transactions = transactionService.getTransActionsByIBAN(emailAddress, iban);

        return new ResponseEntity<List<Transaction>>(transactions, HttpStatus.OK);
    }

    // post transaction, withdraw or deposit
    public ResponseEntity<Transaction> transactionsPost(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody PostTransBody body) throws Exception {
        String accept = request.getHeader("Accept");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailAddress = authentication.getName();
        User loggedInUser = userService.getUserByEmailAddress(emailAddress);

        if (loggedInUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authentication token was given.");
        }
        if (body.getTransactionType() == null ||
                body.getTransferFrom() == null ||
                body.getTransferTo() == null ||
                body.getAmount() == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "One of the input (type, transfer from, transfer to or amount) is missing.");

        Transaction transaction = transactionService.createTransaction(loggedInUser.getEmailAddress(), body);
        return new ResponseEntity<Transaction>(transaction, HttpStatus.OK);

    }
}
