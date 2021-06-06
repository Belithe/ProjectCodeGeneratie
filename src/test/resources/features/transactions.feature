Feature: Transaction tests
  Scenario: Get all transactions from the API without authentication
    When Someone makes a request to the /transactions API endpoint without an authentication token
    Then The server will return a 401 error unauthorized

  Scenario: Get all users from the API performed by employee
    When An employee makes a request to the /transactions API endpoint
    Then The server will return list of 3 transactions
    And The transfer from IBAN will be "NL01INHO0000000003" and "NL01INHO0000000002""

  Scenario: Get all transactions from the API performed by customer
    When A transactions makes a request to the /transactions API endpoint
    Then The server will return a 403 forbidden

  Scenario: Get a single transaction from the API without authentication
    When Someone makes a request to the /transactions/1 API endpoint without an authentication token
    Then The server will return a 401 unauthorized

  Scenario: Get a single transaction from the API performed by employee
    When An employee makes a request to the /transactions/2 API endpoint
    Then The server will return a 200 ok message
    And The returned JSON objects contains a field of "userPerforming" with value of 1
    And The returned JSON objects contains a field of "fransferTo" with value of "NL01INHO0000000003"
    And The returned JSON objects contains a field of "fransferFrom" with value of "NL01INHO0000000002"
    And The returned JSON objects contains a field of "amount" with value of 0
    And The returned JSON objects contains a field of "transactionId" with value of 0

  Scenario: Get own transaction from the API performed by customer
    When A customer makes a request to the /transactions/2 API endpoint and they are that transaction
    Then The server will return a 200 ok message
    And The returned JSON objects contains a field of "userPerforming" with value of 1
    And The returned JSON objects contains a field of "fransferTo" with value of "NL01INHO0000000003"
    And The returned JSON objects contains a field of "fransferFrom" with value of "NL01INHO0000000002"
    And The returned JSON objects contains a field of "amount" with value of 0
    And The returned JSON objects contains a field of "transactionId" with value of 0

  Scenario: Create transaction without authentication
    When Someone makes a POST request to the /transactions API endpoint without an authentication token
    Then The server will return a 401 unauthorized

  Scenario: Create transaction performed by customer
    When A customer makes a POST request to the /transactions API endpoint
    Then The server will return a 403 forbidden

  Scenario: Create transaction performed by employee
    When An employee makes a POST request to the /transactions API endpoint
    Then The server will return a 200 ok

  Scenario: Create transaction performed by employee with an incomplete body
    When An employee makes a POST request to the /transactions API endpoint and does not provide a valid post body
    Then The server will return a 400 bad request

  Scenario: Delete transaction without authentication
    When Someone makes a DELETE request to the /transactions/7 API endpoint without an authentication token
    Then The server will return a 401 unauthorized

  Scenario: Delete transaction performed by employee
    When An employee makes a DELETE request to the /transactions/7 API endpoint
    Then The server will return a 200 ok
