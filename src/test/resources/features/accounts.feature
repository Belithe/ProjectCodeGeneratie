Feature: Account tests
    # Get all accounts scenarios
  Scenario: Get all accounts from the API without authentication
    When Someone makes a request to the /accounts API endpoint without an authentication token
    Then The server will return a number 401 unauthorized error

  Scenario: Get all accounts from the API performed by employee
    When An employee makes a request to the /accounts API endpoint
    Then The server will return list of 3 accounts
    And The IBANs will be NL01INHO0000000002 and NL01INHO0000000003

  Scenario: Get all accounts from the API performed by customer
    When A customer makes a request to the /accounts API endpoint
    Then The server will return a number 403 forbidden error


    #get Single account scenarios
  Scenario: Get a single account from the API without authentication
    When Someone makes a request to the /accounts/NL01INHO0000000002 API endpoint without an authentication token
    Then The server will return a number 401 unauthorized error

  Scenario: Get a single account from the API performed by employee
    When An employee makes a request to the /accounts/NL01INHO0000000002 API endpoint
    Then The server will return a number 200 ok
    And The returned account JSON objects contains a field of "IBAN" with value of "NL01INHO0000000002"

  Scenario: Get own account from the API performed by customer
    When A customer makes a request to the /accounts/NL01INHO0000000002 API endpoint and they own that IBAN
    Then The server will return a number 200 ok
    And The returned account JSON objects contains a field of "IBAN" with value of "NL01INHO0000000002"

  Scenario: Get other's account from the API performed by customer
    When A customer makes a request to the /accounts/NL01INHO0000000004 API endpoint and they do not own that IBAN
    Then The server will return a number 403 forbidden error


    #Put account scenarios
  Scenario: Update account without authentication
    When Someone makes a PUT request to the /accounts/NL01INHO0000000002 API endpoint without an authentication token
    Then The server will return a number 401 unauthorized error

  Scenario: Update account performed by employee
    When An employee makes a PUT request to the /accounts/NL01INHO0000000002 API endpoint
    Then The server will return a number 204 noContent

  Scenario: Update account performed by customer
    When A customer makes a PUT request to the /accounts/NL01INHO0000000002 API endpoint updating fields they have access to
    Then The server will return a number 403 forbidden error


    #Post account scenarios
  Scenario: Create account without authentication
    When Someone makes a POST request to the /accounts API endpoint without an authentication token
    Then The server will return a number 401 unauthorized error

  Scenario: Create account performed by customer
    When A customer makes a POST request to the /accounts API endpoint
    Then The server will return a number 403 forbidden error

  Scenario: Create account performed by employee
    When An employee makes a POST request to the /accounts API endpoint
    Then The server will return a number 201 created

    #Delete account scenarios
  Scenario: Delete account without authentication
    When Someone makes a DELETE request to the /accounts/NL01INHO0000000002 API endpoint without an authentication token
    Then The server will return a number 401 unauthorized error

  Scenario: Delete account performed by customer
    When A customer makes a DELETE request to the /accounts/NL01INHO0000000002 API endpoint
    Then The server will return a number 403 forbidden error

  Scenario: Delete account performed by employee
    When An employee makes a DELETE request to the /accounts/NL01INHO0000000002 API endpoint
    Then The server will return a number 204 noContent