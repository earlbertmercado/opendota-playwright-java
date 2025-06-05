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
    private final Page page;

    private static final String RADIANT_TEAM = "Radiant";
    private static final String DIRE_TEAM = "Dire";

    private static final String RADIANT_TABLE_CLASS = "teamtable-radiant";
    private static final String DIRE_TABLE_CLASS = "teamtable-dire";
    private static final String TEAM_TABLE_SELECTOR_PREFIX = ".teamtable.";
    private static final String PLAYER_ROW_SELECTOR_SUFFIX = " table tbody tr:nth-child(";
    private static final String COLUMN_SELECTOR_SUFFIX = ") td:nth-child(";

    private static final int PLAYERS_PER_TEAM = 5;
    private static final int FIRST_STAT_COLUMN = 2;
    private static final int LAST_COLUMN = 15;

    // Use an unmodifiable set for constants to prevent accidental modification
    private static final Set<Integer> COLUMNS_TO_SKIP = Collections.unmodifiableSet(Set.of(7, 11));


    public OverviewBasicStatsProc(Page page) {
        this.page = page;
    }

    /**
     * Method for rounding API values to align with the web format
     * Return The formatted string (e.g., "1234" -> "1.2k", "1200" -> "1.2k", "1000" -> "1k").
     * Returns the original value as a string if <= 1000.
     * (not 100% working, need to know the exact and precise rounding formula)
     */
    private static String formatValue(int value) {
        if (value <= 1000) {
            return Integer.toString(value);
        }

        double kiloValue = value / 1000.0;
        String formattedValue = String.format(Locale.US, "%.1fk", kiloValue);
        return formattedValue.endsWith(".0k") ? formattedValue.replace(".0k", "k") : formattedValue;
    }

    // Returns the corresponding CSS class name for the specified team.
    private static String getTeamTableClassName(String team) {
        if (team == null) {
            logger.warn("Team name cannot be null.");
            return null;
        }
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
        List<Map<String, Object>> allPlayerStats = new ArrayList<>();
        String teamClassName = getTeamTableClassName(team);

        if (teamClassName == null) {
            logger.warn("Could not determine team table class for team: {}", team);
            return JsonOutput.prettyPrint(JsonOutput.toJson(allPlayerStats));
        }

        // Iterate through each player in the team
        for (int playerNumber = 1; playerNumber <= PLAYERS_PER_TEAM; playerNumber++) {
            Map<String, Object> playerStats = new LinkedHashMap<>();

            int playerIndex = team.equalsIgnoreCase(RADIANT_TEAM) ? playerNumber - 1 : playerNumber + 4;
            playerStats.put("player_index", playerIndex);

            int statKeyIndex = 0;

            // Iterate through each relevant statistic column
            for (int column = FIRST_STAT_COLUMN; column <= LAST_COLUMN; column++) {
                // Skip columns that are not stats
                if (!COLUMNS_TO_SKIP.contains(column)) {
                    String cssSelector = TEAM_TABLE_SELECTOR_PREFIX + teamClassName +
                            PLAYER_ROW_SELECTOR_SUFFIX + playerNumber +
                            COLUMN_SELECTOR_SUFFIX + column + ")";

                    Locator statElement = this.page.locator(cssSelector);
                    String statValue = statElement.textContent().trim();

                    String finalStatValue = "-".equals(statValue) ? "0" : statValue;

                    if (statKeyIndex < statKeys.size()) {
                        playerStats.put(statKeys.get(statKeyIndex), finalStatValue);
                    } else {
                        logger.warn("Stat key not found for column {}. Index: {}", column, statKeyIndex);
                    }
                    statKeyIndex++;
                }
            }
            allPlayerStats.add(playerStats);
        }
        String jsonOutput = JsonOutput.prettyPrint(JsonOutput.toJson(allPlayerStats));
        //  logger.info("Extracted Data From Web - {}", jsonOutput);
        return jsonOutput;
    }

    /**
     * Extracts match stats for the specified team (Radiant or Dire) from the /matches
     * API response. Return A JSON formatted string containing the match stats for the
     * specified team.
     */
    public static String extractMatchStatsFromAPI(JSONObject matchDetails, String team) {
        List<Map<String, Object>> extractedStatsFromAPI = new ArrayList<>();
        JSONArray playersArray = matchDetails.optJSONArray("players");

        if (playersArray == null) {
            logger.warn("API response does not contain 'players' array.");
            return JsonOutput.prettyPrint(JsonOutput.toJson(extractedStatsFromAPI));
        }

        int playerStartIndex = team.equalsIgnoreCase(RADIANT_TEAM) ? 0 : PLAYERS_PER_TEAM;
        int playerEndIndex = playerStartIndex + PLAYERS_PER_TEAM - 1;

        for (int i = playerStartIndex; i <= playerEndIndex; i++) {
            JSONObject playerStats = playersArray.optJSONObject(i);
            if (playerStats == null) {
                logger.warn("Player data not found at index: {}", i);
                continue;
            }

            Map<String, Object> playerStatsMap = new LinkedHashMap<>();

            playerStatsMap.put("player_index", i);
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
        String jsonOutput = JsonOutput.prettyPrint(JsonOutput.toJson(extractedStatsFromAPI));
        //  logger.info("Extracted Data From API - {}", jsonOutput);
        return jsonOutput;
    }
}