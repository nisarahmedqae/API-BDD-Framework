@bookerAPI @createBooking
Feature: To create a new booking in restful-booker

  @createBookingDataTable
  Scenario Outline: <TestCase> To create new booking using cucumber Data Table
    #Given user has access to endpoint "/booking"
    When user creates a booking
      | firstname   | lastname   | totalprice   | depositpaid   | checkin   | checkout   | additionalneeds   |
      | <firstname> | <lastname> | <totalprice> | <depositpaid> | <checkin> | <checkout> | <additionalneeds> |
    Then user should get the response code 200
    And user validates the response with JSON schema "createBookingSchema.json"

    Examples:
      | TestCase | firstname | lastname | totalprice | depositpaid | checkin    | checkout   | additionalneeds |
      | TC01     | John      | Doe      | 1200       | true        | 2021-05-05 | 2021-05-15 | Breakfast       |
   #   | TC02     | Jane      | Doe      | 2400       | false       | 2021-06-01 | 2021-07-10 | Dinner          |


