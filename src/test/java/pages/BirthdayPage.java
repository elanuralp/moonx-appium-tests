package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;


public class BirthdayPage extends BasePage {


    private final By nextButton = AppiumBy.accessibilityId("birthday_next_button");


    private final By validationMessage = AppiumBy.accessibilityId("birthday_validation_message");



    public BirthdayPage(AppiumDriver driver) {
        super(driver);
    }

    public void clickNext() {
        log.info("Clicking Next button on Birthday screen");
        clickElement(nextButton);

    }

    public boolean isValidationMessageDisplayed() {
        log.debug("Checking visibility of validation message");
        // Use the isElementDisplayed helper method from BasePage
        return isElementDisplayed(validationMessage);
    }


    public String getValidationMessageText() {
        log.debug("Getting text from validation message");
        return getElementText(validationMessage);
    }


}
