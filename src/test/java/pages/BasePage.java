package pages;

import io.appium.java_client.AppiumDriver;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class BasePage {

    protected AppiumDriver driver;
    protected WebDriverWait wait;
    protected static final Logger log = LoggerFactory.getLogger(BasePage.class);

    private static final long DEFAULT_WAIT_SECONDS = 20;
    private static final long SHORT_WAIT_SECONDS = 5;


    public BasePage(AppiumDriver driver) {
        if (driver == null) {
            log.error("AppiumDriver instance is null in BasePage constructor!");
            throw new IllegalArgumentException("Driver cannot be null.");
        }
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_SECONDS));
    }


    protected WebElement waitForVisibility(By locator) {
        log.debug("Waiting for visibility of element: {}", locator);
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            log.error("Timeout waiting for element {} to be visible.", locator, e);
            throw e;
        }
    }


    protected WebElement waitForPresence(By locator) {
        log.debug("Waiting for presence of element: {}", locator);
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (TimeoutException e) {
            log.error("Timeout waiting for element {} to be present.", locator, e);
            throw e;
        }
    }


    protected WebElement waitForElementToBeClickable(By locator) {
        log.debug("Waiting for element to be clickable: {}", locator);
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            log.error("Timeout waiting for element {} to be clickable.", locator, e);
            throw e;
        }
    }

    protected void clickElement(By locator) {
        log.info("Clicking element: {}", locator);
        waitForElementToBeClickable(locator).click();
    }


    protected void enterText(By locator, String text) {
        if (text == null) {
            log.warn("Attempting to enter null text into element: {}. Skipping.", locator);
            return;
        }
        log.info("Entering text '{}' into element: {}", text, locator);
        WebElement element = waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getElementText(By locator) {
        String text = waitForVisibility(locator).getText();
        log.debug("Retrieved text '{}' from element: {}", text, locator);
        return text;
    }


    protected boolean isElementDisplayed(By locator) {
        log.debug("Checking display status of element: {}", locator);
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT_SECONDS));
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            log.debug("Element {} is displayed.", locator);
            return true;
        } catch (NoSuchElementException | TimeoutException e) {
            log.debug("Element {} is not displayed (Timeout or Not Found).", locator);
            return false;
        }
    }
}
