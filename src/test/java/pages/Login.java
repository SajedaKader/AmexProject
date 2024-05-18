package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.annotations.BeforeMethod;

public class Login {
    @FindBy(xpath = "")
    private WebElement userName;
    @FindBy(xpath = "")
    private WebElement password;
    @FindBy(xpath = "")
    private WebElement signInButton;
    public void signIn() {
        System.out.println("Hello");
    }
}
