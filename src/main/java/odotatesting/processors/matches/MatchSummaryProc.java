package odotatesting.processors.matches;

import java.util.HashMap;
import java.util.Map;

import com.microsoft.playwright.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import odotatesting.pages.MatchesPage;

public class MatchSummaryProc {

    private static final Logger logger = LogManager.getLogger(MatchSummaryProc.class);
     private final Page page;

    public MatchSummaryProc(Page page) {
        this.page = page;
    }

    // Converts a duration string in HH:MM:SS or MM:SS format to seconds.
    private int convertToSeconds(String durationString) {
        if (durationString == null || durationString.trim().isEmpty()) {
            throw new IllegalArgumentException("Duration string empty.");
        }

        String[] timeComponents = durationString.split(":");

        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        if (timeComponents.length == 3) {
            // Format HH:MM:SS (game lasts more than 1 hour)
            try {
                hours = Integer.parseInt(timeComponents[0]);
                minutes = Integer.parseInt(timeComponents[1]);
                seconds = Integer.parseInt(timeComponents[2]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid duration numbers (HH:MM:SS): " + durationString, e);
            }
        } else if (timeComponents.length == 2) {
            // Format MM:SS (game lasts less than 1 hour)
            try {
                minutes = Integer.parseInt(timeComponents[0]);
                seconds = Integer.parseInt(timeComponents[1]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid duration numbers (MM:SS): " + durationString, e);
            }
        } else {
            throw new IllegalArgumentException("Invalid duration format: " + durationString);
        }

        if (minutes < 0 || minutes >= 60 || seconds < 0 || seconds >= 60 || hours < 0) {
            throw new IllegalArgumentException("Invalid time values: " + durationString);
        }

        return (hours * 3600) + (minutes * 60) + seconds;
    }

    /**
     * Retrieves match summary from the web page and returns them in a Map.
     * Keys: "winner", "radiantKills", "direKills", "matchDurationInSec".
     * Sample map returned: {"winner":"Radiant", "radiantKills":"36", "direKills":"47", "matchDurationInSec":2712}.
     */
    public Map<String, Object> getMatchSummaryFromWeb(MatchesPage matchesPage) {

        Map<String, Object> matchSummary = new HashMap<>();

        matchSummary.put("winner", matchesPage.getWinnerTeam());
        matchSummary.put("radiantKills", matchesPage.getRadiantKills());
        matchSummary.put("direKills", matchesPage.getDireKills());
        matchSummary.put("matchDurationInSec", convertToSeconds(matchesPage.getMatchDuration()));

        logger.info("Extracted Data From Web - {}", matchSummary);
        return matchSummary;
    }

    /**
     * Retrieves match summary from the /matches API and returns them in a Map.
     * Keys: "winner", "radiantKills", "direKills", "matchDurationInSec".
     * Sample map returned: {"winner":"Radiant", "radiantKills":"36", "direKills":"47", "matchDurationInSec":2712}.
     */
    public Map<String, Object> getMatchSummaryFromAPI(JSONObject matchDetailsFromAPI) {
        Map<String, Object> matchSummary = new HashMap<>();

        matchSummary.put("winner", matchDetailsFromAPI.getBoolean("radiant_win") ? "Radiant" : "Dire");
        matchSummary.put("radiantKills", String.valueOf(matchDetailsFromAPI.getInt("radiant_score")));
        matchSummary.put("direKills", String.valueOf(matchDetailsFromAPI.getInt("dire_score")));
        matchSummary.put("matchDurationInSec", matchDetailsFromAPI.getInt("duration"));

        logger.info("Extracted Data From API - {}", matchSummary);
        return matchSummary;
    }
}