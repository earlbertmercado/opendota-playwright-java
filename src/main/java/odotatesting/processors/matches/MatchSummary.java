package odotatesting.processors.matches;

import java.util.HashMap;
import java.util.Map;

import com.microsoft.playwright.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import odotatesting.pages.MatchesPage;

public class MatchSummary {

    private static final Logger logger = LogManager.getLogger(MatchSummary.class);
    private final Page page;

    public MatchSummary(Page page) {
        this.page = page;
    }

    // Converts a duration string in HH:MM:SS or MM:SS format to seconds.
    private int convertToSeconds(String durationFromAPI) {
        final int SECONDS_IN_HOUR = 3600;
        final int SECONDS_IN_MINUTE = 60;

        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        String[] timeComponents = durationFromAPI.split(":");

        if (timeComponents.length == 3) {
            // Format HH:MM:SS (game lasts more than 1 hour)
            hours = Integer.parseInt(timeComponents[0]);
            minutes = Integer.parseInt(timeComponents[1]);
            seconds = Integer.parseInt(timeComponents[2]);
        } else if (timeComponents.length == 2) {
            // Format MM:SS (game lasts less than 1 hour)
            minutes = Integer.parseInt(timeComponents[0]);
            seconds = Integer.parseInt(timeComponents[1]);
        } else {
            throw new IllegalArgumentException("Invalid time format: " + durationFromAPI);
        }
        return (hours * SECONDS_IN_HOUR) + (minutes * SECONDS_IN_MINUTE) + seconds;
    }

    /**
     * Retrieves match summary from the web page and returns them in a Map.
     * keys: "matchId", "winner", "radiantKills", "direKills", "matchDurationInSec".
     * Sample map returned: {"matchId":"7944730404", "winner":"Radiant",
     * "radiantKills":"36", "direKills":"47", "matchDurationInSec":2712}.
     */
    public Map<String, Object> getMatchSummaryFromWeb(MatchesPage matchesPage) {
        Map<String, Object> matchSummary = new HashMap<>();

        //String matchId = matchesPage.getMatchId();
        String winner = matchesPage.getWinnerTeam();
        String radiantKills = matchesPage.getRadiantKills();
        String direKills = matchesPage.getDireKills();
        String matchDuration = matchesPage.getMatchDuration();
        String matchDurationInSec = String.valueOf(convertToSeconds(matchDuration));

        matchSummary.put("winner", winner);
        matchSummary.put("radiantKills", radiantKills);
        matchSummary.put("direKills", direKills);
        matchSummary.put("matchDurationInSec", matchDurationInSec);

        logger.info("Extracted Data From Web - {}", matchSummary);
        return matchSummary;
    }

    /**
     * Retrieves match summary from the /matches API and returns them in a Map.
     * Keys: "matchId", "winner", "radiantKills", "direKills", "matchDurationInSec".
     * Sample map returned: {"matchId":"7944730404", "winner":"Radiant",
     * "radiantKills":"36", "direKills":"47", "matchDurationInSec":2712}.
     */
    public Map<String, Object> getMatchSummaryFromAPI(JSONObject matchDetailsFromAPI) {
        Map<String, Object> matchSummary = new HashMap<>();

        String winner = matchDetailsFromAPI.getBoolean("radiant_win") ? "Radiant" : "Dire";
        String radiantKills = String.valueOf(matchDetailsFromAPI.getInt("radiant_score"));
        String direKills = String.valueOf(matchDetailsFromAPI.getInt("dire_score"));
        String matchDurationInSec = String.valueOf(matchDetailsFromAPI.getInt("duration"));

        matchSummary.put("winner", winner);
        matchSummary.put("radiantKills", radiantKills);
        matchSummary.put("direKills", direKills);
        matchSummary.put("matchDurationInSec", matchDurationInSec);

        logger.info("Extracted Data From API - {}", matchSummary);
        return matchSummary;
    }
}