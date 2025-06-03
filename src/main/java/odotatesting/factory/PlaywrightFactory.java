package odotatesting.factory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.ScreenshotType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import odotatesting.constants.AppConstants;

public class PlaywrightFactory {

    protected static final Logger logger = LogManager.getLogger(PlaywrightFactory.class);

    private Playwright playwright;
    private Browser browser;
    private BrowserContext browserContext;

    private static final ThreadLocal<Page> tlPage = new ThreadLocal<>();
    private static final ThreadLocal<PlaywrightFactory> tlFactory = new ThreadLocal<>();

    public Page getPage() {
        return tlPage.get();
    }

    public static PlaywrightFactory getFactoryInstance() {
        return tlFactory.get();
    }

    public Page initializeBrowser(Properties properties) {
        String browserName = properties.getProperty("browser").trim();
        boolean headlessFlag = Boolean.parseBoolean(properties.getProperty("headless", "true"));

        playwright = Playwright.create();

        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();

        switch (browserName.toLowerCase()) {
            case "chromium":
                launchOptions.setHeadless(headlessFlag).setArgs(List.of("--start-maximized"));
                browser = playwright.chromium().launch(launchOptions);
                break;
            case "firefox":
                launchOptions.setHeadless(headlessFlag);
                browser = playwright.firefox().launch(launchOptions);
                break;
            case "webkit":
                launchOptions.setHeadless(headlessFlag);
                browser = playwright.webkit().launch(launchOptions);
                break;
            case "chrome":
                launchOptions.setChannel("chrome").setHeadless(headlessFlag);
                launchOptions.setArgs(List.of("--start-maximized"));
                browser = playwright.chromium().launch(launchOptions);
                break;
            default:
                logger.error("Invalid browser name: {}", browserName);
                break;
        }

        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions();
        contextOptions.setViewportSize(null);

        browserContext = browser.newContext(contextOptions);
        Page currentPage = browserContext.newPage();
        currentPage.navigate(AppConstants.BASE_URL_WEB);

        // Store the current Page and PlaywrightFactory instance in ThreadLocal
        tlPage.set(currentPage);
        tlFactory.set(this);

        return currentPage;
    }

    public void closeBrowser() {
        if (browserContext != null) {
            browserContext.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
        // Remove the ThreadLocal value after use
        tlPage.remove();
        tlFactory.remove();
    }

    public String takeScreenshot(String scenarioName) {
        Page currentPage = getPage();
        if (currentPage == null) {
            System.err.println("Page instance is null. Cannot take screenshot.");
            return null;
        }
        String screenshotDir = System.getProperty("user.dir") + "/reports/screenshots/";
        Path screenshotDirPath = Paths.get(screenshotDir);

        try {
            Files.createDirectories(screenshotDirPath); // Creates directories if they don't exist
        } catch (Exception e) {
            System.err.println("Failed to create screenshot directory." + e.getMessage());
            return null;
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = now.format(formatter);

        // Clean scenario name for filename safety
        String cleanScenarioName = scenarioName.replaceAll("[^a-zA-Z0-9.-]", "_");
        String filename = cleanScenarioName + "_" + timestamp + ".png";

        Path fullPath = screenshotDirPath.resolve(filename);

        try {
            currentPage.screenshot(new Page.ScreenshotOptions()
                    .setPath(fullPath)
                    .setType(ScreenshotType.PNG)
                    .setFullPage(true));

            String relativePathForReport = "./screenshots/" + filename; // Path relative to the HTML report

            System.out.println("Screenshot saved to: " + fullPath);
            System.out.println("Path for report: " + relativePathForReport);
            return relativePathForReport;
        } catch (Exception e) {
            System.err.println("Failed to take screenshot: " + e.getMessage());
            return null;
        }
    }
}