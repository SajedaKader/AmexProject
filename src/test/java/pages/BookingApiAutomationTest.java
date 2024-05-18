package pages;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class BookingApiAutomationTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
    }

    @Test
    public void testCreateAndGetBooking() {
        // Create booking payload
        BookingDates bookingDates = new BookingDates();
        bookingDates.setCheckin("2023-05-01");
        bookingDates.setCheckout("2023-05-10");

        Booking booking = new Booking();
        booking.setFirstname("John");
        booking.setLastname("Doe");
        booking.setTotalprice(123);
        booking.setDepositpaid(true);
        booking.setBookingdates(bookingDates);
        booking.setAdditionalneeds("Breakfast");

        // Send POST request to create booking
        Response createResponse = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(booking)
                .post("/booking");
        Assert.assertEquals(createResponse.getStatusCode(), 200);
        int bookingId = createResponse.jsonPath().getInt("bookingid");

        // Send GET request to get created booking details by ID
        Response getResponse = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .get("/booking/" + bookingId);
        Assert.assertEquals(getResponse.getStatusCode(), 200);
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

