package odotatesting.listeners;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ExtentReportListener implements ITestListener {

    public ExtentSparkReporter sparkReporter;
    public ExtentReports extentReports;
    public ExtentTest extentTest;

    Properties properties = initializeProperties();

    @Override
    public void onStart(ITestContext context) {
        sparkReporter = new ExtentSparkReporter("./reports/extent-report.html");
        sparkReporter.config().setDocumentTitle("Opendota Playwright Automated Testing Report");
        sparkReporter.config().setReportName("Test Results");
        sparkReporter.config().setTheme(Theme.DARK);

        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
    }

    @Override
    public void onTestStart(ITestResult result) {
        extentReports.setSystemInfo("OS", System.getProperty("os.name"));
        extentReports.setSystemInfo("Browser", properties.getProperty("browser"));
    }

    public void onTestSuccess(ITestResult result) {
        extentTest = extentReports.createTest(result.getMethod().getMethodName());
        extentTest.log(Status.PASS,"Test passed " + result.getMethod().getMethodName());
    }

    public void onTestFailure(ITestResult result) {
        extentTest = extentReports.createTest(result.getMethod().getMethodName());
        extentTest.log(Status.FAIL, "Test failed: " + result.getMethod().getMethodName());
        extentTest.log(Status.FAIL, result.getThrowable().getMessage());
        extentTest.log(Status.FAIL, result.getThrowable());
    }

    public void onTestSkipped(ITestResult result) {
        extentTest = extentReports.createTest(result.getMethod().getMethodName());
        extentTest.log(Status.SKIP, "Test skipped: " + result.getMethod().getMethodName());
        extentTest.log(Status.INFO, result.getThrowable().getMessage());
    }

    public void onFinish(ITestContext context) {
        extentReports.flush();
    }

    //can be moved to utility package
    public Properties initializeProperties() {
        try {
            FileInputStream input = new FileInputStream("./src/test/resources/config/config.properties");
            properties = new Properties();
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return properties;
    }

}
