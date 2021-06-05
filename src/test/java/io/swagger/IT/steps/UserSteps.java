package io.swagger.IT.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.UpdateUserPutBody;
import io.swagger.model.dto.ExceptionDTO;
import io.swagger.model.dto.LoginDTO;
import io.swagger.model.dto.LoginResponseDTO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class UserSteps {
    private final String baseUrl = "http://localhost:5013";
    private RestTemplate restTemplate = new RestTemplate();
    private HttpClientErrorException httpClientErrorException;
    private ObjectMapper objectMapper = new ObjectMapper();
    private ResponseEntity<String> stringResponse;

    private String getJwtToken(String emailAddress, String password) throws URISyntaxException, JsonProcessingException {
        // Create request
        URI uri = new URI(baseUrl + "/login");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        // Create body
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmailAddress(emailAddress);
        loginDTO.setPassword(password);

        String requestBody = objectMapper.writeValueAsString(loginDTO);

        // Perform request
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<LoginResponseDTO> responseDTO = restTemplate.postForEntity(uri, entity, LoginResponseDTO.class);

        return "Bearer " + Objects.requireNonNull(responseDTO.getBody()).getAuthToken();
    }

    @When("Someone makes a request to the /users API endpoint without an authentication token")
    public void iMakeARequestToTheUsersAPIEndpointWithoutAnAuthenticationToken() throws URISyntaxException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/users");

            // Perform request
            restTemplate.getForEntity(uri, ExceptionDTO.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }

    @Then("The server will return a {int} unauthorized")
    public void theServerWillReturnAUnauthorized(int expectedHttpStatusCode) {
        Assert.assertEquals(expectedHttpStatusCode, httpClientErrorException.getRawStatusCode());
    }

    @When("An employee makes a request to the /users API endpoint")
    public void anEmployeeMakesARequestToTheUsersAPIEndpoint() throws URISyntaxException, JsonProcessingException {
        // Create request
        URI uri = new URI(baseUrl + "/users");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getJwtToken("alice@example.com", "idk"));

        // Perform request
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        stringResponse = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
    }

    @Then("The server will return list of {int} users")
    public void theServerWillReturnListOfUsers(int arg0) throws JSONException {
        JSONArray jsonArray = new JSONArray(stringResponse.getBody());
        Assert.assertEquals(arg0, jsonArray.length());
    }

    @When("A customer makes a request to the /users API endpoint")
    public void aCustomerMakesARequestToTheUsersAPIEndpoint() throws URISyntaxException, JsonProcessingException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/users");

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", getJwtToken("bob@example.com", "idk"));

            // Perform request
            HttpEntity<String> entity = new HttpEntity<>(null, headers);
            stringResponse = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }

    @Then("The server will return a {int} forbidden")
    public void theServerWillReturnAForbidden(int expectedHttpStatusCode) {
        Assert.assertEquals(expectedHttpStatusCode, httpClientErrorException.getRawStatusCode());
    }

    @When("Someone makes a request to the \\/users\\/{int} API endpoint without an authentication token")
    public void someoneMakesARequestToTheUsersAPIEndpointWithoutAnAuthenticationToken(int userId) throws URISyntaxException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/users/" + userId);

            // Perform request
            restTemplate.getForEntity(uri, ExceptionDTO.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }

    @When("An employee makes a request to the \\/users\\/{int} API endpoint")
    public void anEmployeeMakesARequestToTheUsersAPIEndpoint(int userId) throws URISyntaxException, JsonProcessingException {
        // Create request
        URI uri = new URI(baseUrl + "/users/" + userId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getJwtToken("alice@example.com", "idk"));

        // Perform request
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        stringResponse = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
    }

    @Then("The server server will return the user with the email address of alice@example.com")
    public void theServerServerWillReturnTheUserWithTheEmailAddressOfAliceExampleCom() throws JSONException {
        // Parse
        JSONObject jsonObject = new JSONObject(stringResponse.getBody());

        // Assertions
        Assert.assertEquals(2, jsonObject.get("id"));
        Assert.assertEquals("bob@example.com", jsonObject.get("emailAddress"));
        Assert.assertEquals("Bob", jsonObject.get("firstName"));
        Assert.assertEquals("Bobson", jsonObject.get("lastName"));
        Assert.assertEquals("2012-12-12", jsonObject.get("birthDate"));
        Assert.assertEquals("+31 6 87654321", jsonObject.get("phone"));
        Assert.assertEquals(2000.0, jsonObject.get("dayLimit"));
        Assert.assertEquals(50.0, jsonObject.get("transactionLimit"));

        JSONArray roles = jsonObject.getJSONArray("role");
        Assert.assertEquals(1, roles.length());
        Assert.assertEquals("customer", roles.get(0));
    }

    @When("A customer makes a request to the \\/users\\/{int} API endpoint and they are that user")
    public void aCustomerMakesARequestToTheUsersAPIEndpointAndTheyAreThatUser(int userId) throws URISyntaxException, JsonProcessingException {
        // Create request
        URI uri = new URI(baseUrl + "/users/" + userId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getJwtToken("bob@example.com", "idk"));

        // Perform request
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        stringResponse = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
    }

    @When("A customer makes a request to the \\/users\\/{int} API endpoint and they are that not the same user as requested")
    public void aCustomerMakesARequestToTheUsersAPIEndpointAndTheyAreThatNotTheSameUserAsRequested(int userId) throws URISyntaxException, JsonProcessingException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/users/" + userId);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", getJwtToken("bob@example.com", "idk"));

            // Perform request
            HttpEntity<String> entity = new HttpEntity<>(null, headers);
            stringResponse = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }

    @When("Someone makes a PUT request to the \\/users/{int} API endpoint without an authentication token")
    public void someoneMakesAPUTRequestToTheUsersAPIEndpointWithoutAnAuthenticationToken(int userId) throws URISyntaxException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/users/" + userId);

            // Perform request
            restTemplate.getForEntity(uri, ExceptionDTO.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }
}
