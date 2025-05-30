package odotatesting.processors.heroes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import odotatesting.constants.HeroesPageLocators;
import odotatesting.utils.CallOpendotaAPI;

public class HeroNamesProc {

    private static final Logger logger = LogManager.getLogger(HeroNamesProc.class);
    private final Page page;

    public HeroNamesProc(Page page) {
        this.page = page;
    }

    public List<String> getHeroListFromWeb() {
        try {
            // Wait for at least one row to be visible, ensuring the table has loaded
            this.page.locator(HeroesPageLocators.ROWS_LOCATOR).
                    first().waitFor(new Locator.
                            WaitForOptions().
                            setState(WaitForSelectorState.VISIBLE));

            // Get all row locators
            List<Locator> heroRows = this.page.locator(HeroesPageLocators.ROWS_LOCATOR).all();
            List<String> heroNameList = new ArrayList<>();

            for (Locator row : heroRows) {
                String heroName = row
                        .locator(HeroesPageLocators.HERO_NAME_COLUMN_LOCATOR)
                        .textContent()
                        .toLowerCase();

                heroNameList.add(heroName);
            }

            Collections.sort(heroNameList);
            logger.info("Number of heroes from web: {}", heroNameList.size());
            return heroNameList;

        } catch (Exception e) {
            logger.error("Failed to retrieve hero names from web due to: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<String> getHeroListFromAPI() {
        JSONArray heroStats = CallOpendotaAPI.heroStats();
        List<String> heroList = new ArrayList<>();

        for (int i = 0; i < heroStats.length(); i++) {
            JSONObject hero = heroStats.getJSONObject(i);
            heroList.add(hero.getString("localized_name").toLowerCase());
        }

        Collections.sort(heroList);
        logger.info("Number of heroes from API: {}", heroList.size());
        return heroList;
    }
}
