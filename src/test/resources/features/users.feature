Feature: User tests

  Scenario: Login without emailaddress
    When Someone try to login without emailaddress
    Then The server will return a 422 unprocessable_entity

  Scenario: Login without password
    When Someone try to login without password
    Then The server will return a 401 unauthorized

  Scenario: Login without emailaddress and without password
    When Someone try to login without emailaddress and without password
    Then The server will return a 422 unprocessable_entity

  Scenario: Login with wrong emailaddress format
    When Someone try to login with wrong emailaddress format
    Then The server will return a 422 unprocessable_entity

  Scenario: Login with wrong emailaddress and password combination
    When Someone try to login with wrong emailaddress and password combination
    Then The server will return a 401 unauthorized




  Scenario: Get all users from the API without authentication
    When Someone makes a request to the /users API endpoint without an authentication token
    Then The server will return a 401 unauthorized

  Scenario: Get all users from the API performed by employee
    When An employee makes a request to the /users API endpoint
    Then The server will return list of 3 users
    And The first names will be "Alice", "Bob", and "Charlie"

  Scenario: Get all users form the API performed by customer
    When A customer makes a request to the /users API endpoint
    Then The server will return a 403 forbidden

  Scenario: Get a single user from the API without authentication
    When Someone makes a request to the /users/1 API endpoint without an authentication token
    Then The server will return a 401 unauthorized

  Scenario: Get a single user form the API performed by employee
    When An employee makes a request to the /users/2 API endpoint
    Then The server will return a 200 ok
    And The returned JSON objects contains a field of "emailAddress" with value of "bob@example.com"
    And The returned JSON objects contains a field of "firstName" with value of "Bob"
    And The returned JSON objects contains a field of "lastName" with value of "Bobson"
    And The returned JSON objects contains a field of "birthDate" with value of "2012-12-12"
    And The returned JSON objects contains a field of "phone" with value of "+31 6 87654321"
    And The returned JSON objects contains a field of "dayLimit" with value of 2000.0
    And The returned JSON objects contains a field of "transactionLimit" with value of 50.0

  Scenario: Get own user from the API performed by customer
    When A customer makes a request to the /users/2 API endpoint and they are that user
    Then The server will return a 200 ok
    And The returned JSON objects contains a field of "emailAddress" with value of "bob@example.com"
    And The returned JSON objects contains a field of "firstName" with value of "Bob"
    And The returned JSON objects contains a field of "lastName" with value of "Bobson"
    And The returned JSON objects contains a field of "birthDate" with value of "2012-12-12"
    And The returned JSON objects contains a field of "phone" with value of "+31 6 87654321"
    And The returned JSON objects contains a field of "dayLimit" with value of 2000.0
    And The returned JSON objects contains a field of "transactionLimit" with value of 50.0

  Scenario: Get other user form the API performed by customer
    When A customer makes a request to the /users/1 API endpoint and they are that not the same user as requested
    Then The server will return a 403 forbidden

  Scenario: Update user without authentication
    When Someone makes a PUT request to the /users/2 API endpoint without an authentication token
    Then The server will return a 401 unauthorized

  Scenario: Update user performed by employee
    When An employee makes a PUT request to the /users/2 API endpoint
    Then The server will return a 200 ok

  Scenario: Update user performed by customer
    When A customer makes a PUT request to the /users/2 API endpoint updating fields they have access to
    Then The server will return a 200 ok

  Scenario: Update user blacklisted fields performed by customer
    When A customer makes a PUT request to the /users/2 API endpoint updating fields they have do not access to
    Then The server will return a 403 forbidden
    And The returned JSON exception object contains a field of "message" with value of "Customers cannot change their firstname of their own."

  Scenario: Update user blacklisted fields performed by employee
    When An employee makes a PUT request to the /users/2 API endpoint updating fields the customer does not have access to
    Then The server will return a 200 ok

  Scenario: Create user without authentication
    When Someone makes a POST request to the /users API endpoint without an authentication token
    Then The server will return a 401 unauthorized

  Scenario: Create user performed by customer
    When A customer makes a POST request to the /users API endpoint
    Then The server will return a 403 forbidden

  Scenario: Create user performed by employee
    When An employee makes a POST request to the /users API endpoint
    Then The server will return a 200 ok

  Scenario: Create user performed by employee with an incomplete body
    When An employee makes a POST request to the /users API endpoint and does not provide a valid post body
    Then The server will return a 400 bad request

  Scenario: Delete user without authentication
    When Someone makes a DELETE request to the /users/7 API endpoint without an authentication token
    Then The server will return a 401 unauthorized

  Scenario: Delete user performed by customer
    When A customer makes a DELETE request to the /users/7 API endpoint
    Then The server will return a 403 forbidden

  Scenario: Delete user performed by employee
    When An employee makes a DELETE request to the /users/7 API endpoint
    Then The server will return a 200 ok