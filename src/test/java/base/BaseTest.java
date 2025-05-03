package base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobilePlatform;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class BaseTest {

    private static ThreadLocal<AppiumDriver> driverThreadLocal = new ThreadLocal<>();
    protected AppiumDriver driver;
    private static final String APPIUM_SERVER_URL = "http://127.0.0.1:4723/";
    // protected static final Logger log = LoggerFactory.getLogger(BaseTest.class); // Keep if needed elsewhere

    public static AppiumDriver getDriver() {
        return driverThreadLocal.get();
    }

    @Parameters({ "platformName", "platformVersion", "deviceName", "appPath" })
    @BeforeClass
    public void setUpClass(
            @Optional("iOS") String platformName,
            @Optional("18.2") String platformVersion,
            @Optional("iPhone 16") String deviceName,
            @Optional("") String appPath
    ) throws MalformedURLException {

        AppiumDriver initializedDriver;

        if (appPath == null || appPath.isEmpty()) {
            appPath = "/Users/elanuralp/Development/flutter/moonx/build/ios/iphonesimulator/Runner.app";
        }

        File appFile = new File(appPath);
        if (!appFile.exists()) {
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
                options.setNoReset(false);
                options.setCapability("autoAcceptAlerts", false);
                options.setCapability("autoDismissAlerts", false);

                initializedDriver = new IOSDriver(appiumServerUrl, options);
            } else {
                throw new IllegalArgumentException(
                        "Only iOS is supported for this test"
                );
            }
            driverThreadLocal.set(initializedDriver);
            this.driver = initializedDriver;

        } catch (Exception e) {
            throw e;
        }
    }

    @AfterClass
    public void tearDownClass() {
        AppiumDriver driverToQuit = driverThreadLocal.get();
        if (driverToQuit != null) {
            driverToQuit.quit();
            driverThreadLocal.remove();
        }
        this.driver = null;
    }
}
