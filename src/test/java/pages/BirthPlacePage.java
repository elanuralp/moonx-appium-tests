package pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

public class BirthPlacePage extends BasePage {
    public BirthPlacePage(AppiumDriver driver) {
        super(driver);
    }

    public void enterPlace(String place) {
        try {
            // 1. Open the birth place picker (adjust the button index or locator as needed)
            List<WebElement> buttons = driver.findElements(AppiumBy.className("XCUIElementTypeButton"));
            boolean clicked = false;
            for (WebElement button : buttons) {
                String name = button.getAttribute("name");
                if (name != null && (name.equalsIgnoreCase("Select Birth Place") || name.contains("Birth Place"))) {
                    button.click();
                    clicked = true;
                    break;
                }
            }
            // If you know the index, you can use: buttons.get(X).click();
            if (!clicked && buttons.size() > 0) {
                // fallback: try the last button
                buttons.get(buttons.size() - 1).click();
            }

            // 2. Wait for the picker to appear
            Thread.sleep(500); // or use WebDriverWait if you prefer

            // 3. Find the picker wheel for city
            List<WebElement> pickers = driver.findElements(AppiumBy.xpath("//XCUIElementTypeOther[@value]"));
            if (pickers.isEmpty()) return;
            WebElement cityPicker = pickers.get(0); // Adjust index if needed

            // 4. Swipe until the desired city is selected
            for (int i = 0; i < 15; i++) {
                String currentValue = cityPicker.getAttribute("value");
                if (currentValue != null && currentValue.equalsIgnoreCase(place)) {
                    break;
                }
                swipeOnElement(cityPicker, true); // swipe up
                Thread.sleep(400);
            }
            // Try swiping down if not found
            for (int i = 0; i < 15; i++) {
                String currentValue = cityPicker.getAttribute("value");
                if (currentValue != null && currentValue.equalsIgnoreCase(place)) {
                    break;
                }
                swipeOnElement(cityPicker, false); // swipe down
                Thread.sleep(400);
            }

            // 5. Tap Done or outside picker to confirm
            List<WebElement> doneButtons = driver.findElements(AppiumBy.accessibilityId("Done"));
            if (!doneButtons.isEmpty()) {
                doneButtons.get(0).click();
                Thread.sleep(300);
            } else {
                tapOutsidePicker();
            }
        } catch (Exception ignored) {}
    }

    // Helper: swipe on picker element
    private void swipeOnElement(WebElement element, boolean swipeUp) {
        try {
            Point location = element.getLocation();
            int width  = element.getSize().getWidth();
            int height = element.getSize().getHeight();
            int centerX = location.getX() + width / 2;
            int centerY = location.getY() + height / 2;
            // swipe by a quarter of the wheel’s height:
            int offset = height / 4;

            int startY = swipeUp ? centerY + offset : centerY - offset;
            int endY   = swipeUp ? centerY - offset : centerY + offset;

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence swipe = new Sequence(finger, 1);
            swipe.addAction(
                    finger.createPointerMove(Duration.ZERO,
                            PointerInput.Origin.viewport(), centerX, startY));
            swipe.addAction(
                    finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            swipe.addAction(
                    finger.createPointerMove(Duration.ofMillis(200),
                            PointerInput.Origin.viewport(), centerX, endY));
            swipe.addAction(
                    finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(Collections.singletonList(swipe));
        } catch (Exception ignored) {}
    }


    // Helper: tap outside picker if no Done button
    private void tapOutsidePicker() {
        try {
            int width = driver.manage().window().getSize().getWidth();
            int height = driver.manage().window().getSize().getHeight();
            int tapX = width / 2;
            int tapY = (int) (height * 0.1); // 10% from the top

            org.openqa.selenium.interactions.PointerInput finger = new org.openqa.selenium.interactions.PointerInput(
                    org.openqa.selenium.interactions.PointerInput.Kind.TOUCH, "finger");
            org.openqa.selenium.interactions.Sequence tap = new org.openqa.selenium.interactions.Sequence(finger, 1);
            tap.addAction(finger.createPointerMove(Duration.ZERO, org.openqa.selenium.interactions.PointerInput.Origin.viewport(), tapX, tapY));
            tap.addAction(finger.createPointerDown(org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
            tap.addAction(finger.createPointerUp(org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(Collections.singletonList(tap));
            Thread.sleep(300);
        } catch (Exception ignored) {}
    }


    public void tapNext() {
        try {
            driver.findElement(AppiumBy.accessibilityId("Next")).click();
        } catch (Exception ignored) {}
    }

    public void selectBirthTime(String hour, String minute, String ampm) throws InterruptedException {
        // 1) Tap the “Enter Birth Time” button directly by its label
        WebElement timeBtn = driver.findElement(AppiumBy.iOSNsPredicateString(
                "label CONTAINS 'Enter Birth Time' or name CONTAINS 'Birth Time'"
        ));
        timeBtn.click();
        Thread.sleep(500);

        // 2) Locate your three custom wheels (value-based)
        List<WebElement> wheels = driver.findElements(AppiumBy.xpath(
                "//XCUIElementTypeOther[@value]"
        ));
        if (wheels.size() < 3) return;

        // 3) Debug: print out what values you see
        for (int i = 0; i < wheels.size(); i++) {
            System.out.println("Wheel[" + i + "] = " + wheels.get(i).getAttribute("value"));
        }

        // 4) Brute-force each wheel: up to 10 taps above, then 10 taps below
        bruteForceSet(wheels.get(0), hour);
        bruteForceSet(wheels.get(1), minute);
        bruteForceSet(wheels.get(2), ampm);

        // 5) Dismiss the picker by tapping outside
        Dimension sz = driver.manage().window().getSize();
        int x = sz.width / 2;
        int y = (int)(sz.height * 0.1);
        new TouchAction<>((PerformsTouchActions) driver)
                .press(new PointOption().withCoordinates(x, y))
                .release()
                .perform();
        Thread.sleep(300);
    }

    // Bounded, two-phase tap logic: first top half, then bottom half
    private void bruteForceSet(WebElement wheel, String target) throws InterruptedException {
        Point loc = wheel.getLocation();
        Dimension size = wheel.getSize();
        int cx = loc.getX() + size.width/2;
        int topY = loc.getY() + size.height/4;      // tap here to go up
        int botY = loc.getY() + size.height*3/4;    // tap here to go down

        // 10 taps on top
        for (int i = 0; i < 10; i++) {
            String cur = wheel.getAttribute("value");
            if (target.equalsIgnoreCase(cur)) return;
            new TouchAction<>((PerformsTouchActions) driver)
                    .tap(new PointOption().withCoordinates(cx, topY))
                    .perform();
            Thread.sleep(200);
        }
        // 10 taps on bottom
        for (int i = 0; i < 10; i++) {
            String cur = wheel.getAttribute("value");
            if (target.equalsIgnoreCase(cur)) return;
            new TouchAction<>((PerformsTouchActions) driver)
                    .tap(new PointOption().withCoordinates(cx, botY))
                    .perform();
            Thread.sleep(200);
        }
    }

}
