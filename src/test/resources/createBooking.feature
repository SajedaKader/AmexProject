Feature: Create and Retrieve Booking

    Scenario: Create a new booking and verify details
        Given I create a new booking
        When I retrieve the booking by ID
        Then the booking details should match