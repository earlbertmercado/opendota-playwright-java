package odotatesting.browser;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import odotatesting.constants.AppConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class BrowserManager {

    private static final Logger logger = LogManager.getLogger(BrowserManager.class);

    private static final ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> tlBrowserContext = new ThreadLocal<>();
    private static final ThreadLocal<Page> tlPage = new ThreadLocal<>();
    private static final ThreadLocal<BrowserManager> tlFactory = new ThreadLocal<>();

    public static BrowserManager getFactoryInstance() {
        return tlFactory.get();
    }

    public Page getPage() {
        return tlPage.get();
    }

    public Page initializeBrowser(Properties props) {
        String browserNameRaw = props.getProperty("browser", "CHROMIUM");
        boolean headless = Boolean.parseBoolean(props.getProperty("isHeadless", "false"));

        Integer width = parseDimension(props.getProperty("browserWidth"), "width");
        Integer height = parseDimension(props.getProperty("browserHeight"), "height");

        BrowserTypes browserType = BrowserTypes.fromString(browserNameRaw);
        logger.info("Starting [{}] - {}", browserType, headless ? "headless" : "headed");

        tlPlaywright.set(Playwright.create());
        IBrowserCreator creator = BrowserFactory.createContext(browserType);
        tlBrowserContext.set(creator.createContext(tlPlaywright.get(), headless, width, height));

        Page page = tlBrowserContext.get().newPage();
        page.navigate(AppConstants.BASE_URL_WEB);

        tlPage.set(page);
        tlFactory.set(this);

        logger.info("Started [{}]", browserType);
        return page;
    }

    public void closeBrowser() {
        try {
            logger.info("Closing browser...");
            if (tlBrowserContext.get() != null) {
                Browser browser = tlBrowserContext.get().browser();
                tlBrowserContext.get().close();
                if (browser != null) browser.close();
            }
            if (tlPlaywright.get() != null) tlPlaywright.get().close();
        } catch (Exception e) {
            logger.error("Close failed: {}", e.getMessage(), e);
        } finally {
            tlPage.remove();
            tlBrowserContext.remove();
            tlPlaywright.remove();
            tlFactory.remove();
            logger.info("Cleanup done.");
        }
    }

    private Integer parseDimension(String value, String type) {
        try {
            if (value != null && !value.trim().isEmpty()) {
                int dim = Integer.parseInt(value.trim());
                logger.debug("{}: {}", type, dim);
                return dim;
            }
        } catch (NumberFormatException e) {
            logger.warn("Invalid {} '{}'", type, value);
        }
        return null;
    }
}
