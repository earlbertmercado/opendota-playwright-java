package odotatesting.browser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum BrowserTypes {
    CHROMIUM,
    FIREFOX,
    WEBKIT,
    CHROME,
    EDGE;

    private static final Logger logger = LogManager.getLogger(BrowserTypes.class);

    public static BrowserTypes fromString(String name) {
        try {
            return BrowserTypes.valueOf(name.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.error("Unsupported browser type: {}", name, e);
            throw new IllegalArgumentException("Unsupported browser: " + name);
        }
    }
}
