package odotatesting.base;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

import odotatesting.factory.PlaywrightFactory;
import odotatesting.pages.HomePage;

public abstract class BaseTest {

    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    String scenario = this.getClass().getSimpleName();

    PlaywrightFactory pf;
    Page page;
    protected Properties properties;
    protected HomePage homePage;

    @BeforeClass
    public void printClassNameOnStart() {
        logger.info("================================================================================");
        logger.info("*** TEST STARTED ***");
        logger.info("--------------------------------------------------------------------------------");
        logger.info("*** Starting Scenario {} ***", scenario);
    }

    @AfterClass
    public void printClassNameOnEnd() {
        logger.info("*** Scenario {} Execution Ended ***", scenario);
        logger.info("--------------------------------------------------------------------------------");
    }

    @BeforeTest
    public void setup() {
        pf = new PlaywrightFactory();
        try {
            properties = pf.initializeProperties();
            if (properties == null) {
                logger.error("Setup failed: Properties null.");
                throw new RuntimeException("Setup failed: Properties not loaded.");
            }

            page = pf.initializeBrowser(properties);
            if (page == null) {
                logger.error("Setup failed: Browser page null.");
                throw new RuntimeException("Setup failed: Browser not launched.");
            }

            homePage = new HomePage(page);

        } catch (PlaywrightException e) {
            logger.fatal("Playwright setup failed: {}. Scenario: {}.", e.getMessage(), scenario, e);
            throw new RuntimeException("Playwright setup failed.", e);
        } catch (Exception e) {
            logger.fatal("Unexpected setup error: {}. Scenario: {}.", e.getMessage(), scenario, e);
            throw new RuntimeException("Unexpected setup error.", e);
        }
    }

    @AfterTest
    public void tearDown() {
        if (page != null && page.context() != null && page.context().browser() != null) {
            try {
                page.context().browser().close();
                logger.info("Browser closed.");
                logger.info("================================================================================");
            } catch (PlaywrightException e) {
                logger.warn("Playwright teardown error: {}. Scenario: {}.", e.getMessage(), scenario, e);
            } catch (Exception e) {
                logger.warn("Unexpected teardown error: {}. Scenario: {}.", e.getMessage(), scenario, e);
            }
        } else {
            logger.warn("Browser or page null during teardown. Scenario: {}.", scenario);
        }
    }
}