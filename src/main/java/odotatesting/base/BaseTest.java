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
import odotatesting.utils.InitializeProperties;

public abstract class BaseTest {

    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    protected PlaywrightFactory pf;
    protected Page page;
    protected Properties properties;
    protected HomePage homePage;

    @BeforeClass
    public void setupBrowserAndPages() {
        pf = new PlaywrightFactory();
        try{
            properties = InitializeProperties.loadProperties();
            if (properties == null) {
                throw new RuntimeException("Setup failed: Properties not loaded.");
            }
            page = pf.initializeBrowser(properties);
            if (page == null) {
                throw new RuntimeException("Setup failed: Browser not launched.");
            }
            homePage = new HomePage(page);
        }catch (PlaywrightException e) {
            throw new RuntimeException("Playwright setup failed.");
        }
    }

    @AfterClass
    public void tearDownBrowser() {
        if (page != null && page.context() != null && page.context().browser() != null) {
            pf.closeBrowser();
        }
    }
}