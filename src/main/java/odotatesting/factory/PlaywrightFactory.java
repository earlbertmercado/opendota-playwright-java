package odotatesting.factory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import odotatesting.constants.AppConstants;

public class PlaywrightFactory {

    Playwright playwright;
    Browser browser;
    BrowserContext browserContext;
    Page page;

    Properties properties;

    public Page initializeBrowser(Properties properties) {
        String browserName = properties.getProperty("browser").trim();
        boolean headlessFlag = Boolean.parseBoolean(properties.getProperty("headless"));

        playwright = Playwright.create();

        switch (browserName.toLowerCase()) {
            case "chromium":
                browser = playwright.chromium()
                        .launch(new BrowserType.LaunchOptions().setHeadless(headlessFlag).setArgs(List.of("--start-maximized")));
                break;
            case "firefox":
                browser = playwright.firefox().launch(new BrowserType.LaunchOptions());
                break;
            case "safari":
                browser = playwright.webkit().launch(new BrowserType.LaunchOptions());
                break;
            case "chrome":
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("Chrome").setHeadless(headlessFlag));
                break;
            default:
                System.out.println("Pass a valid browser name.");
                break;
        }

        browserContext = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
        page = browserContext.newPage();
        page.navigate(AppConstants.BASE_URL_WEB);
        return page;
    }

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