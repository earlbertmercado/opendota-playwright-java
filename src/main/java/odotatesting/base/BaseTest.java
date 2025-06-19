package odotatesting.base;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import odotatesting.browser.BrowserManager;
import odotatesting.pages.HeroesPage;
import odotatesting.pages.MatchesPage;
import odotatesting.pages.TeamsPage;
import odotatesting.utils.InitializeProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.util.Properties;

public abstract class BaseTest {

    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    protected BrowserManager browserManager;
    protected Page page;
    protected Properties properties;
    protected BasePage basePageInstance;

    @BeforeClass
    public void setupBrowserAndPages() {
        browserManager = new BrowserManager();
        try {
            properties = InitializeProperties.loadProperties();
            if (properties == null) {
                throw new RuntimeException("Setup failed: Properties not loaded.");
            }

            page = browserManager.initializeBrowser(properties);
            if (page == null) {
                throw new RuntimeException("Setup failed: Browser not launched.");
            }

            basePageInstance = new BasePage(page);
        } catch (PlaywrightException e) {
            throw new RuntimeException("Playwright setup failed: " + e.getMessage(), e);
        }
    }

    @AfterMethod
    public void printUrl() {
        if (page != null) {
            logger.info("Current URL: {}", page.url());
        } else {
            logger.warn("Page is not initialized, cannot display URL.");
        }
    }

    @AfterClass
    public void tearDownBrowser() {
        if (browserManager != null) {
            browserManager.getFactoryInstance().closeBrowser();
        }
    }

    // Helper methods for navigating via BasePage
    protected MatchesPage navigateToMatchesPage() {
        return basePageInstance.navigateToMatchesPage();
    }

    protected HeroesPage navigateToHeroesPage() {
        return basePageInstance.navigateToHeroesPage();
    }

    protected TeamsPage navigateToTeamsPage() {
        return basePageInstance.navigateToTeamsPage();
    }
}
