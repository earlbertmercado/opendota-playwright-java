package odotatesting.factory;

import java.util.List;
import java.util.Properties;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
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
        contextOptions.setViewportSize(null);   //null = fullscreen

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
}