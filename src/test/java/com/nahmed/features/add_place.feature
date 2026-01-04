@add_place @google_maps
Feature: To add a new place

  @add_place
  Scenario Outline: <TestCase>: Verify that the POST /maps/api/place/add/json endpoint returns a successful response with a valid request body
    When the user sends a POST request to the "/maps/api/place/add/json" endpoint with the following body:
      | field        | value              |
      | lat          | -38.383494         |
      | lng          | 33.427362          |
      | accuracy     | 50                 |
      | name         | <name>             |
      | phone_number | (+91) 983 893 3937 |
      | address      | <address>          |
      | website      | http://google.com  |
      | language     | <language>         |
      | types        | shoe park, shop    |
    Then user should get the response code 200
    And user validates the add place success response with the following data:
      | field  | value |
      | status | OK    |
      | scope  | APP   |
    And user validates the response against the JSON schema "addPlaceSchema.json"

    Examples:
      | TestCase | name            | address                   | language |
      | TC01     | Frontline house | 29, side layout, cohen 09 | French   |
      | TC02     | GN House        | Sector Techzone           | German   |

