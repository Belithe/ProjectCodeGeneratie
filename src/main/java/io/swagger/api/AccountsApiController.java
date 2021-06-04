package io.swagger.api;

import io.swagger.model.Account;
import io.swagger.model.CreateAccountPostBody;
import io.swagger.model.UpdateAccountPutBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.User;
import io.swagger.service.AccountManagementService;
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
import org.springframework.beans.factory.annotation.Autowired;
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

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-06-02T13:47:48.293Z[GMT]")
@RestController
public class AccountsApiController implements AccountsApi {

    private static final Logger log = LoggerFactory.getLogger(AccountsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    AccountManagementService accountService;

    @org.springframework.beans.factory.annotation.Autowired
    public AccountsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Account> createAccount(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody CreateAccountPostBody body) {
        accountService.createNewAccount(body);
        return new ResponseEntity<Account>(HttpStatus.OK);
    }

    public ResponseEntity<Void> deleteAccount(@Size(min=18,max=18) @Parameter(in = ParameterIn.PATH, description = "The IBAN of the to be deleted account, which must be 18 characters long.", required=true, schema=@Schema()) @PathVariable("iban") String iban) throws NotFoundException {
        accountService.deleteSingleAccount(iban);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<List<Account>> getAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return new ResponseEntity<List<Account>>(accounts, HttpStatus.OK);
    }

    public ResponseEntity<Account> getUserAccounts(@Parameter(in = ParameterIn.PATH, description = "The IBAN of the account to get.", required=true, schema=@Schema()) @PathVariable("iban") String iban) {
        Account accountRequested = accountService.getByIBAN(iban);
        return new ResponseEntity<Account>(accountRequested, HttpStatus.OK);
    }

    public ResponseEntity<Void> regularEditAccount(@Parameter(in = ParameterIn.PATH, description = "The IBAN of the account to edit", required=true, schema=@Schema()) @PathVariable("iban") String iban,@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody UpdateAccountPutBody body) {
        accountService.updateExistingAccount(iban, body);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
