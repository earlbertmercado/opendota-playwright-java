package odotatesting.processors.heroes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import odotatesting.constants.HeroesPageLocators;

public class HeroesProTableDataProc {

    private static final Logger logger = LogManager.getLogger(HeroesProTableDataProc.class);
    private final Page page;

    private static final int WEB_ELEMENT_WAIT_TIMEOUT_MS = 10000;

    public HeroesProTableDataProc(Page page) {
        this.page = page;
    }

    /**
     * Fetches hero data from the table as a sorted JSON string.
     * (For logging and debugging purposes.)
     */
    public String getHeroStatsAsJson() {
        waitForHeroImagesToLoad();

        Map<String, List<String>> rawHeroData = collectRawHeroData();
        List<String> heroNames = rawHeroData.get("heroNames");
        Map<String, JSONObject> sortedHeroesMap = new TreeMap<>();

        if (hasMismatchedListSizes(rawHeroData)) {
            logger.error("Data size mismatch. Cannot create JSON.");
            logListSizes(rawHeroData);
            return new JSONObject().toString();
        }

        logger.info("Processing {} heroes.", heroNames.size());

        for (int i = 0; i < heroNames.size(); i++) {
            String heroName = heroNames.get(i).trim();
            if (heroName.isEmpty()) {
                logger.warn("Empty hero name at index {}, skipping.", i);
                continue;
            }

            JSONObject stats = new JSONObject();
            putValidatedStat(stats, "proPickPlusBanPercentage", rawHeroData.get("proPickPlusBanPercentage").get(i));
            putValidatedStat(stats, "proPickPlusBanCount", rawHeroData.get("proPickPlusBanCount").get(i));
            putValidatedStat(stats, "proPickPercentage", rawHeroData.get("proPickPercentage").get(i));
            putValidatedStat(stats, "proPickCount", rawHeroData.get("proPickCount").get(i));
            putValidatedStat(stats, "proBanPercentage", rawHeroData.get("proBanPercentage").get(i));
            putValidatedStat(stats, "proBanCount", rawHeroData.get("proBanCount").get(i));
            putValidatedStat(stats, "proWinPercentage", rawHeroData.get("proWinPercentage").get(i));
            putValidatedStat(stats, "proWinCount", rawHeroData.get("proWinCount").get(i));

            sortedHeroesMap.put(heroName, stats);
        }

        JSONObject heroesJson = new JSONObject(sortedHeroesMap);
        logger.info("Created JSON for {} heroes.", heroesJson.length());
        return heroesJson.toString(4);
    }

    /**
     * Checks if hero table data is valid (consistent sizes, no empty strings).
     * This is the main method that will be passed to the test class and will
     * validate the data in heroes table.
     */
    public boolean doesTableContainValidData() {
        waitForHeroImagesToLoad();

        Map<String, List<String>> rawHeroData = collectRawHeroData();

        if (hasMismatchedListSizes(rawHeroData)) {
            logger.error("Validation failed: Data size mismatch.");
            logListSizes(rawHeroData);
            return false;
        }

        List<String> heroNames = rawHeroData.get("heroNames");
        if (heroNames.isEmpty()) {
            logger.warn("Validation: No heroes found.");
            return false;
        }

        boolean allDataValid = true;
        for (int i = 0; i < heroNames.size(); i++) {
            String heroName = heroNames.get(i).trim();
            if (heroName.isEmpty()) {
                logger.error("Validation failed: Empty hero name at index {}.", i);
                allDataValid = false;
            }

            for (Map.Entry<String, List<String>> entry : rawHeroData.entrySet()) {
                if (entry.getKey().equals("heroNames")) {
                    continue;
                }

                String statValue = entry.getValue().get(i).trim();
                if (statValue.isEmpty()) {
                    logger.error("Validation failed: Empty stat '{}' at index {} (hero: {}).",
                            entry.getKey(), i, heroName.isEmpty() ? "Unknown" : heroName);
                    allDataValid = false;
                }
            }
        }

        if (allDataValid) {
            logger.info("Validation passed: All data is valid.");
        } else {
            logger.error("Validation failed: Found empty/invalid data.");
        }

        return allDataValid;
    }

    /**
     * Collects all raw hero data lists from page locators.
     * It returns Map of list names to their extracted text contents.
     */
    private Map<String, List<String>> collectRawHeroData() {
        Map<String, List<String>> data = new LinkedHashMap<>();
        data.put("heroNames", page.locator(HeroesPageLocators.HERO_NAME_LOCATOR_RELATIVE).allTextContents());
        data.put("proPickPlusBanPercentage", page.locator(HeroesPageLocators.PRO_PICK_PLUS_BAN_PERCENTAGE).allTextContents());
        data.put("proPickPlusBanCount", page.locator(HeroesPageLocators.PRO_PICK_PLUS_BAN_COUNT).allTextContents());
        data.put("proPickPercentage", page.locator(HeroesPageLocators.PRO_PICK_PERCENTAGE).allTextContents());
        data.put("proPickCount", page.locator(HeroesPageLocators.PRO_PICK_COUNT).allTextContents());
        data.put("proBanPercentage", page.locator(HeroesPageLocators.PRO_BAN_PERCENTAGE).allTextContents());
        data.put("proBanCount", page.locator(HeroesPageLocators.PRO_BAN_COUNT).allTextContents());
        data.put("proWinPercentage", page.locator(HeroesPageLocators.PRO_WIN_PERCENTAGE).allTextContents());
        data.put("proWinCount", page.locator(HeroesPageLocators.PRO_WIN_COUNT).allTextContents());
        return data;
    }


    // Checks if all lists in the provided map have the same size.
    private boolean hasMismatchedListSizes(Map<String, List<String>> data) {
        if (data.isEmpty()) {
            return true;
        }
        int expectedSize = data.values().iterator().next().size();
        return !data.values().stream()
                .allMatch(list -> list.size() == expectedSize);
    }

    // Logs the sizes of all lists.
    private void logListSizes(Map<String, List<String>> data) {
        StringBuilder logMessage = new StringBuilder("List sizes: ");
        data.forEach((name, list) -> logMessage.append(name).append("=").append(list.size()).append(", "));
        if (logMessage.length() > "List sizes: ".length()) {
            logMessage.setLength(logMessage.length() - 2);
        }
        logger.error(logMessage.toString());
    }

    //Trims string, uses JSONObject.NULL if empty, then puts into JSONObject.
    private void putValidatedStat(JSONObject jsonObject, String key, String value) {
        String trimmedValue = value.trim();
        jsonObject.put(key, trimmedValue.isEmpty() ? JSONObject.NULL : trimmedValue);
    }

    private void waitForHeroImagesToLoad() {
        try {
            page.waitForSelector(HeroesPageLocators.PRO_PICK_PLUS_BAN_PERCENTAGE, new Page.WaitForSelectorOptions()
                    .setState(WaitForSelectorState.VISIBLE).setTimeout(WEB_ELEMENT_WAIT_TIMEOUT_MS));
            logger.debug("Hero stats table loaded.");
        } catch (Exception e) {
            logger.error("Wait for hero stats table failed: {}", e.getMessage(), e);
        }
    }
}