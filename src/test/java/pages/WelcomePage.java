package pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class WelcomePage extends BasePage {

    private static final By NEXT_BUTTON = AppiumBy.accessibilityId("Next");

    private static final Duration INTERACTION_WAIT = Duration.ofSeconds(10);

    public WelcomePage(AppiumDriver driver) {
        super(driver);
    }

    public void tapNext() {
        WebDriverWait wait = new WebDriverWait(driver, INTERACTION_WAIT);
        try {
            WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(NEXT_BUTTON));
            nextButton.click();
        } catch (TimeoutException e) {
            Assert.fail("Welcome screen 'Next' button not clickable.", e);
        } catch (Exception e) {
            Assert.fail("Failed to tap Welcome screen 'Next' button.", e);
        }
    }
}
