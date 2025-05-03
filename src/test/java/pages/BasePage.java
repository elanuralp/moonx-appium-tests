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

    public BasePage(AppiumDriver driver) {
        if (driver == null) {
            log.error("AppiumDriver instance is null in BasePage constructor!");
            throw new IllegalArgumentException("Driver cannot be null.");
        }
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_SECONDS));
    }
}
