package tests;

import base.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class OnboardingTest extends BaseTest {

    @Test(description = "TC_INPUT_03: Test birthday screen validation message")
    public void testBirthdayValidation() {
        log.info("Starting test: testBirthdayValidation");

        try {
            Thread.sleep(3000);
            WebElement welcomeNextButton = driver.findElement(AppiumBy.xpath("//XCUIElementTypeButton[@name='Next']"));
            welcomeNextButton.click();
            log.info("Clicked Next button on welcome screen");

            Thread.sleep(2000);

            WebElement birthdayNextButton = driver.findElement(AppiumBy.xpath("//XCUIElementTypeButton[@name='Next']"));
            birthdayNextButton.click();
            log.info("Clicked Next button on birthday screen without entering data");

            Thread.sleep(2000);

            boolean stillOnBirthdayScreen = !driver.findElements(
                    AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name, 'Tab 2')]")).isEmpty();

            Assert.assertTrue(stillOnBirthdayScreen,
                    "Validation failed - app navigated away from birthday screen");
            log.info("Validation confirmed - app correctly stayed on birthday screen");

        } catch (Exception e) {
            log.error("Test failed with exception", e);
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
}
