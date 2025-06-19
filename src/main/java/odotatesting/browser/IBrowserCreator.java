package odotatesting.browser;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Playwright;

import java.awt.Dimension;
import java.awt.Toolkit;

public interface IBrowserCreator {
    BrowserContext createContext(Playwright playwright,
                                 boolean isHeadless,
                                 Integer width,
                                 Integer height);

    default Browser.NewContextOptions setBrowserSize(Integer width, Integer height) {
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions();

        if (width != null && height != null) {
            contextOptions.setViewportSize(width, height);
        } else {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int screenWidth = (int) screenSize.getWidth();
            int screenHeight = (int) screenSize.getHeight();
            contextOptions.setViewportSize(screenWidth, screenHeight);
        }

        return contextOptions;
    }
}
