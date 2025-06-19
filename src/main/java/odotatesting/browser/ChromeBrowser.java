package odotatesting.browser;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

public class ChromeBrowser implements IBrowserCreator {

    @Override
    public BrowserContext createContext(Playwright playwright,
                                        boolean isHeadless,
                                        Integer width,
                                        Integer height) {

        Browser browser = playwright.chromium().
                launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(isHeadless));

        return browser.newContext(setBrowserSize(width, height));
    }
}
