Feature: Purchase Cheapest Item 
As a standard user
User should in, find the cheapest item with the help of filter by selecting low to high, add it to the cart, continue shopping, and process checkout successfully

  Scenario: Purchase the cheapest item
    Given I am logged in as a standard user with username "standard_user" and password "secret_sauce"
    When I find the cheapest item and add it to the cart
    And I continue shopping
    And I proceed to checkout
    Then the checkout process should be successful
    
  Scenario: Create a new booking and verify details
        Given I create a new booking
        When I retrieve the booking by ID
        Then the booking details should match