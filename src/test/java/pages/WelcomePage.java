package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class WelcomePage extends BasePage {

    private final By nextButton = AppiumBy.xpath("//XCUIElementTypeButton[@name='Next']");


    private final By welcomeText = AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name, 'Welcome')]");

    public WelcomePage(AppiumDriver driver) {
        super(driver);
    }

    public BirthdayPage clickNext() {
        log.info("Clicking Next button on Welcome screen");
        clickElement(nextButton);
        return new BirthdayPage(driver);
    }

    public boolean isWelcomeTextVisible() {
        log.debug("Checking visibility of Welcome text");
        return isElementDisplayed(welcomeText);
    }
}
