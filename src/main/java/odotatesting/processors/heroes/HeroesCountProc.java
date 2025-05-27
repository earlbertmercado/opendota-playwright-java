package odotatesting.processors.heroes;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;

import odotatesting.constants.HeroesPageLocators;
import odotatesting.utils.CallOpendotaAPI;

public class HeroesCountProc {

    private static final Logger logger = LogManager.getLogger(HeroesCountProc.class);
    private final Page page;

    private static final int WEB_ELEMENT_WAIT_TIMEOUT_MS = 10000;

    public HeroesCountProc(Page page) {
        this.page = page;
    }

    // Retrieves the count of heroes by counting the rows in the heroes table on the web page.
    public Integer getHeroesCountFromWeb() {
        Locator tableRows = this.page.locator(HeroesPageLocators.HERO_TABLE_ROWS_XPATH);
        int heroCount = 0; // Initialize count

        try {
            // Wait for at least the first row to be visible to ensure the table has loaded
            logger.debug("Waiting for web hero table rows to be visible...");
            tableRows.first().waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(WEB_ELEMENT_WAIT_TIMEOUT_MS));

            heroCount = tableRows.count();
            logger.info("Web count: {}", heroCount);

        } catch (Exception e) {
            logger.error("Failed to retrieve hero count from web (table not found/loaded): {}", e.getMessage());
            logger.error("Stack trace:", e);
        }

        return heroCount;
    }

    // Retrieves the total count of heroes by calling the OpenDota API.
    public Integer getHeroesCountFromAPI() {
        int apiHeroCount = 0;

        try {
            JSONArray jsonArray = CallOpendotaAPI.heroStats();
            if (jsonArray != null) {
                apiHeroCount = jsonArray.length();
            } else {
                logger.warn("API call returned null JSON array.");
            }
        } catch (Exception e) {
            logger.error("Failed to retrieve hero count from API: {}", e.getMessage());
            logger.error("Stack trace:", e);
        }

        logger.info("API count: {}", apiHeroCount);
        return apiHeroCount;
    }
}