@update_place @google_maps
Feature: To add a new place

  @update_place_positive
  Scenario Outline: <TestCase>: Verify that the PUT /maps/api/place/update/json endpoint returns a successful response with a valid request body
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
    When the user sends a PUT request to the "/maps/api/place/update/json" endpoint with the following body:
      | field   | value             |
      | address | <updated address> |
      | key     | qaclick123        |
    Then user should get the response code 200
    And user validates the update place success response with the following data:
      | field  | value |
      | msg | Address successfully updated    |
    And user validates the response against the JSON schema "updatePlaceSchema.json"

    Examples:
      | TestCase | name            | address                   | language | updated address |
      | TC01     | Frontline house | 29, side layout, cohen 09 | French   | Sector Techzone |

  @update_place_negative
  Scenario Outline: <TestCase>: Verify that the PUT /maps/api/place/update/json endpoint returns an error response with a in b valid request body
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
    When the user sends a PUT request to the "/maps/api/place/update/json" endpoint with invalid request body:
      | field   | value             |
      | place_id | invalid place_id |
      | address | <updated address> |
      | key     | qaclick123        |
    Then user should get the response code 404
    And user validates the update place error response with the following data:
      | field | value                                                               |
      | msg   | Update address operation failed, looks like the data doesn't exists |

    Examples:
      | TestCase | name            | address                   | language |updated address |
      | TC02     | Frontline house | 29, side layout, cohen 09 | French   |Sector Techzone |
