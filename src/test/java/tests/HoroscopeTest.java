package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.*;

public class HoroscopeTest extends BaseTest {

    @BeforeClass
    public void navigateToHomeScreen() throws InterruptedException {

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

        boolean onHome = homePage.isHomeScreenDisplayed();
        Assert.assertTrue(onHome, "Setup for HoroscopeTest failed: Could not reach Home screen after onboarding.");
    }

    @Test(description = "TC_HORO_02: Check Horoscope Text (Fails if API Key error is present)")
    public void testHoroscopeTextIsApiKeyError() {
        HomePage homePage = new HomePage(driver);
        boolean isApiErrorDisplayed = homePage.isApiKeyErrorDisplayed();
        Assert.assertFalse(isApiErrorDisplayed,
                "Failure Condition Detected: 'API key not valid' error IS displayed instead of horoscope text.");
    }
}
