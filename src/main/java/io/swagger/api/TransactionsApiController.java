//package io.swagger.api;
//
//import io.swagger.model.Body5;
//import io.swagger.model.Transaction;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.swagger.model.User;
//import io.swagger.model.UserRole;
//import io.swagger.service.TransactionService;
//import io.swagger.service.UserService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.enums.ParameterIn;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.media.ArraySchema;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.CookieValue;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RequestPart;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.server.ResponseStatusException;
//
//import javax.validation.constraints.*;
//import javax.validation.Valid;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//
//@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-06-02T14:07:43.568Z[GMT]")
//@RestController
//public class TransactionsApiController implements TransactionsApi {
//
//    private static final Logger log = LoggerFactory.getLogger(TransactionsApiController.class);
//
//    private final ObjectMapper objectMapper;
//
//    private final HttpServletRequest request;
//
//    @Autowired
//    TransactionService transactionService;
//
//    @Autowired
//    UserService userService;
//
//    @org.springframework.beans.factory.annotation.Autowired
//    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
//        this.objectMapper = objectMapper;
//        this.request = request;
//    }
//
//    // get transaction for user
////    public ResponseEntity<List<Transaction>> transactionsGet(@Min(1) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "The number of transactions to return." ,schema=@Schema(allowableValues={  }, minimum="1", maximum="50"
////            , defaultValue="100")) @Valid @RequestParam(value = "limit", required = false, defaultValue="20") Integer limit,@Min(1)@Parameter(in = ParameterIn.QUERY, description = "The page of transactions to return." ,schema=@Schema(allowableValues={  }, minimum="1"
////            , defaultValue="1")) @Valid @RequestParam(value = "page", required = false, defaultValue="1") Integer page) {
////
////        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////        String emailAddress = authentication.getName();
////        User loggedInUser = userService.getUserByEmailAddress(emailAddress);
////
////        if (loggedInUser == null) {
////            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authentication token was given.");
////        }
////
////        List<Transaction> transactions = transactionService.getAllTransactions(limit, page, emailAddress);
////        return new ResponseEntity<List<Transaction>>(transactions, HttpStatus.OK);
////    }
////
////    // get transactions by iban
////    public ResponseEntity<List<Transaction>> transactionsIbanGet(@Parameter(in = ParameterIn.PATH, description = "Get all transaction by IBAN number.", required=true, schema=@Schema()) @PathVariable("iban") String iban) {
////        String accept = request.getHeader("Accept");
////        if (accept != null && accept.contains("application/json")) {
////            try {
////                return new ResponseEntity<List<Transaction>>(objectMapper.readValue("[ {\n  \"amount\" : 9000.01,\n  \"userPerforming\" : 42,\n  \"transferFrom\" : \"NL02ABNA0123456789\",\n  \"transferTo\" : \"NL02ABNA0123456789\",\n  \"type\" : \"transfer\",\n  \"transactionId\" : 123567890,\n  \"timestamp\" : \"2021-01-01T08:00:01Z\"\n}, {\n  \"amount\" : 9000.01,\n  \"userPerforming\" : 42,\n  \"transferFrom\" : \"NL02ABNA0123456789\",\n  \"transferTo\" : \"NL02ABNA0123456789\",\n  \"type\" : \"transfer\",\n  \"transactionId\" : 123567890,\n  \"timestamp\" : \"2021-01-01T08:00:01Z\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
////            } catch (IOException e) {
////                log.error("Couldn't serialize response for content type application/json", e);
////                return new ResponseEntity<List<Transaction>>(HttpStatus.INTERNAL_SERVER_ERROR);
////            }
////        }
////
////        return new ResponseEntity<List<Transaction>>(HttpStatus.NOT_IMPLEMENTED);
////    }
////
////    // post transaction, withdraw or deposit
////    public ResponseEntity<Transaction> transactionsPost(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody Body5 body) {
////        String accept = request.getHeader("Accept");
////        if (accept != null && accept.contains("application/json")) {
////            try {
////                return new ResponseEntity<Transaction>(objectMapper.readValue("{\n  \"amount\" : 9000.01,\n  \"userPerforming\" : 42,\n  \"transferFrom\" : \"NL02ABNA0123456789\",\n  \"transferTo\" : \"NL02ABNA0123456789\",\n  \"type\" : \"transfer\",\n  \"transactionId\" : 123567890,\n  \"timestamp\" : \"2021-01-01T08:00:01Z\"\n}", Transaction.class), HttpStatus.NOT_IMPLEMENTED);
////            } catch (IOException e) {
////                log.error("Couldn't serialize response for content type application/json", e);
////                return new ResponseEntity<Transaction>(HttpStatus.INTERNAL_SERVER_ERROR);
////            }
////        }
////
////        return new ResponseEntity<Transaction>(HttpStatus.NOT_IMPLEMENTED);
////    }
//}
