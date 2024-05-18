package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import junit.framework.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.Booking;
import pages.BookingDates;

public class StepDefinition {
    WebDriver driver;
    WebDriverWait wait;
    private Response createResponse;
    private Booking booking;
    private int bookingId;
    Response getResponse;
    @Given("I am logged in as a standard user with username {string} and password {string}")
    public void i_am_logged_in_as_a_standard_user_with_username_and_password(String username, String password) {
        System.setProperty("webdriver.chrome.driver", "src/main/driver/chromedriver");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
        driver.get("https://www.saucedemo.com/");
        driver.findElement(By.id("user-name")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login-button")).click();
    }

    @When("I find the cheapest item and add it to the cart")
    public void i_find_the_cheapest_item_and_add_it_to_the_cart()  {
        WebElement filter = driver.findElement(By.xpath("//span[@class='select_container']"));
        WebElement selectLowToHigh = driver.findElement(By.xpath("//select//option[3]"));
        filter.click();selectLowToHigh.click();
        WebElement addToCartFirstItem = driver.findElement(By.xpath("(//div[@class='inventory_item_name '])[1]"));
        addToCartFirstItem.click();
        WebElement addToCart = driver.findElement(By.xpath("//*[contains(text(),'Add to cart')]"));
        addToCart.click();
    }
    
    @When("I continue shopping")
    public void i_continue_shopping() {
        driver.findElement(By.className("shopping_cart_link")).click();
        driver.findElement(By.xpath("//button[@name='continue-shopping']")).click();
    }

    @When("I proceed to checkout")
    public void i_proceed_to_checkout() {
        driver.findElement(By.className("shopping_cart_link")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("checkout"))).click();
        driver.findElement(By.id("first-name")).sendKeys("John");
        driver.findElement(By.id("last-name")).sendKeys("Doe");
        driver.findElement(By.id("postal-code")).sendKeys("12345");
        driver.findElement(By.id("continue")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("finish"))).click();
    }

    @Then("the checkout process should be successful")
    public void the_checkout_process_should_be_successful() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("complete-header")));
        String successMessage = driver.findElement(By.className("complete-header")).getText();
        assert successMessage.contains("THANK YOU FOR YOUR ORDER");
        driver.quit();
    }

    @Given("I create a new booking")
    public void createBooking() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        BookingDates bookingDates = new BookingDates();
        bookingDates.setCheckin("2024-05-21");
        bookingDates.setCheckout("2024-05-21");

        booking = new Booking();
        booking.setFirstname("John");
        booking.setLastname("Doe");
        booking.setTotalprice(100);
        booking.setDepositpaid(true);
        booking.setBookingdates(bookingDates);
        booking.setAdditionalneeds("Breakfast");

        // Send POST request to create booking
        createResponse = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(booking)
                .post("/booking");

        Assert.assertEquals(createResponse.getStatusCode(), 200);
    }
    
    @When("I retrieve the booking by ID")
    public void getBookingById() {
        bookingId = createResponse.jsonPath().getInt("bookingid");

        // Send GET request to get created booking details by ID
        getResponse = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .get("/booking/" + bookingId);
        Assert.assertEquals(getResponse.getStatusCode(), 200);
    }

    @Then("the booking details should match")
    public void verifyBookingDetails() {
        Booking returnedBooking = getResponse.as(Booking.class);
        Assert.assertEquals(returnedBooking.getFirstname(), booking.getFirstname());
        Assert.assertEquals(returnedBooking.getLastname(), booking.getLastname());
        Assert.assertEquals(returnedBooking.getTotalprice(), booking.getTotalprice());
        Assert.assertEquals(returnedBooking.isDepositpaid(), booking.isDepositpaid());
        Assert.assertEquals(returnedBooking.getBookingdates().getCheckin(), booking.getBookingdates().getCheckin());
        Assert.assertEquals(returnedBooking.getBookingdates().getCheckout(), booking.getBookingdates().getCheckout());
        Assert.assertEquals(returnedBooking.getAdditionalneeds(), booking.getAdditionalneeds());
    }
}
