package odotatesting.base;

import java.util.Properties;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import odotatesting.factory.PlaywrightFactory;
import odotatesting.pages.HomePage;

public abstract class BaseTest {

    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    String scenario = this.getClass().getSimpleName();

    protected PlaywrightFactory pf;
    protected Page page;
    protected Properties properties;
    protected HomePage homePage;

    @BeforeClass
    public void setupBrowserAndPages() {
        logger.info("================================================================================");
        logger.info("*** TEST SUITE SETUP START ***");
        logger.info("--------------------------------------------------------------------------------");
        logger.info("*** Setting up for Scenario Class: {} ***", scenario);

        pf = new PlaywrightFactory();
        try {
            properties = pf.initializeProperties();
            if (properties == null) {
                logger.error("Setup failed for {}: Properties null.", scenario);
                throw new RuntimeException("Setup failed: Properties not loaded.");
            }

            page = pf.initializeBrowser(properties);
            if (page == null) {
                logger.error("Setup failed for {}: Browser page null.", scenario);
                throw new RuntimeException("Setup failed: Browser not launched.");
            }

            homePage = new HomePage(page);

        } catch (PlaywrightException e) {
            logger.fatal("Playwright setup failed for {}: {}. ", scenario, e.getMessage(), e);
            throw new RuntimeException("Playwright setup failed for " + scenario, e);
        } catch (Exception e) {
            logger.fatal("Unexpected setup error for {}: {}.", scenario, e.getMessage(), e);
            throw new RuntimeException("Unexpected setup error for " + scenario, e);
        }
    }

    @AfterClass
    public void tearDownBrowser() {
        if (page != null && page.context() != null && page.context().browser() != null) {
            try {
                page.context().browser().close();
                logger.info("Browser closed for scenario: {}.", scenario);
                logger.info("================================================================================");
            } catch (PlaywrightException e) {
                logger.warn("Playwright teardown error for {}: {}.", scenario, e.getMessage(), e);
            } catch (Exception e) {
                logger.warn("Unexpected teardown error for {}: {}.", scenario, e.getMessage(), e);
            }
        } else {
            logger.warn("Browser or page null during teardown for scenario: {}.", scenario);
        }
        logger.info("*** TEST SUITE SETUP END ***"); // More generic message
    }

    @BeforeClass
    public void printClassStartLog() {
        logger.info("================================================================================");
        logger.info("*** CLASS TEST STARTED: {} ***", scenario);
        logger.info("--------------------------------------------------------------------------------");
    }

    @AfterClass
    public void printClassEndLog() {
        logger.info("*** CLASS TEST ENDED: {} ***", scenario);
        logger.info("--------------------------------------------------------------------------------");
    }
}