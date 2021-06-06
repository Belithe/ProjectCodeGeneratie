package io.swagger.IT.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.CreateUserPostBody;
import io.swagger.model.UpdateUserPutBody;
import io.swagger.model.UserRole;
import io.swagger.model.dto.ExceptionDTO;
import io.swagger.model.dto.LoginDTO;
import io.swagger.model.dto.LoginResponseDTO;
import org.hibernate.sql.Update;
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
import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
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
        Assert.assertNotNull(httpClientErrorException);
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

    @And("The first names will be {string}, {string}, and {string}")
    public void theFirstNamesWillBeAnd(String name1, String name2, String name3) throws JSONException {
        JSONArray jsonArray = new JSONArray(stringResponse.getBody());
        Assert.assertEquals(name1, jsonArray.getJSONObject(0).get("firstName"));
        Assert.assertEquals(name2, jsonArray.getJSONObject(1).get("firstName"));
        Assert.assertEquals(name3, jsonArray.getJSONObject(2).get("firstName"));
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
        Assert.assertNotNull(httpClientErrorException);
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

    @Then("The server server will return the user with the email address of {string}")
    public void theServerServerWillReturnTheUserWithTheEmailAddressOfAliceExampleCom(String expectedEmailAddress) throws JSONException {
        // Parse
        JSONObject jsonObject = new JSONObject(stringResponse.getBody());

        // Assertions
        Assert.assertEquals(expectedEmailAddress, jsonObject.get("emailAddress"));
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

    @And("The returned JSON objects contains a field of {string} with value of {string}")
    public void theReturnedJSONObjectsContainsAFieldOfWithValueOf(String key, String expectedValue) throws JSONException {
        // Parse
        JSONObject jsonObject = new JSONObject(stringResponse.getBody());

        // Assertions
        Assert.assertEquals(expectedValue, jsonObject.get(key));
    }

    @And("The returned JSON objects contains a field of {string} with value of {float}")
    public void theReturnedJSONObjectsContainsAFieldOfWithValueOf(String key, float expectedFloatValue) throws JSONException {
        // Parse
        JSONObject jsonObject = new JSONObject(stringResponse.getBody());

        // Assertions
        Assert.assertEquals((double) expectedFloatValue, jsonObject.get(key));
    }

    @And("The returned JSON exception object contains a field of {string} with value of {string}")
    public void theReturnedJSONExceptionObjectContainsAFieldOfWithValueOf(String key, String expectedValue) throws JSONException {
        // Parse
        JSONObject jsonObject = new JSONObject(httpClientErrorException.getResponseBodyAsString());

        // Assertions
        Assert.assertEquals(expectedValue, jsonObject.get(key));
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

    @When("Someone makes a PUT request to the \\/users\\/{int} API endpoint without an authentication token")
    public void someoneMakesAPUTRequestToTheUsersAPIEndpointWithoutAnAuthenticationToken(int userId) throws URISyntaxException, JsonProcessingException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/users/" + userId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");

            // Create body
            UpdateUserPutBody userPutBody = new UpdateUserPutBody();
            userPutBody.setFirstName("test");

            String requestBody = objectMapper.writeValueAsString(userPutBody);

            // Perform request
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }

    @When("An employee makes a PUT request to the \\/users\\/{int} API endpoint")
    public void anEmployeeMakesAPUTRequestToTheUsersAPIEndpoint(int userId) throws JsonProcessingException, URISyntaxException {
        // Create request
        URI uri = new URI(baseUrl + "/users/" + userId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", getJwtToken("alice@example.com", "idk"));

        // Create body
        UpdateUserPutBody userPutBody = new UpdateUserPutBody();
        userPutBody.setFirstName("Bob");

        String requestBody = objectMapper.writeValueAsString(userPutBody);

        // Perform request
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        stringResponse = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
    }

    @Then("The server will return a {int} ok")
    public void theServerWillReturnAOk(int statusCode) {
        Assert.assertEquals(statusCode, stringResponse.getStatusCodeValue());
    }

    @When("A customer makes a PUT request to the \\/users\\/{int} API endpoint updating fields they have access to")
    public void aCustomerMakesAPUTRequestToTheUsersAPIEndpointUpdatingFieldsTheyHaveAccessTo(int userId) throws URISyntaxException, JsonProcessingException {
        // Create request
        URI uri = new URI(baseUrl + "/users/" + userId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", getJwtToken("bob@example.com", "idk"));

        // Create body
        UpdateUserPutBody userPutBody = new UpdateUserPutBody();
        userPutBody.setPassword("idk");

        String requestBody = objectMapper.writeValueAsString(userPutBody);

        // Perform request
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        stringResponse = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
    }

    @When("A customer makes a PUT request to the \\/users\\/{int} API endpoint updating fields they have do not access to")
    public void aCustomerMakesAPUTRequestToTheUsersAPIEndpointUpdatingFieldsTheyHaveDoNotAccessTo(int userId) throws JsonProcessingException, URISyntaxException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/users/" + userId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Authorization", getJwtToken("bob@example.com", "idk"));

            // Create body
            UpdateUserPutBody userPutBody = new UpdateUserPutBody();
            userPutBody.setFirstName("Bob");

            String requestBody = objectMapper.writeValueAsString(userPutBody);

            // Perform request
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }

    @Then("The server will return a {int} bad request")
    public void theServerWillReturnABadRequest(int statusCode) {
        Assert.assertNotNull(httpClientErrorException);
        Assert.assertEquals(statusCode, httpClientErrorException.getRawStatusCode());
    }


    @When("An employee makes a PUT request to the \\/users\\/{int} API endpoint updating fields the customer does not have access to")
    public void anEmployeeMakesAPUTRequestToTheUsersAPIEndpointUpdatingFieldsTheCustomerDoesNotHaveAccessTo(int userId) throws URISyntaxException, JsonProcessingException {
        // Create request
        URI uri = new URI(baseUrl + "/users/" + userId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", getJwtToken("alice@example.com", "idk"));

        // Create body
        UpdateUserPutBody userPutBody = new UpdateUserPutBody();
        userPutBody.setFirstName("Bob");

        String requestBody = objectMapper.writeValueAsString(userPutBody);

        // Perform request
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        stringResponse = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
    }

    @When("Someone makes a DELETE request to the \\/users\\/{int} API endpoint without an authentication token")
    public void someoneMakesADELETERequestToTheUsersAPIEndpointWithoutAnAuthenticationToken(int userId) throws URISyntaxException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/users/" + userId);

            // Perform request
            restTemplate.exchange(uri, HttpMethod.DELETE, null, String.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }

    @When("An employee makes a DELETE request to the \\/users\\/{int} API endpoint")
    public void anEmployeeMakesADELETERequestToTheUsersAPIEndpoint(int userId) throws URISyntaxException, JsonProcessingException {
        // Create request
        URI uri = new URI(baseUrl + "/users/" + userId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getJwtToken("alice@example.com", "idk"));

        // Perform request
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        stringResponse = restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);
    }

    @When("A customer makes a DELETE request to the \\/users\\/{int} API endpoint")
    public void aCustomerMakesADELETERequestToTheUsersAPIEndpoint(int userId) throws URISyntaxException, JsonProcessingException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/users/" + userId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", getJwtToken("bob@example.com", "idk"));

            // Perform request
            HttpEntity<String> entity = new HttpEntity<>(null, headers);
            restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }

    @When("Someone makes a POST request to the \\/users API endpoint without an authentication token")
    public void someoneMakesAPOSTRequestToTheUsersAPIEndpointWithoutAnAuthenticationToken() throws URISyntaxException, JsonProcessingException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/users");
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

    @When("An employee makes a POST request to the \\/users API endpoint")
    public void anEmployeeMakesAPOSTRequestToTheUsersAPIEndpoint() throws URISyntaxException, JsonProcessingException {
        // Create request
        URI uri = new URI(baseUrl + "/users");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", getJwtToken("alice@example.com", "idk"));

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
        stringResponse = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
    }

    @When("An employee makes a POST request to the \\/users API endpoint and does not provide a valid post body")
    public void anEmployeeMakesAPOSTRequestToTheUsersAPIEndpointAndDoesNotProvideAValidPostBody() throws JsonProcessingException, URISyntaxException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/users");
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Authorization", getJwtToken("alice@example.com", "idk"));

            // Create body
            String requestBody = "{\n" +
                    "  \"birthDate\": \"1800-06-02\",\n" +
                    "  \"dayLimit\": 0,\n" +
                    "  \"emailAddress\": \"test@example.com\",\n" +
                    "  \"firstName\": \"Test\",\n" +
                    // Missing last name
                    "  \"password\": \"idk\",\n" +
                    "  \"phone\": \"+31 6 12345678\",\n" +
                    "  \"role\": [\n" +
                    "    \"customer\"\n" +
                    "  ],\n" +
                    "  \"transactionLimit\": 100\n" +
                    "}";

            // Perform request
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            stringResponse = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }

    @When("A customer makes a POST request to the \\/users API endpoint")
    public void aCustomerMakesAPOSTRequestToTheUsersAPIEndpoint() throws JsonProcessingException, URISyntaxException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/users");
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Authorization", getJwtToken("bob@example.com", "idk"));

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
            stringResponse = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }

    @When("Someone makes a GET request to the \\/users\\/self API endpoint without an authentication token")
    public void someoneMakesAGETRequestToTheUsersSelfAPIEndpointWithoutAnAuthenticationToken() throws URISyntaxException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/users/self");

            // Perform request
            restTemplate.getForEntity(uri, ExceptionDTO.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }

    @When("Someone makes a GET request to the \\/users\\/self API endpoint providing a authentication token")
    public void someoneMakesAGETRequestToTheUsersSelfAPIEndpointProvidingAAuthenticationToken() throws URISyntaxException, JsonProcessingException {
        // Create request
        URI uri = new URI(baseUrl + "/users/self");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getJwtToken("bob@example.com", "idk"));

        // Perform request
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        stringResponse = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
    }
}
