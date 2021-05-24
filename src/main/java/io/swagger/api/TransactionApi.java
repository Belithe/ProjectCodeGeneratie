/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.25).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.model.Transaction;
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
public interface TransactionApi {

    @Operation(summary = "Get single transactions by transaction ID.", description = "", security = {
        @SecurityRequirement(name = "AuthToken")    }, tags={ "Customer", "Employee" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Transaction by ID.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Transaction.class)))),
        
        @ApiResponse(responseCode = "400", description = "The given input was not valid for this operation at this endpoint."),
        
        @ApiResponse(responseCode = "401", description = "The current auth token does not provide access to this resource."),
        
        @ApiResponse(responseCode = "404", description = "Could not find a transaction with the given transaction ID."),
        
        @ApiResponse(responseCode = "500", description = "An internal server error has occurred.") })
    @RequestMapping(value = "/transaction/{transactionId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<Transaction>> transactionTransactionIdGet(@Parameter(in = ParameterIn.PATH, description = "The ID of the transaction.", required=true, schema=@Schema()) @PathVariable("transactionId") Integer transactionId);

}
