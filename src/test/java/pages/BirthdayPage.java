package pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

public class BirthdayPage extends BasePage {
    private static final String[] MONTHS = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    private static final Duration FAST_WAIT = Duration.ofMillis(1500);
    private static final Duration SAFE_WAIT = Duration.ofSeconds(5);

    public BirthdayPage(AppiumDriver driver) {
        super(driver);
    }

    public void selectBirthDate(String month, String day, String year) {
        try {
            By dateButtonLocator = AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`label CONTAINS '/'`][1]");
            WebDriverWait buttonWait = new WebDriverWait(driver, SAFE_WAIT);
            WebElement dateButton = buttonWait.until(ExpectedConditions.elementToBeClickable(dateButtonLocator));
            dateButton.click();
            WebDriverWait pickerWait = new WebDriverWait(driver, SAFE_WAIT);
            List<WebElement> pickers = pickerWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                    AppiumBy.xpath("//XCUIElementTypeOther[@value]"), 2
            ));
            if (pickers.size() >= 3) {
                setPickerValueAdaptively(pickers.get(0), month, "month");
                setPickerValueAdaptively(pickers.get(1), day, "numeric");
                setPickerValueAdaptively(pickers.get(2), year, "numeric");
                tapDoneOrOutside();
            } else {
                Assert.fail("Did not find enough date picker elements.");
            }
        } catch (Exception e) {
            Assert.fail("Failed during selectBirthDate execution.", e);
        }
    }

    public void selectBirthTime(String hour, String minute, String ampm) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, SAFE_WAIT);
        WebElement timeTriggerElement = null;
        By timeTriggerLocator = AppiumBy.xpath("(//XCUIElementTypeButton[contains(@name,'/') or contains(@label,'/') or string-length(@name)>5])[2]");

        try {
            timeTriggerElement = wait.until(ExpectedConditions.elementToBeClickable(timeTriggerLocator));
            timeTriggerElement.click();
        } catch (TimeoutException toe) {
            Assert.fail("Time trigger element (Positional XPath) not clickable within timeout (" + SAFE_WAIT.getSeconds() + "s). Locator: " + timeTriggerLocator, toe);
            return;
        } catch (Exception e) {
            Assert.fail("Could not find or click the TIME TRIGGER element (Positional XPath).", e);
            return;
        }

        WebDriverWait pickerWait = new WebDriverWait(driver, FAST_WAIT);

        try {
            List<WebElement> wheels = pickerWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                    AppiumBy.className("XCUIElementTypePickerWheel"), 2
            ));
            String formattedMinute = String.format("%02d", Integer.parseInt(minute));
            wheels.get(0).sendKeys(hour); Thread.sleep(200);
            wheels.get(1).sendKeys(formattedMinute); Thread.sleep(200);
            wheels.get(2).sendKeys(ampm);
            tapDoneOrOutside();
            return;
        } catch (TimeoutException toe) {
            // Fall through to next strategy
        } catch (Exception e) {
            // Fall through to next strategy
        }

        try {
            List<WebElement> customPickers = pickerWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                    AppiumBy.xpath("//XCUIElementTypeOther[@value]"), 2
            ));
            if (customPickers.size() >= 3) {
                setPickerValueAdaptively(customPickers.get(0), hour, "numeric");
                setPickerValueAdaptively(customPickers.get(1), minute, "numeric");
                setPickerValueAdaptively(customPickers.get(2), ampm, "ampm");
                tapDoneOrOutside();
            } else {
                Assert.fail("Did not find enough custom time picker elements for swipe interaction.");
            }
        } catch (TimeoutException toe) {
            Assert.fail("Custom time picker elements not found within timeout (" + FAST_WAIT.toMillis() + "ms).", toe);
        } catch (Exception e) {
            Assert.fail("Failed during custom time picker interaction.", e);
        }
    }

    private void setPickerValueAdaptively(WebElement picker, String targetValue, String type) throws InterruptedException {
        int maxSwipes = 40;
        boolean match = false;
        for (int i = 0; i < maxSwipes; i++) {
            String currentValue = null;
            try {
                Thread.sleep(150);
                currentValue = picker.getAttribute("value");
            } catch (Exception e) {
                if (e.getMessage() != null && e.getMessage().contains("ECONNREFUSED")) {
                    Assert.fail("WDA connection refused during picker interaction.", e);
                    return;
                }
                Thread.sleep(500);
                try {
                    currentValue = picker.getAttribute("value");
                } catch (Exception e2) {
                    Assert.fail("Failed to get picker attribute for " + targetValue + " after retry.", e2);
                    return;
                }
            }

            if (currentValue == null) {
                Thread.sleep(500);
                continue;
            }
            currentValue = currentValue.trim();

            int diff = 0;
            match = false;
            if (type.equals("numeric")) {
                int currentInt = parseIntSafe(currentValue);
                int targetInt = parseIntSafe(targetValue);
                if (currentInt == -1 || targetInt == -1) {
                    Thread.sleep(1000);
                    continue;
                }
                if (currentInt == targetInt) match = true;
                diff = currentInt - targetInt;
            } else if (type.equals("month")) {
                int currentIndex = monthToIndex(currentValue);
                int targetIndex = monthToIndex(targetValue);
                if (currentIndex == -1 || targetIndex == -1) {
                    Thread.sleep(1000);
                    continue;
                }
                if (currentIndex == targetIndex) match = true;
                diff = currentIndex - targetIndex;
            } else if (type.equals("ampm")) {
                String normCurrent = normalizeAmPm(currentValue);
                String normTarget = normalizeAmPm(targetValue);
                if (normCurrent.equals(normTarget)) {
                    match = true;
                    break;
                }
                swipeOnElementAdaptive(picker, true, 0.2);
                Thread.sleep(500);
                currentValue = picker.getAttribute("value");
                if (currentValue != null) {
                    normCurrent = normalizeAmPm(currentValue);
                    if (normCurrent.equals(normTarget)) {
                        match = true;
                        break;
                    } else {
                        swipeOnElementAdaptive(picker, false, 0.2);
                        Thread.sleep(500);
                    }
                }
                break;
            } else {
                if (currentValue.equalsIgnoreCase(targetValue)) match = true;
                diff = 0;
            }

            if (match) {
                break;
            }
            if (i == maxSwipes - 1) {
                break;
            }

            double ratio;
            int absDiff = Math.abs(diff);
            if (type.equals("numeric")) {
                if (absDiff >= 20) ratio = 0.5;
                else if (absDiff >= 10) ratio = 0.35;
                else if (absDiff >= 5) ratio = 0.25;
                else if (absDiff >= 3) ratio = 0.15;
                else { ratio = 0.15; }
            } else if (type.equals("month")) {
                if (absDiff >= 5) ratio = 0.3;
                else if (absDiff >= 3) ratio = 0.2;
                else ratio = 0.1;
            } else {
                ratio = 0.2;
            }
            boolean swipeDown = diff > 0;
            swipeOnElementAdaptive(picker, swipeDown, ratio);
            Thread.sleep(600);
        }
        if (!match) {
            Assert.fail("Failed to set picker value " + targetValue + " after " + maxSwipes + " swipes.");
        }
    }

    private int parseIntSafe(String value) {
        if (value == null) return -1;
        try {
            String cleanedValue = value.replaceAll("\\D", "");
            if (cleanedValue.isEmpty()) {
                return -1;
            }
            return Integer.parseInt(cleanedValue);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private int monthToIndex(String month) {
        if (month == null) return -1;
        String cleanedMonth = month.trim();
        for (int i = 0; i < MONTHS.length; i++) {
            if (MONTHS[i].equalsIgnoreCase(cleanedMonth)) return i;
        }
        return -1;
    }

    private String normalizeAmPm(String value) {
        if (value == null) return "";
        String lower = value.toLowerCase().replaceAll("[^a-z]", "");
        if (lower.contains("am")) return "am";
        if (lower.contains("pm")) return "pm";
        return lower;
    }

    private void swipeOnElementAdaptive(WebElement element, boolean swipeDown, double verticalOffsetRatio) {
        try {
            Point location = element.getLocation();
            Dimension size = element.getSize();
            int width = size.getWidth();
            int height = size.getHeight();
            if (height <= 0) {
                return;
            }
            int centerX = location.getX() + width / 2;
            int centerY = location.getY() + height / 2;
            int swipeDistance = Math.max(5, (int) (height * verticalOffsetRatio));
            int startY = centerY;
            int endY = swipeDown ? centerY + swipeDistance : centerY - swipeDistance;
            if (startY == endY) {
                return;
            }
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence swipe = new Sequence(finger, 1);
            swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, startY));
            swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            swipe.addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), centerX, endY));
            swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(Collections.singletonList(swipe));
        } catch (Exception e) {
            // Error during swipe is logged implicitly by test failure if it causes issues
        }
    }

    public void tapNext() {
        try {
            WebElement nextButton = new WebDriverWait(driver, SAFE_WAIT).until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("Next")));
            nextButton.click();
        } catch (Exception e) {
            Assert.fail("Failed to tap 'Next' button.", e);
        }
    }

    private void tapDoneOrOutside() {
        try {
            WebDriverWait doneWait = new WebDriverWait(driver, FAST_WAIT);
            WebElement doneButton = doneWait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("Done")));
            doneButton.click();
            Thread.sleep(200);
            return;
        } catch (Exception e) {
            // Could not find 'Done', proceed to tap outside
        }
        try {
            int width = driver.manage().window().getSize().getWidth();
            int height = driver.manage().window().getSize().getHeight();
            int tapX = width / 2;
            int tapY = (int) (height * 0.1);
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence tap = new Sequence(finger, 1);
            tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), tapX, tapY));
            tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            tap.addAction(finger.createPointerMove(Duration.ofMillis(50), PointerInput.Origin.pointer(), 0, 0));
            tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(Collections.singletonList(tap));
            Thread.sleep(200);
        } catch (Exception e) {
            // Error tapping outside is logged implicitly by test failure if it causes issues
        }
    }
}
