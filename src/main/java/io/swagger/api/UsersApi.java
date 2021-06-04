/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.25).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.model.CreateUserPostBody;
import io.swagger.model.UpdateUserPutBody;
import io.swagger.model.User;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-31T10:47:35.905Z[GMT]")
@Validated
public interface UsersApi {

    @Operation(summary = "Get all users.", description = "", security = {
        @SecurityRequirement(name = "AuthToken")    }, tags={ "Employee" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "An array of all users.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class)))),
        
        @ApiResponse(responseCode = "401", description = "The current auth token does not provide access to this resource."),
        
        @ApiResponse(responseCode = "500", description = "An internal server error has occurred.") })
    @RequestMapping(value = "/users",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    ResponseEntity<List<User>> usersGet();


    @Operation(summary = "Create a new user.", description = "", security = {
        @SecurityRequirement(name = "AuthToken")    }, tags={ "Employee" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "User that was created.", content = @Content(schema = @Schema(implementation = User.class))),
        
        @ApiResponse(responseCode = "400", description = "The given input was not valid for this operation at this endpoint."),
        
        @ApiResponse(responseCode = "401", description = "The current auth token does not provide access to this resource."),
        
        @ApiResponse(responseCode = "500", description = "An internal server error has occurred.") })
    @RequestMapping(value = "/users",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    ResponseEntity<User> usersPost(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody CreateUserPostBody body);


    @Operation(summary = "Delete a specific user by ID.", description = "", security = {
        @SecurityRequirement(name = "AuthToken")    }, tags={ "Employee" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "204", description = "The user has been deleted."),
        
        @ApiResponse(responseCode = "400", description = "The given input was not valid for this operation at this endpoint."),
        
        @ApiResponse(responseCode = "401", description = "The current auth token does not provide access to this resource."),
        
        @ApiResponse(responseCode = "404", description = "Could not find an user with the given user ID."),
        
        @ApiResponse(responseCode = "500", description = "An internal server error has occurred.") })
    @RequestMapping(value = "/users/{userId}",
        method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    ResponseEntity<Void> usersUserIdDelete(@Parameter(in = ParameterIn.PATH, description = "The ID of an user.", required=true, schema=@Schema()) @PathVariable("userId") Integer userId) throws NotFoundException;


    @Operation(summary = "Get a specific user by ID.", description = "", security = {
        @SecurityRequirement(name = "AuthToken")    }, tags={ "Customer", "Employee" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Return the user with the given user ID.", content = @Content(schema = @Schema(implementation = User.class))),
        
        @ApiResponse(responseCode = "400", description = "The given input was not valid for this operation at this endpoint."),
        
        @ApiResponse(responseCode = "401", description = "The current auth token does not provide access to this resource."),
        
        @ApiResponse(responseCode = "404", description = "Could not find an user with the given user ID."),
        
        @ApiResponse(responseCode = "500", description = "An internal server error has occurred.") })
    @RequestMapping(value = "/users/{userId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<User> usersUserIdGet(@Parameter(in = ParameterIn.PATH, description = "The ID of an user.", required=true, schema=@Schema()) @PathVariable("userId") Integer userId);


    @Operation(summary = "Modify a specific user by ID.", description = "", security = {
        @SecurityRequirement(name = "AuthToken")    }, tags={ "Customer", "Employee" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "204", description = "User information has been updated."),
        
        @ApiResponse(responseCode = "400", description = "The given input was not valid for this operation at this endpoint."),
        
        @ApiResponse(responseCode = "401", description = "The current auth token does not provide access to this resource."),
        
        @ApiResponse(responseCode = "404", description = "Could not find an user with the given user ID."),
        
        @ApiResponse(responseCode = "500", description = "An internal server error has occurred.") })
    @RequestMapping(value = "/users/{userId}",
        consumes = { "application/json" }, 
        method = RequestMethod.PUT)
    ResponseEntity<Void> usersUserIdPut(@Parameter(in = ParameterIn.PATH, description = "The ID of an user.", required=true, schema=@Schema()) @PathVariable("userId") Integer userId, @Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody UpdateUserPutBody body) throws NotFoundException;

    @Operation(summary = "Returns user information about the logged in user.", description = "", security = {
            @SecurityRequirement(name = "AuthToken")    }, tags={ "Customer", "Employee" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User information for the JWT that was given."),

            @ApiResponse(responseCode = "400", description = "The given input was not valid for this operation at this endpoint."),

            @ApiResponse(responseCode = "401", description = "The current auth token does not provide access to this resource."),

            @ApiResponse(responseCode = "404", description = "Could not find an user with the given user ID."),

            @ApiResponse(responseCode = "500", description = "An internal server error has occurred.") })
    @RequestMapping(value = "/users/self",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<User> usersSelfGet();
}

