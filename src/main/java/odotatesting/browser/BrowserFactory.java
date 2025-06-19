package odotatesting.browser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BrowserFactory {

    private static final Logger logger = LogManager.getLogger(BrowserFactory.class);

    public static IBrowserCreator createContext(BrowserTypes browserType) {
        switch (browserType) {
            case CHROMIUM:
                return new ChromiumBrowser();
            case FIREFOX:
                return new FirefoxBrowser();
            case WEBKIT:
                return new WebkitBrowser();
            case CHROME:
                return new ChromeBrowser();
            case EDGE:
                return new EdgeBrowser();
            default:
                logger.error("Unsupported browser type: {}", browserType);
                throw new IllegalArgumentException("Unsupported browser: " + browserType);
        }
    }
}
