/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.25).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.model.Body;
import io.swagger.model.InlineResponse200;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CookieValue;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-12T14:50:34.731Z[GMT]")
@Validated
public interface LoginApi {

    @Operation(summary = "Returns a JWT used to make calls to protected endpoints.", description = "", tags={ "Authentication" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Returns a JWT.", content = @Content(schema = @Schema(implementation = InlineResponse200.class))),
        
        @ApiResponse(responseCode = "400", description = "The given input was not valid for this operation at this endpoint."),
        
        @ApiResponse(responseCode = "401", description = "The given password was not valid for the given email address."),
        
        @ApiResponse(responseCode = "404", description = "No user was found using the given email address."),
        
        @ApiResponse(responseCode = "500", description = "An internal server error has occurred.") })
    @RequestMapping(value = "/login",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<String> loginPost(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody Body body);

}

