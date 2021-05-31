package io.swagger.api;

import io.swagger.model.Body;
import io.swagger.model.dto.LoginResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-12T14:50:34.731Z[GMT]")
@RestController
public class LoginApiController implements LoginApi {

    @Autowired
    UserService userService;

    private static final Logger log = LoggerFactory.getLogger(LoginApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public LoginApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<LoginResponseDTO> loginPost(@Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody Body body) {
//        try {
        String token = userService.login(body.getEmailAddress(), body.getPassword());
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setAuthToken(token);


        //return new ResponseEntity<String>(userService.login(body.getEmailAddress(), body.getPassword()), HttpStatus.OK);
         return new ResponseEntity<LoginResponseDTO>(loginResponseDTO, HttpStatus.OK);
//        } catch (Exception e) {
//            if (e.toString().contains("Login failed")) {
//                return new ResponseEntity<LoginResponseDTO>(HttpStatus.UNPROCESSABLE_ENTITY);
//                //throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Login failed.");
//            }
//
//            log.error("Couldn't serialize response for content type application/json", e);
//            return new ResponseEntity<LoginResponseDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
    }
}

