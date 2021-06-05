Feature: User tests
  Scenario: Get all users from the API without authentication
    When Someone makes a request to the /users API endpoint without an authentication token
    Then The server will return a 401 unauthorized

  Scenario: Get all users from the API performed by employee
    When An employee makes a request to the /users API endpoint
    Then The server will return list of 3 users

  Scenario: Get all users form the API performed by customer
    When A customer makes a request to the /users API endpoint
    Then The server will return a 403 forbidden

  Scenario: Get a single user from the API without authentication
    When Someone makes a request to the /users/1 API endpoint without an authentication token
    Then The server will return a 401 unauthorized

  Scenario: Get a single user form the API performed by employee
    When An employee makes a request to the /users/2 API endpoint
    Then The server server will return the user with the email address of alice@example.com

  Scenario: Get own user from the API performed by customer
    When A customer makes a request to the /users/2 API endpoint and they are that user
    Then The server server will return the user with the email address of alice@example.com

  Scenario: Get other user form the API performed by customer
    When A customer makes a request to the /users/1 API endpoint and they are that not the same user as requested
    Then The server will return a 403 forbidden

  Scenario: Update user without authentication
    When Someone makes a PUT request to the /users/2 API endpoint without an authentication token
    Then The server will return a 401 unauthorized