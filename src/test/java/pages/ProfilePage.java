package pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProfilePage extends BasePage {

    private static final By PROFILE_SCREEN_INDICATOR = AppiumBy.accessibilityId("Profile\nTab 3 of 3");

    private static final Duration VERIFICATION_WAIT = Duration.ofSeconds(10);

    public ProfilePage(AppiumDriver driver) {
        super(driver);
    }

    public boolean isProfileScreenDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, VERIFICATION_WAIT);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(PROFILE_SCREEN_INDICATOR));
            return true;
        } catch (TimeoutException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
