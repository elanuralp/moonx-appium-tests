package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentManager {

    private static ExtentReports extent;
    private static String reportFileName = "ExtentReport-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".html";
    private static String fileSeparator = System.getProperty("file.separator");
    private static String reportFilepath = System.getProperty("user.dir") + fileSeparator + "test-output" + fileSeparator + reportFileName;

    public static ExtentReports getInstance() {
        if (extent == null)
            createInstance();
        return extent;
    }

    public static ExtentReports createInstance() {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFilepath);
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setDocumentTitle("Moonx App Test Report");
        sparkReporter.config().setEncoding("utf-8");
        sparkReporter.config().setReportName("Moonx Automation Test Results");
        sparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));

        return extent;
    }

    public static String getReportFilepath() {
        return reportFilepath;
    }
}
