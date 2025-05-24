package odotatesting.processors.heroes;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;

import odotatesting.utils.CallOpendotaAPI;

public class HeroesCount {

    private static final Logger logger = LogManager.getLogger(HeroesCount.class);
    private final Page page;

    public HeroesCount(Page page) {
        this.page = page;
    }

    public Integer getHeroesCountFromWeb() {
        final String ROWS_XPATH_LOCATOR = "xpath=/html//table[1]/tbody[1]/tr";
        Locator tableRows = this.page.locator(ROWS_XPATH_LOCATOR);

        try {
            // waits for the first row to be visible
            tableRows.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        } catch (Exception e) {
            logger.error("Timeout waiting for table rows to be visible: {}", e.getMessage());
            return 0;
        }
        logger.info("Number of heroes from web: {}", tableRows.count());
        return tableRows.count();
    }

    public Integer getHeroesCountFromAPI() {
        JSONArray jsonArray = CallOpendotaAPI.heroStats();
        logger.info("Number of heroes from API: {}", jsonArray.length());
        return jsonArray.length();
    }
}
