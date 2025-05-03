package tests;

import base.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.BirthPlacePage;
import pages.BirthdayPage;
import pages.HomePage;
import pages.WelcomePage;

public class OnboardingTest extends BaseTest {

    @Test(description = "TC_INPUT_03: Test birthday screen validation message")
    public void testBirthdayValidation() {
        try {
            Thread.sleep(3000);
            WebElement welcomeNextButton = driver.findElement(AppiumBy.xpath("//XCUIElementTypeButton[@name='Next']"));
            welcomeNextButton.click();
            Thread.sleep(2000);
            WebElement birthdayNextButton = driver.findElement(AppiumBy.xpath("//XCUIElementTypeButton[@name='Next']"));
            birthdayNextButton.click();
            Thread.sleep(2000);
            boolean stillOnBirthdayScreen = !driver.findElements(
                    AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name, 'Tab 2')]")).isEmpty();
            Assert.assertTrue(stillOnBirthdayScreen,
                    "Validation failed - app navigated away from birthday screen");
        } catch (Exception e) {
            Assert.fail("Test failed: " + e.getMessage());
        }
    }

    @Test(description = "TC_INPUT_01: Complete Onboarding Flow")
    public void testCompleteOnboardingFlow() throws InterruptedException {
        WelcomePage welcomePage = new WelcomePage(driver);
        BirthdayPage birthdayPage = new BirthdayPage(driver);
        BirthPlacePage birthPlacePage = new BirthPlacePage(driver);
        HomePage homePage = new HomePage(driver);

        welcomePage.tapNext();

        birthdayPage.selectBirthDate("June", "10", "2001");
        birthdayPage.selectBirthTime("11", "05", "AM");
        birthdayPage.tapNext();

        birthPlacePage.enterPlace("Istanbul");
        birthPlacePage.tapNext();

        boolean onHomeScreen = homePage.isHomeScreenDisplayed();
        Assert.assertTrue(onHomeScreen,
                "Failed to verify navigation to the Home screen after onboarding.");
    }
}
