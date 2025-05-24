package odotatesting.processors.matches;

import java.util.*;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import groovy.json.JsonOutput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class OverviewBasicStatsProc {

    private static final Logger logger = LogManager.getLogger(OverviewBasicStatsProc.class);
    private static final String RADIANT_TEAM = "Radiant";
    private static final String DIRE_TEAM = "Dire";

    private final Page page;

    public OverviewBasicStatsProc(Page page) {
        this.page = page;
    }

    // Method for rounding API values to align with the web format
    // (not 100% working, need to know the exact and precise rounding formula)
    private static String formatValue(int value) {
        if (value > 1000) {
            int lastTwoDigits = value % 100;
            double roundedValue;

            if (lastTwoDigits <= 50) {
                roundedValue = Math.floor(value / 1000.0 * 10) / 10;
            } else {
                roundedValue = Math.ceil(value / 1000.0 * 10) / 10;
            }

            String formattedValue = String.format("%.1fk", roundedValue);
            return formattedValue.endsWith(".0k") ? formattedValue.replace(".0k", "k") : formattedValue;
        } else {
            return Integer.toString(value);
        }
    }

    // Returns the corresponding CSS class name for the specified team.
    private static String getTeamTableClassName(String team) {
        final String RADIANT_TABLE_CLASS = "teamtable-radiant";
        final String DIRE_TABLE_CLASS = "teamtable-dire";

        if (team.equalsIgnoreCase(RADIANT_TEAM)) {
            return RADIANT_TABLE_CLASS;
        } else if (team.equalsIgnoreCase(DIRE_TEAM)) {
            return DIRE_TABLE_CLASS;
        } else {
            logger.warn("Invalid team input: {}", team);
            return null;
        }
    }

    /**
     * Extracts match stats for the specified team (Radiant or Dire) from the web page
     * and returns the data in a JSON formatted string. The stats are gathered by iterating
     * through the match overview table and collecting relevant stats for each player.
     * (Returns empty JSON if table not found.)
     */
    public String extractMatchStatsFromWeb(List<String> statKeys, String team) {
        final int PLAYERS_PER_TEAM = 5;
        final int FIRST_STAT_COLUMN = 2;
        final int LAST_COLUMN = 15;
        final Set<Integer> COLUMNS_TO_SKIP = Set.of(7, 11);

        List<Map<String, Object>> allPlayerStats = new ArrayList<>();

        String teamClassName = getTeamTableClassName(team);

        if (teamClassName == null) {
            return JsonOutput.prettyPrint(JsonOutput.toJson(allPlayerStats));
        }

        // Iterate through each player in the team
        for (int playerNumber = 1; playerNumber <= PLAYERS_PER_TEAM; playerNumber++) {
            Map<String, Object> playerStats = new LinkedHashMap<>();

            int playerIndex = team.equals(RADIANT_TEAM) ? playerNumber - 1 : playerNumber + 4;
            playerStats.put("player_index", playerIndex);

            int statKeyIndex = 0;

            // Iterate through each relevant statistic column
            for (int column = FIRST_STAT_COLUMN; column <= LAST_COLUMN; column++) {
                // Skip blank columns 7 and 11
                if (!COLUMNS_TO_SKIP.contains(column)) {

                    String cssSelector = ".teamtable." + teamClassName + " table tbody tr:nth-child(" + playerNumber + ") td:nth-child(" + column + ")";
                    Locator statElement = this.page.locator(cssSelector);

                    String statValue = "-".equals(statElement.textContent()) ? "0" : statElement.textContent();
                    playerStats.put(statKeys.get(statKeyIndex), statValue);
                    statKeyIndex++;
                }
            }
            allPlayerStats.add(playerStats);
        }
        logger.info("Extracted Data From Web - {}", JsonOutput.prettyPrint(JsonOutput.toJson(allPlayerStats)));
        return JsonOutput.prettyPrint(JsonOutput.toJson(allPlayerStats));
    }

    /**
     * Extracts match stats for the specified team (Radiant or Dire) from the /matches
     * API response. Return A JSON formatted string containing the match stats for the
     * specified team.
     */
    public static String extractMatchStatsFromAPI(JSONObject matchDetails, String team) {

        int playerStartIndex = team.equalsIgnoreCase("Radiant") ? 0 : 5;
        int playerEndIndex = playerStartIndex + 4;

        List<Map<String, Object>> extractedStatsFromAPI = new ArrayList<>();

        JSONArray playersArray = matchDetails.getJSONArray("players");

        for (int playerIndex = playerStartIndex; playerIndex <= playerEndIndex; playerIndex++) {
            JSONObject playerStats = playersArray.getJSONObject(playerIndex);
            Map<String, Object> playerStatsMap = new LinkedHashMap<>();

            // Ensure the order of keys matches the expected order
            playerStatsMap.put("player_index", playerIndex);
            playerStatsMap.put("level", String.valueOf(playerStats.optInt("level", 0)));
            playerStatsMap.put("kills", String.valueOf(playerStats.optInt("kills", 0)));
            playerStatsMap.put("deaths", String.valueOf(playerStats.optInt("deaths", 0)));
            playerStatsMap.put("assists", String.valueOf(playerStats.optInt("assists", 0)));
            playerStatsMap.put("last_hits", String.valueOf(playerStats.optInt("last_hits", 0)));
            playerStatsMap.put("denies", String.valueOf(playerStats.optInt("denies", 0)));
            playerStatsMap.put("net_worth", formatValue(playerStats.optInt("net_worth", 0)));
            playerStatsMap.put("gold_per_min", String.valueOf(playerStats.optInt("gold_per_min", 0)));
            playerStatsMap.put("xp_per_min", String.valueOf(playerStats.optInt("xp_per_min", 0)));
            playerStatsMap.put("hero_damage", formatValue(playerStats.optInt("hero_damage", 0)));
            playerStatsMap.put("tower_damage", formatValue(playerStats.optInt("tower_damage", 0)));
            playerStatsMap.put("hero_healing", formatValue(playerStats.optInt("hero_healing", 0)));

            extractedStatsFromAPI.add(playerStatsMap);
        }
        logger.info("Extracted Data From API - {}", JsonOutput.prettyPrint(JsonOutput.toJson(extractedStatsFromAPI)));
        return JsonOutput.prettyPrint(JsonOutput.toJson(extractedStatsFromAPI));
    }
}