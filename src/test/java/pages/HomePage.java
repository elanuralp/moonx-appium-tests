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

public class HomePage extends BasePage {

    private static final By HOME_SCREEN_INDICATOR = AppiumBy.name("Home\nTab 1 of 3");
    private static final By LOCATION_ALERT_CONTAINER = AppiumBy.className("XCUIElementTypeAlert");
    private static final By ALLOW_WHILE_USING_APP_BUTTON = AppiumBy.accessibilityId("Allow While Using App");

    private static final Duration POPUP_WAIT_DURATION = Duration.ofSeconds(15);
    private static final Duration VERIFICATION_WAIT = Duration.ofSeconds(10);

    public HomePage(AppiumDriver driver) {
        super(driver);
    }

    public boolean isHomeScreenDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, VERIFICATION_WAIT);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(HOME_SCREEN_INDICATOR));
            return true;
        } catch (TimeoutException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
