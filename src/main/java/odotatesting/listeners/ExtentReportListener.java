package odotatesting.listeners;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.UUID;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import odotatesting.factory.PlaywrightFactory;
import odotatesting.utils.InitializeProperties;

public class ExtentReportListener implements ITestListener, ISuiteListener {

    private static final String OUTPUT_FOLDER = "./reports/";
    private static final String FILE_NAME = "extent-report.html";
    private static final String UUID_KEY = "executionId";

    private static final Logger logger = LogManager.getLogger(ExtentReportListener.class);

    public static ExtentReports extentReports = init();
    public static ThreadLocal<ExtentTest> extentTestThread = new ThreadLocal<>();

    private static ExtentReports init() {
        Properties properties = InitializeProperties.loadProperties();
        Path path = Paths.get(OUTPUT_FOLDER);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            }catch (IOException e) {
                throw new RuntimeException("Couldn't create reports directory: " + e.getMessage(), e);
            }
        }

        extentReports = new ExtentReports();
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(OUTPUT_FOLDER + FILE_NAME);

        sparkReporter.config().setDocumentTitle("Opendota Playwright Automation Report");
        sparkReporter.config().setReportName("Test Results");
        sparkReporter.config().setTheme(Theme.DARK);

        extentReports.attachReporter(sparkReporter);
        extentReports.setSystemInfo("OS", System.getProperty("os.name"));
        extentReports.setSystemInfo("Browser", properties.getProperty("browser"));
        extentReports.setSystemInfo("Headless", properties.getProperty("headless"));

        return extentReports;

    }

    @Override
    public void onStart(ITestContext context) {
        String suiteExecutionId = UUID.randomUUID().toString();
        ThreadContext.put(UUID_KEY, suiteExecutionId);
        ThreadContext.put("executionId", suiteExecutionId);
        logger.info("===========Test=Execution=Started============");
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extentReports.createTest(result.getMethod().getMethodName());
        extentTestThread.set(test);
        extentTestThread.get().log(Status.INFO,"UUID: " + ThreadContext.get(UUID_KEY));
        logger.info("Test started: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTestThread.get().log(Status.PASS,"Test passed: " + result.getMethod().getMethodName());
        logger.info("Test passed: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: {}", result.getMethod().getMethodName());
        ExtentTest currentTest = extentTestThread.get();
        currentTest.log(Status.FAIL, "Test failed: " + result.getMethod().getMethodName());
        currentTest.log(Status.FAIL, result.getThrowable().getMessage());
        currentTest.log(Status.FAIL, result.getThrowable());

        PlaywrightFactory factory = PlaywrightFactory.getFactoryInstance();
        String scenarioName = result.getMethod().getMethodName();

        String screenshotPath = factory.takeScreenshot(scenarioName);
        currentTest.fail("Screenshot: ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        extentTestThread.get().log(Status.SKIP, "Test skipped: " + result.getMethod().getMethodName());
        extentTestThread.get().log(Status.INFO, result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        extentReports.flush();
        logger.info("===========Test=Execution=Finished===========");
        ThreadContext.remove("executionId");
    }
}
