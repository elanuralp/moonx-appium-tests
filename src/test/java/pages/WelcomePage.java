package pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;

public class WelcomePage extends BasePage {
    public WelcomePage(AppiumDriver driver) {
        super(driver);
    }

    public void tapNext() {
        WebElement nextButton = driver.findElement(AppiumBy.accessibilityId("Next"));
        nextButton.click();
    }
}
