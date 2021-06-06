package io.swagger.IT.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.PostTransBody;
import io.swagger.model.dto.ExceptionDTO;
import io.swagger.model.dto.LoginDTO;
import io.swagger.model.dto.LoginResponseDTO;
import org.junit.Assert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class TransactionSteps {
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

    @When("Someone makes a request to the \\/transactions API endpoint without an authentication token")
    public void someoneMakesARequestToTheTransactionsAPIEndpointWithoutAnAuthenticationToken() throws URISyntaxException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/transactions");

            // Perform request
            restTemplate.getForEntity(uri, ExceptionDTO.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }

    @When("An employee makes a request to the \\/transactions API endpoint")
    public void anEmployeeMakesARequestToTheTransactionsAPIEndpoint() throws URISyntaxException, JsonProcessingException {
        // Create request
        URI uri = new URI(baseUrl + "/transactions");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getJwtToken("alice@example.com", "idk"));

        // Perform request
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        stringResponse = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
    }

    @Then("The server will return list of {int} error transactions")
    public void theServerWillReturnListOfTransactions(int arg0) throws JSONException {
        JSONArray jsonArray = new JSONArray(stringResponse.getBody());
        Assert.assertEquals(arg0, jsonArray.length());
    }

    @And("The transfer from IBAN will be {string} and {string}\"")
    public void theTransferFromIBANWillBeAnd(String iban1, String iban2) throws JSONException {
        JSONArray jsonArray = new JSONArray(stringResponse.getBody());
        Assert.assertEquals(iban1, jsonArray.getJSONObject(0).get("IBAN"));
        Assert.assertEquals(iban2, jsonArray.getJSONObject(1).get("IBAN"));
    }

    @When("A transactions makes a request to the \\/transactions API endpoint")
    public void aTransactionsMakesARequestToTheTransactionsAPIEndpoint() throws JsonProcessingException, URISyntaxException {
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


    @Then("The server will return a {int} forbidden error")
    public void theServerWillReturnAnErrorForbidden(int expectedHttpStatusCode) {
        Assert.assertNotNull(httpClientErrorException);
        Assert.assertEquals(expectedHttpStatusCode, httpClientErrorException.getRawStatusCode());
    }

    @When("Someone makes a request to the \\/transactions\\/{int} API endpoint without an authentication token")
    public void someoneMakesARequestToTheTransactionsAPIEndpointWithoutAnAuthenticationToken(int transactionId) throws URISyntaxException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/transactions/" + transactionId);

            // Perform request
            restTemplate.getForEntity(uri, ExceptionDTO.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }

    @When("An employee makes a request to the \\/transactions\\/{int} API endpoint")
    public void anEmployeeMakesARequestToTheTransactionsAPIEndpoint(int transactionId) throws URISyntaxException, JsonProcessingException {
        // Create request
        URI uri = new URI(baseUrl + "/transactions/" + transactionId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getJwtToken("alice@example.com", "idk"));

        // Perform request
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        stringResponse = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
    }

    @Then("The server server will return the transaction with the email address of {string}")
    public void theServerServerWillReturnTheTransactionWithTheEmailAddressOfAliceExampleCom(String expectedEmailAddress) throws JSONException {
        // Parse
        JSONObject jsonObject = new JSONObject(stringResponse.getBody());

        // Assertions
        Assert.assertEquals(expectedEmailAddress, jsonObject.get("emailAddress"));
    }

    @When("A customer makes a request to the \\/transactions\\/{int} API endpoint and they are that transaction")
    public void aCustomerMakesARequestToTheTransactionsAPIEndpointAndTheyAreThatTransaction(int transactionId) throws URISyntaxException, JsonProcessingException {
        // Create request
        URI uri = new URI(baseUrl + "/transactions/" + transactionId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getJwtToken("bob@example.com", "idk"));

        // Perform request
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        stringResponse = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
    }

    @When("Someone makes a PUT request to the \\/transactions\\/{int} API endpoint without an authentication token")
    public void someoneMakesAPUTRequestToTheTransactionsAPIEndpointWithoutAnAuthenticationToken(int transactionId) throws URISyntaxException, JsonProcessingException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/transactions/" + transactionId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");

            // Create body
            PostTransBody transactionPutBody = new PostTransBody();
            //transactionPutBody.setFirstName("test");

            String requestBody = objectMapper.writeValueAsString(transactionPutBody);

            // Perform request
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }

    @When("An employee makes a PUT request to the \\/transactions\\/{int} API endpoint")
    public void anEmployeeMakesAPUTRequestToTheTransactionsAPIEndpoint(int transactionId) throws JsonProcessingException, URISyntaxException {
        // Create request
        URI uri = new URI(baseUrl + "/transactions/" + transactionId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", getJwtToken("alice@example.com", "idk"));

        // Create body
        PostTransBody transactionPutBody = new PostTransBody();
        //transactionPutBody.setFirstName("Bob");

        String requestBody = objectMapper.writeValueAsString(transactionPutBody);

        // Perform request
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        stringResponse = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
    }

    @Then("The server will return a {int} ok message")
    public void theServerWillReturnAOkStatus(int statusCode) {
        Assert.assertEquals(statusCode, stringResponse.getStatusCodeValue());
    }

    @When("A customer makes a PUT request to the \\/transactions\\/{int} API endpoint updating fields they have access to")
    public void aCustomerMakesAPUTRequestToTheTransactionsAPIEndpointUpdatingFieldsTheyHaveAccessTo(int transactionId) throws URISyntaxException, JsonProcessingException {
        // Create request
        URI uri = new URI(baseUrl + "/transactions/" + transactionId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", getJwtToken("bob@example.com", "idk"));

        // Create body
        PostTransBody transactionPutBody = new PostTransBody();
        //transactionPutBody.setPassword("idk");

        String requestBody = objectMapper.writeValueAsString(transactionPutBody);

        // Perform request
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        stringResponse = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
    }

    @When("A customer makes a PUT request to the \\/transactions\\/{int} API endpoint updating fields they have do not access to")
    public void aCustomerMakesAPUTRequestToTheTransactionsAPIEndpointUpdatingFieldsTheyHaveDoNotAccessTo(int transactionId) throws JsonProcessingException, URISyntaxException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/transactions/" + transactionId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Authorization", getJwtToken("bob@example.com", "idk"));

            // Create body
            PostTransBody transactionPutBody = new PostTransBody();
            //transactionPutBody.setFirstName("Bob");

            String requestBody = objectMapper.writeValueAsString(transactionPutBody);

            // Perform request
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }

    @Then("The server will return a {int} bad request error")
    public void theServerWillReturnABadRequestError(int statusCode) {
        Assert.assertNotNull(httpClientErrorException);
        Assert.assertEquals(statusCode, httpClientErrorException.getRawStatusCode());
    }

    @Then("The server will return a {int} unauthorized error")
    public void theServerWillReturnAUnauthorized(int expectedHttpStatusCode) {
        Assert.assertNotNull(httpClientErrorException);
        Assert.assertEquals(expectedHttpStatusCode, httpClientErrorException.getRawStatusCode());
    }


    @When("An employee makes a PUT request to the \\/transactions\\/{int} API endpoint updating fields the customer does not have access to")
    public void anEmployeeMakesAPUTRequestToTheTransactionsAPIEndpointUpdatingFieldsTheCustomerDoesNotHaveAccessTo(int transactionId) throws URISyntaxException, JsonProcessingException {
        // Create request
        URI uri = new URI(baseUrl + "/transactions/" + transactionId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", getJwtToken("alice@example.com", "idk"));

        // Create body
        PostTransBody transactionPutBody = new PostTransBody();
        //transactionPutBody.set("Bob");

        String requestBody = objectMapper.writeValueAsString(transactionPutBody);

        // Perform request
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        stringResponse = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
    }

    @When("Someone makes a DELETE request to the \\/transactions\\/{int} API endpoint without an authentication token")
    public void someoneMakesADELETERequestToTheTransactionsAPIEndpointWithoutAnAuthenticationToken(int transactionId) throws URISyntaxException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/transactions/" + transactionId);

            // Perform request
            restTemplate.exchange(uri, HttpMethod.DELETE, null, String.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }

    @When("An employee makes a DELETE request to the \\/transactions\\/{int} API endpoint")
    public void anEmployeeMakesADELETERequestToTheTransactionsAPIEndpoint(int transactionId) throws URISyntaxException, JsonProcessingException {
        // Create request
        URI uri = new URI(baseUrl + "/transactions/" + transactionId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getJwtToken("alice@example.com", "idk"));

        // Perform request
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        stringResponse = restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);
    }

    @When("A customer makes a DELETE request to the \\/transactions\\/{int} API endpoint")
    public void aCustomerMakesADELETERequestToTheTransactionsAPIEndpoint(int transactionId) throws URISyntaxException, JsonProcessingException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/transactions/" + transactionId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", getJwtToken("bob@example.com", "idk"));

            // Perform request
            HttpEntity<String> entity = new HttpEntity<>(null, headers);
            restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }

    @When("Someone makes a POST request to the \\/transactions API endpoint without an authentication token")
    public void someoneMakesAPOSTRequestToTheTransactionsAPIEndpointWithoutAnAuthenticationToken() throws URISyntaxException, JsonProcessingException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/transactions");
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");

            // Create body
            String requestBody = "{\"amount\":9000.01,\"timestamp\":{\"offset\":{\"totalSeconds\":0,\"id\":\"Z\",\"rules\":{\"fixedOffset\":true,\"transitionRules\":[],\"transitions\":[]}},\"nano\":0,\"year\":2021,\"monthValue\":1,\"dayOfMonth\":1,\"hour\":8,\"minute\":0,\"second\":1,\"month\":\"JANUARY\",\"dayOfWeek\":\"FRIDAY\",\"dayOfYear\":1},\"transactionId\":123567890,\"transferFrom\":\"NL02ABNA0123456789\",\"transferTo\":\"NL02ABNA0123456789\",\"type\":\"deposit\",\"userPerforming\":42}";

            // Perform request
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }

    @When("An employee makes a POST request to the \\/transactions API endpoint")
    public void anEmployeeMakesAPOSTRequestToTheTransactionsAPIEndpoint() throws URISyntaxException, JsonProcessingException {
        // Create request
        URI uri = new URI(baseUrl + "/transactions");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", getJwtToken("alice@example.com", "idk"));

        // Create body
        String requestBody = "{\"amount\":9000.01,\"timestamp\":{\"offset\":{\"totalSeconds\":0,\"id\":\"Z\",\"rules\":{\"fixedOffset\":true,\"transitionRules\":[],\"transitions\":[]}},\"nano\":0,\"year\":2021,\"monthValue\":1,\"dayOfMonth\":1,\"hour\":8,\"minute\":0,\"second\":1,\"month\":\"JANUARY\",\"dayOfWeek\":\"FRIDAY\",\"dayOfYear\":1},\"transactionId\":123567890,\"transferFrom\":\"NL02ABNA0123456789\",\"transferTo\":\"NL02ABNA0123456789\",\"type\":\"deposit\",\"userPerforming\":42}";


        // Perform request
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        stringResponse = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
    }

    @When("An employee makes a POST request to the \\/transactions API endpoint and does not provide a valid post body")
    public void anEmployeeMakesAPOSTRequestToTheTransactionsAPIEndpointAndDoesNotProvideAValidPostBody() throws JsonProcessingException, URISyntaxException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/transactions");
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Authorization", getJwtToken("alice@example.com", "idk"));

            // Create body
            String requestBody = "{\"amount\":9000.01,\"timestamp\":{\"offset\":{\"totalSeconds\":0,\"id\":\"Z\",\"rules\":{\"fixedOffset\":true,\"transitionRules\":[],\"transitions\":[]}},\"nano\":0,\"year\":2021,\"monthValue\":1,\"dayOfMonth\":1,\"hour\":8,\"minute\":0,\"second\":1,\"month\":\"JANUARY\",\"dayOfWeek\":\"FRIDAY\",\"dayOfYear\":1},\"transactionId\":123567890,\"transferFrom\":\"NL02ABNA0123456789\",\"transferTo\":\"NL02ABNA0123456789\",\"type\":\"deposit\",\"userPerforming\":42}";


            // Perform request
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            stringResponse = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }

    @When("A customer makes a POST request to the \\/transactions API endpoint")
    public void aCustomerMakesAPOSTRequestToTheTransactionsAPIEndpoint() throws JsonProcessingException, URISyntaxException {
        try {
            // Create request
            URI uri = new URI(baseUrl + "/transactions");
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Authorization", getJwtToken("bob@example.com", "idk"));

            // Create body
            String requestBody = "{\"amount\":9000.01,\"timestamp\":{\"offset\":{\"totalSeconds\":0,\"id\":\"Z\",\"rules\":{\"fixedOffset\":true,\"transitionRules\":[],\"transitions\":[]}},\"nano\":0,\"year\":2021,\"monthValue\":1,\"dayOfMonth\":1,\"hour\":8,\"minute\":0,\"second\":1,\"month\":\"JANUARY\",\"dayOfWeek\":\"FRIDAY\",\"dayOfYear\":1},\"transactionId\":123567890,\"transferFrom\":\"NL02ABNA0123456789\",\"transferTo\":\"NL02ABNA0123456789\",\"type\":\"deposit\",\"userPerforming\":42}";


            // Perform request
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            stringResponse = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
        } catch (HttpClientErrorException e) {
            httpClientErrorException = e;
        }
    }

    @Then("The server will return a {int} unauthorize error")
    public void theServerWillReturnAUnauthorizeError(int expectedStatusCode) {
        Assert.assertNotNull(httpClientErrorException);
        Assert.assertEquals(expectedStatusCode, httpClientErrorException.getRawStatusCode());
    }
}
