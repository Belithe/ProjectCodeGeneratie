package io.swagger.IT.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.User;
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
import java.util.List;
import java.util.Objects;

public class UserSteps {
    private final String baseUrl = "http://localhost:5013";
    private RestTemplate restTemplate = new RestTemplate();
    private HttpClientErrorException httpClientErrorException;
    private ObjectMapper objectMapper = new ObjectMapper();

    private String getJwtToken(String emailAddress, String password) throws URISyntaxException, JsonProcessingException {
        URI uri = new URI(baseUrl + "/login");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmailAddress(emailAddress);
        loginDTO.setPassword(password);

        String requestBody = objectMapper.writeValueAsString(loginDTO);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<LoginResponseDTO> responseDTO = restTemplate.postForEntity(uri, entity, LoginResponseDTO.class);

        return "Bearer " + Objects.requireNonNull(responseDTO.getBody()).getAuthToken();
    }

    @When("I make a request to the /users API endpoint without an authentication token")
    public void iMakeARequestToTheUsersAPIEndpointWithoutAnAuthenticationToken() throws URISyntaxException {
        try {
            URI uri = new URI(baseUrl + "/users");
            restTemplate.getForEntity(uri, ExceptionDTO.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }

    @Then("The server will return a {int} unauthorized")
    public void theServerWillReturnAUnauthorized(int expectedHttpStatusCode) {
        Assert.assertEquals(expectedHttpStatusCode, httpClientErrorException.getRawStatusCode());
    }

    private ResponseEntity<String> stringResponse;

    @When("An employee makes a request to the \\/users API endpoint")
    public void anEmployeeMakesARequestToTheUsersAPIEndpoint() throws URISyntaxException, JsonProcessingException {
        URI uri = new URI(baseUrl + "/users");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getJwtToken("alice@example.com", "idk"));

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        stringResponse = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
    }

    @Then("The server will return list of {int} users")
    public void theServerWillReturnListOfUsers(int arg0) throws JSONException {
        JSONArray jsonObject = new JSONArray(stringResponse.getBody());
        Assert.assertEquals(arg0, jsonObject.length());
    }
}
