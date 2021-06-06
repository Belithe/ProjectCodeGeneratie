package io.swagger.IT.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.UpdateAccountPutBody;
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

public class AccountSteps {
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

    // Check any thrown error
    @Then("The server will return a number {int} {word} error")
    public void theServerWillReturnAUnauthorized(int expectedHttpStatusCode, String httpStatusString) {
        Assert.assertNotNull(httpClientErrorException);
        Assert.assertEquals(expectedHttpStatusCode, httpClientErrorException.getRawStatusCode());
    }

    @And("The returned account JSON objects contains a field of {string} with value of {string}")
    public void theReturnedAccountJSONObjectsContainsAFieldOfWithValueOf(String key, String expectedValue) throws JSONException {
        // Parse
        JSONObject jsonObject = new JSONObject(stringResponse.getBody());

        // Assertions
        Assert.assertEquals(expectedValue, jsonObject.get(key));
    }

    // Employee request /accounts/
    @When("An employee makes a request to the /accounts API endpoint")
    public void anEmployeeMakesARequestToTheAccountsAPIEndpoint() throws URISyntaxException, JsonProcessingException {
        // Create request
        URI uri = new URI(baseUrl + "/accounts");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getJwtToken("alice@example.com", "idk"));

        // Perform request
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        stringResponse = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
    }

    // Customer request /accounts/
    @When("A customer makes a request to the /accounts API endpoint")
    public void aCustomerMakesARequestToTheAccountsAPIEndpoint() throws URISyntaxException, JsonProcessingException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/accounts");

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", getJwtToken("bob@example.com", "idk"));

            // Perform request
            HttpEntity<String> entity = new HttpEntity<>(null, headers);
            stringResponse = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }

    // Unauthorized request /accounts/
    @When("Someone makes a request to the /accounts API endpoint without an authentication token")
    public void iMakeARequestToTheAccountsAPIEndpointWithoutAnAuthenticationToken() throws URISyntaxException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/accounts");

            // Perform request
            restTemplate.getForEntity(uri, ExceptionDTO.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }


    // Returns account list with length
    @Then("The server will return list of {int} accounts")
    public void theServerWillReturnListOfAccounts(int expectedAmount) throws JSONException {
        JSONArray jsonArray = new JSONArray(stringResponse.getBody());
        Assert.assertEquals(expectedAmount, jsonArray.length());
    }

    // Check returned account list IBANs
    @And("The IBANs will be {word} and {word}")
    public void theIBANsWillBeAnd(String iban1, String iban2) throws JSONException {
        JSONArray jsonArray = new JSONArray(stringResponse.getBody());
        Assert.assertEquals(iban1, jsonArray.getJSONObject(0).get("iban"));
        Assert.assertEquals(iban2, jsonArray.getJSONObject(1).get("iban"));
    }


    // Returns matching IBAN account
    @Then("The server will return the account with the matching IBAN of {word}")
    public void theServerWillReturnTheAccountWithTheMatchingIban(String expectedIban) throws JSONException {
        // Parse
        JSONObject jsonObject = new JSONObject(stringResponse.getBody());

        // Assertions
        Assert.assertEquals(expectedIban, jsonObject.get("iban"));
    }

    // Returns with httpStatus
    @Then("The server will return a number {int} {word}")
    public void theServerWillReturnAOk(int statusCode, String statusName) {
        Assert.assertEquals(statusCode, stringResponse.getStatusCodeValue());
    }

    // Unauthorized request /accounts/IBAN
    @When("Someone makes a request to the \\/accounts\\/{word} API endpoint without an authentication token")
    public void someoneMakesARequestToTheAccountsAPIEndpointWithoutAnAuthenticationToken(String iban) throws URISyntaxException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/accounts/" + iban);

            // Perform request
            restTemplate.getForEntity(uri, ExceptionDTO.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }

    // Employee request /accounts/IBAN
    @When("An employee makes a request to the \\/accounts\\/{word} API endpoint")
    public void anEmployeeMakesARequestToTheAccountsAPIEndpoint(String iban) throws URISyntaxException, JsonProcessingException {
        // Create request
        URI uri = new URI(baseUrl + "/accounts/" + iban);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getJwtToken("alice@example.com", "idk"));

        // Perform request
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        stringResponse = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
    }

    // Customer request /accounts/IBAN while owned
    @When("A customer makes a request to the \\/accounts\\/{word} API endpoint and they own that IBAN")
    public void aCustomerMakesARequestToTheAccountsAPIEndpointAndTheyOwnTheIBAN(String iban) throws URISyntaxException, JsonProcessingException {
        // Create request
        URI uri = new URI(baseUrl + "/accounts/" + iban);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getJwtToken("bob@example.com", "idk"));

        // Perform request
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        stringResponse = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
    }

    // Customer request /accounts/IBAN not owned
    @When("A customer makes a request to the \\/accounts\\/{word} API endpoint and they do not own that IBAN")
    public void aCustomerMakesARequestToTheAccountsAPIEndpointAndTheyAreThatNotTheSameAccountAsRequested(String iban) throws URISyntaxException, JsonProcessingException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/accounts/" + iban);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", getJwtToken("bob@example.com", "idk"));

            // Perform request
            HttpEntity<String> entity = new HttpEntity<>(null, headers);
            stringResponse = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }


    // Request PUT unauthorized
    @When("Someone makes a PUT request to the \\/accounts\\/{word} API endpoint without an authentication token")
    public void someoneMakesAPUTRequestToTheAccountsAPIEndpointWithoutAnAuthenticationToken(String iban) throws URISyntaxException, JsonProcessingException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/accounts/" + iban);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");

            // Create body
            UpdateAccountPutBody accountPutBody = new UpdateAccountPutBody();
            accountPutBody.setMinimumLimit(200f);

            String requestBody = objectMapper.writeValueAsString(accountPutBody);

            // Perform request
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }

    // Request PUT Employee
    @When("An employee makes a PUT request to the \\/accounts\\/{word} API endpoint")
    public void anEmployeeMakesAPUTRequestToTheAccountsAPIEndpoint(String iban) throws JsonProcessingException, URISyntaxException {
        // Create request
        URI uri = new URI(baseUrl + "/accounts/" + iban);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", getJwtToken("alice@example.com", "idk"));

        // Create body
        UpdateAccountPutBody accountPutBody = new UpdateAccountPutBody();
        accountPutBody.setMinimumLimit(200f);

        String requestBody = objectMapper.writeValueAsString(accountPutBody);

        // Perform request
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        stringResponse = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
    }

    // Request PUT Customer
    @When("A customer makes a PUT request to the \\/accounts\\/{word} API endpoint updating fields they have access to")
    public void aCustomerMakesAPUTRequestToTheAccountsAPIEndpointUpdatingFieldsTheyHaveAccessTo(String iban) throws URISyntaxException, JsonProcessingException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/accounts/" + iban);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Authorization", getJwtToken("bob@example.com", "idk"));

            // Create body
            UpdateAccountPutBody accountPutBody = new UpdateAccountPutBody();
            accountPutBody.setMinimumLimit(200f);

            String requestBody = objectMapper.writeValueAsString(accountPutBody);

            // Perform request
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            stringResponse = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }


    //Request DELETE Unauthorized
    @When("Someone makes a DELETE request to the \\/accounts\\/{word} API endpoint without an authentication token")
    public void someoneMakesADELETERequestToTheAccountsAPIEndpointWithoutAnAuthenticationToken(String iban) throws URISyntaxException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/accounts/" + iban);

            // Perform request
            restTemplate.exchange(uri, HttpMethod.DELETE, null, String.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }

    //Request DELETE employee
    @When("An employee makes a DELETE request to the \\/accounts\\/{word} API endpoint")
    public void anEmployeeMakesADELETERequestToTheAccountsAPIEndpoint(String iban) throws URISyntaxException, JsonProcessingException {
        // Create request
        URI uri = new URI(baseUrl + "/accounts/" + iban);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getJwtToken("alice@example.com", "idk"));

        // Perform request
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        stringResponse = restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);
    }

    //Request DELETE customer
    @When("A customer makes a DELETE request to the \\/accounts\\/{word} API endpoint")
    public void aCustomerMakesADELETERequestToTheAccountsAPIEndpoint(String iban) throws URISyntaxException, JsonProcessingException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/accounts/" + iban);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", getJwtToken("bob@example.com", "idk"));

            // Perform request
            HttpEntity<String> entity = new HttpEntity<>(null, headers);
            restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }


    //Request POST unauthorized
    @When("Someone makes a POST request to the \\/accounts API endpoint without an authentication token")
    public void someoneMakesAPOSTRequestToTheAccountsAPIEndpointWithoutAnAuthenticationToken() throws URISyntaxException, JsonProcessingException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/accounts");
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");

            // Create body
            String requestBody = "{\n" +
                    "  \"birthDate\": \"1800-06-02\",\n" +
                    "  \"dayLimit\": 0,\n" +
                    "  \"emailAddress\": \"test@example.com\",\n" +
                    "  \"firstName\": \"Test\",\n" +
                    "  \"lastName\": \"Testson\",\n" +
                    "  \"password\": \"idk\",\n" +
                    "  \"phone\": \"+31 6 12345678\",\n" +
                    "  \"role\": [\n" +
                    "    \"customer\"\n" +
                    "  ],\n" +
                    "  \"transactionLimit\": 100\n" +
                    "}";

            // Perform request
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }

    //Request POST employee
    @When("An employee makes a POST request to the \\/accounts API endpoint")
    public void anEmployeeMakesAPOSTRequestToTheAccountsAPIEndpoint() throws URISyntaxException, JsonProcessingException {
        // Create request
        URI uri = new URI(baseUrl + "/accounts");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", getJwtToken("alice@example.com", "idk"));

        // Create body
        String requestBody = "{\n" +
                "  \"accountType\": \"current\",\n" +
                "  \"iban\": \"NL02INHO0123456789\",\n" +
                "  \"minimumLimit\": 200,\n" +
                "  \"userId\": 2\n" +
                "}";

        // Perform request
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        stringResponse = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
    }

    //Request POST Customer
    @When("A customer makes a POST request to the \\/accounts API endpoint")
    public void aCustomerMakesAPOSTRequestToTheAccountsAPIEndpoint() throws JsonProcessingException, URISyntaxException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/accounts");
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Authorization", getJwtToken("bob@example.com", "idk"));

            // Create body
            String requestBody = "{\n" +
                    "  \"accountType\": \"current\",\n" +
                    "  \"IBAN\": \"NL02INHO0123456780\",\n" +
                    "  \"minimumLimit\": 200,\n" +
                    "  \"userId\": 2\n" +
                    "}";

            // Perform request
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            stringResponse = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }
}

