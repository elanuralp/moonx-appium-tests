package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.*;

public class NavigationTest extends BaseTest {

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
        Assert.assertTrue(onHome, "Setup for NavigationTest failed: Could not reach Home screen after onboarding.");
    }

    @Test(description = "TC_NAV_01: Bottom Navigation")
    public void testBottomNavigation() {
        HomePage homePage = new HomePage(driver);
        MeditationPage meditationPage = new MeditationPage(driver);
        ProfilePage profilePage = new ProfilePage(driver);

        homePage.tapMeditationTab();
        Assert.assertTrue(meditationPage.isMeditationScreenDisplayed(), "Failed to verify navigation to Meditation screen.");

        homePage.tapProfileTab();
        Assert.assertTrue(profilePage.isProfileScreenDisplayed(), "Failed to verify navigation to Profile screen.");

        homePage.tapHomeTab();
        Assert.assertTrue(homePage.isHomeScreenDisplayed(), "Failed to verify navigation back to Home screen.");
    }
}
