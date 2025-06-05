package odotatesting.processors.teams;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import odotatesting.constants.TeamsPageLocators;

public class TeamsTableDataProc {

    private static final Logger logger = LogManager.getLogger(TeamsTableDataProc.class);
    private final Page page;

    private static final int DEFAULT_TIMEOUT_MS = 30000;

    public TeamsTableDataProc(Page page) {
        this.page = page;
    }

    public boolean hasNoInvalidData() {
        List<String> teamNames = getColumnData(TeamsPageLocators.TEAM_NAME_COLUMN);
        List<String> ratings = getColumnData(TeamsPageLocators.RATING_COLUMN);
        List<String> wins = getColumnData(TeamsPageLocators.WINS_COLUMN);
        List<String> losses = getColumnData(TeamsPageLocators.LOSSES_COLUMN);

        if (containsNullOrEmptyValues(teamNames) ||
                containsNullOrEmptyValues(ratings) ||
                containsNullOrEmptyValues(wins) ||
                containsNullOrEmptyValues(losses)) {
            logger.error("One or more columns contain null or empty values.");
            return false;
        }

        if (teamNames.size() != ratings.size() ||
                teamNames.size() != wins.size() ||
                teamNames.size() != losses.size()) {
            logger.error("Column sizes do not match. Team Names: {}, Ratings: {}, Wins: {}, Losses: {}",
                    teamNames.size(), ratings.size(), wins.size(), losses.size());
            return false;
        }
        //  createJsonArrayFromColumnData();
        logger.info("All team table columns validated successfully.");
        return true;
    }

    private void waitForTeamNamesToLoad() {
        try {
            page.waitForSelector(TeamsPageLocators.TEAM_NAME_COLUMN, new Page.WaitForSelectorOptions()
                    .setState(WaitForSelectorState.VISIBLE).setTimeout(DEFAULT_TIMEOUT_MS));
        } catch (Exception e) {
            logger.error("Failed to load team names column: {}", e.getMessage(), e);
        }
    }

    private List<String> getColumnData(String columnLocator) {
        waitForTeamNamesToLoad();
        try {
            page.waitForSelector(columnLocator, new Page.WaitForSelectorOptions()
                    .setState(WaitForSelectorState.VISIBLE).setTimeout(DEFAULT_TIMEOUT_MS));
            return page.locator(columnLocator).allTextContents();
        } catch (Exception e) {
            logger.error("Failed to retrieve data from column '{}': {}", columnLocator, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    private boolean containsNullOrEmptyValues(List<String> columnData) {
        if (columnData.isEmpty()) {
            logger.warn("Column data is empty. No values found for validation.");
            return true;
        }

        for (String item : columnData) {
            if (item == null || item.trim().isEmpty()) {
                logger.warn("Null or empty value found in column data: '{}'",
                        Objects.requireNonNullElse(item, "null"));
                return true;
            }
        }

        return false;
    }

    private void createJsonArrayFromColumnData() {
        List<String> teamNames = getColumnData(TeamsPageLocators.TEAM_NAME_COLUMN);
        List<String> ratings = getColumnData(TeamsPageLocators.RATING_COLUMN);
        List<String> wins = getColumnData(TeamsPageLocators.WINS_COLUMN);
        List<String> losses = getColumnData(TeamsPageLocators.LOSSES_COLUMN);

        JSONArray jsonArray = new JSONArray();

        int dataSize = Math.min(teamNames.size(), Math.min(ratings.size(), Math.min(wins.size(), losses.size())));

        for (int i = 0; i < dataSize; i++) {
            JSONObject teamDetails = new JSONObject();
            try {
                teamDetails.put("ratings", Integer.parseInt(ratings.get(i).trim()));
                teamDetails.put("wins", Integer.parseInt(wins.get(i).trim()));
                teamDetails.put("losses", Integer.parseInt(losses.get(i).trim()));
            } catch (NumberFormatException e) {
                logger.error("Error parsing number for team {}", teamNames.get(i), e);
                continue;
            }
            JSONObject teamEntry = new JSONObject();
            teamEntry.put(teamNames.get(i), teamDetails);

            jsonArray.put(teamEntry);
        }
        //  logger.info(jsonArray.toString(2));
    }
}