Feature: User tests
  Scenario: Get all users from the API
    When I make a request to the /users API endpoint
    Then The server will return a 403 unauthorized