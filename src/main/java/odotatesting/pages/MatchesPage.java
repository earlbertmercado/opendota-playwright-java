package odotatesting.pages;

import com.microsoft.playwright.Page;

import odotatesting.base.BasePage;
import odotatesting.constants.MatchesPageLocators;

public class MatchesPage extends BasePage {

    public MatchesPage(Page page) {
        super(page);
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
}