package base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobilePlatform;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseTest {

    protected AppiumDriver driver;
    private static final String APPIUM_SERVER_URL = "http://127.0.0.1:4723/";

    protected static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    @Parameters({ "platformName", "platformVersion", "deviceName", "appPath" })
    @BeforeMethod
    public void setUp(
            @Optional("iOS") String platformName,
            @Optional("18.2") String platformVersion,
            @Optional("iPhone 16") String deviceName,
            @Optional("") String appPath
    ) throws MalformedURLException {
        if (appPath == null || appPath.isEmpty()) {
            appPath = "/Users/elanuralp/Development/flutter/moonx/build/ios/iphonesimulator/Runner.app";
            log.info("Using hardcoded app path: {}", appPath);
        }

        log.info("Setting up driver for: {} v{} on {} for app: {}",
                platformName, platformVersion, deviceName, appPath);

        File appFile = new File(appPath);
        if (!appFile.exists()) {
            log.error("App not found at path: {}", appPath);
            throw new IllegalArgumentException("App not found at specified path: " + appPath);
        }

        URL appiumServerUrl = new URL(APPIUM_SERVER_URL);

        try {
            if (platformName.equalsIgnoreCase(MobilePlatform.IOS)) {
                XCUITestOptions options = new XCUITestOptions();
                options.setPlatformName("iOS");
                options.setPlatformVersion(platformVersion);
                options.setDeviceName(deviceName);
                options.setApp(appPath);
                options.setAutomationName(AutomationName.IOS_XCUI_TEST);
                options.setWdaLaunchTimeout(Duration.ofSeconds(120));

                driver = new IOSDriver(appiumServerUrl, options);
                log.info("iOS driver initialized successfully");
            } else {
                throw new IllegalArgumentException(
                        "Only iOS is supported for this test"
                );
            }
        } catch (Exception e) {
            log.error("Error initializing driver: {}", e.getMessage(), e);
            throw e;
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        log.info("Implicit wait set to 15 seconds.");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            log.info("Tearing down driver...");
            driver.quit();
            log.info("Driver quit successfully.");
        } else {
            log.warn("Driver was null in tearDown, nothing to quit.");
        }
    }
}
