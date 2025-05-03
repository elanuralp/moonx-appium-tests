package pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class HomePage extends BasePage {

    private static final By HOME_TAB_BUTTON = AppiumBy.name("Home\nTab 1 of 3");
    private static final By MEDITATION_TAB_BUTTON = AppiumBy.name("Meditation\nTab 2 of 3");
    private static final By PROFILE_TAB_BUTTON = AppiumBy.name("Profile\nTab 3 of 3");
    private static final By HOROSCOPE_SECTION_INDICATOR = AppiumBy.xpath("//XCUIElementTypeImage[contains(@name, 'Your Daily Horoscope')]");
    private static final By API_KEY_ERROR_TEXT = AppiumBy.xpath("//XCUIElementTypeStaticText[@name='API key not valid. Please pass a valid API key.']");
    private static final By LOCATION_ALERT_CONTAINER = AppiumBy.className("XCUIElementTypeAlert");
    private static final By ALLOW_WHILE_USING_APP_BUTTON = AppiumBy.accessibilityId("Allow While Using App");

    private static final Duration POPUP_WAIT_DURATION = Duration.ofSeconds(15);
    private static final Duration VERIFICATION_WAIT = Duration.ofSeconds(10);
    private static final Duration TAP_WAIT = Duration.ofSeconds(5);

    public HomePage(AppiumDriver driver) {
        super(driver);
    }


    public boolean isHomeScreenDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, VERIFICATION_WAIT);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(HOME_TAB_BUTTON));
            return true;
        } catch (TimeoutException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }


    public boolean isApiKeyErrorDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, VERIFICATION_WAIT);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(API_KEY_ERROR_TEXT));
            return true;
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void tapMeditationTab() {
        WebDriverWait wait = new WebDriverWait(driver, TAP_WAIT);
        try {
            WebElement meditationTab = wait.until(ExpectedConditions.elementToBeClickable(MEDITATION_TAB_BUTTON));
            meditationTab.click();
        } catch (Exception e) {
            Assert.fail("Failed to tap Meditation tab.", e);
        }
    }

    public void tapProfileTab() {
        WebDriverWait wait = new WebDriverWait(driver, TAP_WAIT);
        try {
            WebElement profileTab = wait.until(ExpectedConditions.elementToBeClickable(PROFILE_TAB_BUTTON));
            profileTab.click();
        } catch (Exception e) {
            Assert.fail("Failed to tap Profile tab.", e);
        }
    }

    public void tapHomeTab() {
        WebDriverWait wait = new WebDriverWait(driver, TAP_WAIT);
        try {
            WebElement homeTab = wait.until(ExpectedConditions.elementToBeClickable(HOME_TAB_BUTTON));
            homeTab.click();
        } catch (Exception e) {
            Assert.fail("Failed to tap Home tab.", e);
        }
    }
}
