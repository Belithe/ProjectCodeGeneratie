Feature: User tests
  Scenario: Get all users from the API without authentication
    When I make a request to the /users API endpoint without an authentication token
    Then The server will return a 403 unauthorized

  Scenario: Get all users from the API performed by employee
    When An employee makes a request to the /users API endpoint
    Then The server will return list of users