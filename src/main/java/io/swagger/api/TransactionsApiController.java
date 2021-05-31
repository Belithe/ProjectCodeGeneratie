package io.swagger.api;

import io.swagger.model.Body4;
import io.swagger.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-31T10:47:35.905Z[GMT]")
@RestController
public class TransactionsApiController implements TransactionsApi {

    private static final Logger log = LoggerFactory.getLogger(TransactionsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<List<Transaction>> transactionsGet(@Min(1) @Max(1000) @Parameter(in = ParameterIn.QUERY, description = "The number of transactions to return." ,schema=@Schema(allowableValues={  }, minimum="1", maximum="1000"
, defaultValue="100")) @Valid @RequestParam(value = "limit", required = false, defaultValue="100") Integer limit,@Min(1)@Parameter(in = ParameterIn.QUERY, description = "The page of transactions to return." ,schema=@Schema(allowableValues={  }, minimum="1"
, defaultValue="1")) @Valid @RequestParam(value = "page", required = false, defaultValue="1") Integer page) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Transaction>>(objectMapper.readValue("[ {\n  \"amount\" : 9000.01,\n  \"userPerforming\" : 42,\n  \"transferFrom\" : \"NL02ABNA0123456789\",\n  \"transferTo\" : \"NL02ABNA0123456789\",\n  \"transactionId\" : 123567890,\n  \"timestamp\" : \"2021-01-01T08:00:01Z\"\n}, {\n  \"amount\" : 9000.01,\n  \"userPerforming\" : 42,\n  \"transferFrom\" : \"NL02ABNA0123456789\",\n  \"transferTo\" : \"NL02ABNA0123456789\",\n  \"transactionId\" : 123567890,\n  \"timestamp\" : \"2021-01-01T08:00:01Z\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Transaction>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Transaction>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<Transaction>> transactionsIbanGet(@Parameter(in = ParameterIn.PATH, description = "Get all transaction by IBAN number.", required=true, schema=@Schema()) @PathVariable("iban") String iban) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Transaction>>(objectMapper.readValue("[ {\n  \"amount\" : 9000.01,\n  \"userPerforming\" : 42,\n  \"transferFrom\" : \"NL02ABNA0123456789\",\n  \"transferTo\" : \"NL02ABNA0123456789\",\n  \"transactionId\" : 123567890,\n  \"timestamp\" : \"2021-01-01T08:00:01Z\"\n}, {\n  \"amount\" : 9000.01,\n  \"userPerforming\" : 42,\n  \"transferFrom\" : \"NL02ABNA0123456789\",\n  \"transferTo\" : \"NL02ABNA0123456789\",\n  \"transactionId\" : 123567890,\n  \"timestamp\" : \"2021-01-01T08:00:01Z\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Transaction>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Transaction>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Transaction> transactionsPost(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody Body4 body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Transaction>(objectMapper.readValue("{\n  \"amount\" : 9000.01,\n  \"userPerforming\" : 42,\n  \"transferFrom\" : \"NL02ABNA0123456789\",\n  \"transferTo\" : \"NL02ABNA0123456789\",\n  \"transactionId\" : 123567890,\n  \"timestamp\" : \"2021-01-01T08:00:01Z\"\n}", Transaction.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Transaction>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Transaction>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Transaction> transactionsTransactionIdGet(@Parameter(in = ParameterIn.PATH, description = "The ID of the transaction.", required=true, schema=@Schema()) @PathVariable("transactionId") Integer transactionId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Transaction>(objectMapper.readValue("{\n  \"amount\" : 9000.01,\n  \"userPerforming\" : 42,\n  \"transferFrom\" : \"NL02ABNA0123456789\",\n  \"transferTo\" : \"NL02ABNA0123456789\",\n  \"transactionId\" : 123567890,\n  \"timestamp\" : \"2021-01-01T08:00:01Z\"\n}", Transaction.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Transaction>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Transaction>(HttpStatus.NOT_IMPLEMENTED);
    }

}
