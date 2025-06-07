package odotatesting.pages;

import com.microsoft.playwright.Page;
import org.json.JSONObject;

import odotatesting.base.BasePage;
import odotatesting.constants.MatchesPageLocators;
import odotatesting.processors.matches.MatchSummaryProc;
import odotatesting.processors.matches.OverviewBasicStatsProc;

import java.util.List;
import java.util.Map;

public class MatchesPage extends BasePage {

    private MatchSummaryProc matchSummaryProc;
    private OverviewBasicStatsProc overviewBasicStatsProc;

    public MatchesPage(Page page) {
        super(page);
        initializeProcessors(page);
    }

    public void initializeProcessors(Page page) {
        this.matchSummaryProc = new MatchSummaryProc(page);
        this.overviewBasicStatsProc = new OverviewBasicStatsProc(page);
    }

    public MatchesPage clickTopPublicTab() {
        page.click(MatchesPageLocators.TOP_PUBLIC_TAB);
        return this;
    }

    public MatchesPage clickFirstTopPubMatchOnList() {
        page.click(MatchesPageLocators.TOP_PUB_MATCH_ON_LIST);
        return this;
    }

    public MatchesPage clickMatchesOverview() {
        page.click(MatchesPageLocators.OVERVIEW_TAB);
        return this;
    }

    public String getMatchId() {
        return page.textContent(MatchesPageLocators.MATCH_ID);
    }

    public String getWinnerTeam() {
        return page.textContent(MatchesPageLocators.WINNER_TEAM).split(" ")[0];
    }

    public String getRadiantKills() {
        return page.textContent(MatchesPageLocators.RADIANT_KILLS);
    }

    public String getDireKills() {
        return page.textContent(MatchesPageLocators.DIRE_KILLS);
    }

    public String getMatchDuration() {
        return page.textContent(MatchesPageLocators.MATCH_DURATION);
    }

    public Map<String, Object> getMatchSummaryFromWeb() {
        return matchSummaryProc.getMatchSummaryFromWeb();
    }

    public Map<String, Object> getMatchSummaryFromAPI(JSONObject matchDetailsFromAPI) {
        return matchSummaryProc.getMatchSummaryFromAPI(matchDetailsFromAPI);
    }

    public String extractMatchStatsFromWeb(List<String> statKeys, String team) {
        return overviewBasicStatsProc.extractMatchStatsFromWeb(statKeys, team);
    }

    public String extractMatchStatsFromAPI(JSONObject matchDetails, String team) {
        return overviewBasicStatsProc.extractMatchStatsFromAPI(matchDetails, team);
    }

}