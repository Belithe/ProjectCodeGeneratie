package io.swagger.api;

import io.swagger.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.service.AccountManagementService;
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
import java.util.stream.Collectors;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-06-02T13:47:48.293Z[GMT]")
@RestController
public class AccountsApiController implements AccountsApi {

    private static final Logger log = LoggerFactory.getLogger(AccountsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    AccountManagementService accountService;

    @Autowired
    UserService userService;


    @org.springframework.beans.factory.annotation.Autowired
    public AccountsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Account> createAccount(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody CreateAccountPostBody body) throws ResponseStatusException {
        if(getLoggedInUser().getRole().contains(UserRole.EMPLOYEE)){
            accountService.createNewAccount(body);
            return new ResponseEntity<Account>(HttpStatus.CREATED);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The current auth token does not provide access to this resource.");
        }
    }

    public ResponseEntity<Void> deleteAccount(@Size(min=18,max=18) @Parameter(in = ParameterIn.PATH, description = "The IBAN of the to be deleted account, which must be 18 characters long.", required=true, schema=@Schema()) @PathVariable("iban") String iban) throws ResponseStatusException {
        if(getLoggedInUser().getRole().contains(UserRole.EMPLOYEE) && !iban.equals("NL01INHO0000000001")){
            accountService.deleteSingleAccount(iban);
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The current auth token does not provide access to this resource.");
        }
    }

    public ResponseEntity<List<Account>> getAccounts(@Min(1) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "The number of transactions to return." ,schema=@Schema(allowableValues={  }, minimum="1", maximum="50"
            , defaultValue="100")) @Valid @RequestParam(value = "limit", required = false, defaultValue="20") Integer limit,@Min(1)@Parameter(in = ParameterIn.QUERY, description = "The page of transactions to return." ,schema=@Schema(allowableValues={  }, minimum="1"
            , defaultValue="1")) @Valid @RequestParam(value = "page", required = false, defaultValue="1") Integer page, @Parameter(in = ParameterIn.QUERY, description = "Current user's id.", required=false, schema=@Schema()) Integer userId) {
        if(userId != null) {
            if(getLoggedInUser().getRole().contains(UserRole.EMPLOYEE) || userId == getLoggedInUser().getId()) {
                List<Account> accounts = accountService.getAllAccountsById(userId);

                page -= 1;
                int skip = limit * page;
                accounts = accounts.stream()
                        .skip(skip)
                        .limit(limit)
                        .collect(Collectors.toList());

                return new ResponseEntity<List<Account>>(accounts, HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The current auth token does not provide access to this resource.");
            }
        } else {
            if (getLoggedInUser().getRole().contains(UserRole.EMPLOYEE)) {
                List<Account> accounts = accountService.getAllAccounts();

                page -= 1;
                int skip = limit * page;
                accounts = accounts.stream()
                        .filter(account -> account.getIBAN() != "NL01INHO0000000001")
                        .skip(skip)
                        .limit(limit)
                        .collect(Collectors.toList());

//                if (page == 0) {
//                    //remove bank account from getAll
//                    accounts.remove(0);
//                }

                return new ResponseEntity<List<Account>>(accounts, HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The current auth token does not provide access to this resource.");
            }
        }
    }

    public ResponseEntity<Account> getAccountsByIBAN(@Parameter(in = ParameterIn.PATH, description = "The IBAN of the account to get.", required=true, schema=@Schema()) @PathVariable("iban") String iban) {
        Account accountRequested = accountService .getByIBAN(iban);

        if((getLoggedInUser().getRole().contains(UserRole.EMPLOYEE) || accountRequested.getUserId() == getLoggedInUser().getId()) && !iban.equals("NL01INHO0000000001")) {
            return new ResponseEntity<Account>(accountRequested, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The current auth token does not provide access to this resource.");
        }
    }

    public ResponseEntity<Void> regularEditAccount(@Parameter(in = ParameterIn.PATH, description = "The IBAN of the account to edit", required=true, schema=@Schema()) @PathVariable("iban") String iban,@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody UpdateAccountPutBody body) {
        if (getLoggedInUser().getRole().contains(UserRole.EMPLOYEE) && !iban.equals("NL01INHO0000000001")) {
            accountService.updateExistingAccount(iban, body);
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The current auth token does not provide access to this resource.");
        }
    }


    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String emailAddress = authentication.getName();
        User loggedInUser = userService.getUserByEmailAddress(emailAddress);
        if (loggedInUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authentication token was given.");
        }

        return loggedInUser;
    }
}
