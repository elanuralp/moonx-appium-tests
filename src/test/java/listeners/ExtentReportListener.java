package listeners;

import base.BaseTest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ExtentManager;

public class ExtentReportListener implements ITestListener {

    private static ExtentReports extent = ExtentManager.getInstance();
    private static ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        // Setup handled by ExtentManager
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        String description = result.getMethod().getDescription();
        ExtentTest test = extent.createTest(result.getMethod().getMethodName(),
                description != null ? description : ""); // Use description or empty string
        extentTestThreadLocal.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (extentTestThreadLocal.get() != null) {
            extentTestThreadLocal.get().log(Status.PASS, "Test Passed");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (extentTestThreadLocal.get() != null) {
            ExtentTest test = extentTestThreadLocal.get();
            test.log(Status.FAIL, "Test Failed");
            test.log(Status.FAIL, result.getThrowable());

            try {
                AppiumDriver driver = BaseTest.getDriver();
                if (driver instanceof TakesScreenshot) {
                    String screenshotBase64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
                    test.fail("Screenshot on failure:",
                            MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());
                } else {
                    test.log(Status.WARNING, "Driver does not support TakesScreenshot, cannot capture screenshot.");
                }
            } catch (Exception e) {
                test.log(Status.WARNING, "Failed to capture screenshot: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (extentTestThreadLocal.get() != null) {
            extentTestThreadLocal.get().log(Status.SKIP, "Test Skipped");
            if (result.getThrowable() != null) {
                extentTestThreadLocal.get().log(Status.SKIP, result.getThrowable());
            }
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // No action needed by default
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        onTestFailure(result);
    }
}
