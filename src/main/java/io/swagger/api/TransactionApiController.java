package io.swagger.api;

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

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-12T14:50:34.731Z[GMT]")
@RestController
public class TransactionApiController implements TransactionApi {

    private static final Logger log = LoggerFactory.getLogger(TransactionApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<List<Transaction>> transactionTransactionIdGet(@Parameter(in = ParameterIn.PATH, description = "The ID of the transaction.", required=true, schema=@Schema()) @PathVariable("transactionId") Integer transactionId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Transaction>>(objectMapper.readValue("[ {\n  \"userPerforming\" : {\n    \"key\" : {\n      \"firstName\" : \"Alice\",\n      \"lastName\" : \"Alixon\",\n      \"emailAddress\" : \"alice@example.com\",\n      \"role\" : \"customer\",\n      \"dayLimit\" : 0.8008282,\n      \"phone\" : \"+31 6 12345678\",\n      \"id\" : 42,\n      \"transactionLimit\" : 100,\n      \"birthDate\" : \"2000-05-29T00:00:00.000+00:00\"\n    }\n  },\n  \"transferFrom\" : \"NL02ABNA0123456789\",\n  \"transferTo\" : \"NL02ABNA0123456789\",\n  \"transactionId\" : 123567890,\n  \"timestamp\" : \"2021-01-01T08:00:01Z\"\n}, {\n  \"userPerforming\" : {\n    \"key\" : {\n      \"firstName\" : \"Alice\",\n      \"lastName\" : \"Alixon\",\n      \"emailAddress\" : \"alice@example.com\",\n      \"role\" : \"customer\",\n      \"dayLimit\" : 0.8008282,\n      \"phone\" : \"+31 6 12345678\",\n      \"id\" : 42,\n      \"transactionLimit\" : 100,\n      \"birthDate\" : \"2000-05-29T00:00:00.000+00:00\"\n    }\n  },\n  \"transferFrom\" : \"NL02ABNA0123456789\",\n  \"transferTo\" : \"NL02ABNA0123456789\",\n  \"transactionId\" : 123567890,\n  \"timestamp\" : \"2021-01-01T08:00:01Z\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Transaction>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Transaction>>(HttpStatus.NOT_IMPLEMENTED);
    }

}
