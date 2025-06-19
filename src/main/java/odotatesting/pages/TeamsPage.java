package odotatesting.pages;

import com.microsoft.playwright.Page;
import odotatesting.base.BasePage;
import odotatesting.constants.TeamsPageLocators;
import odotatesting.processors.teams.TeamsTableDataProc;
import odotatesting.processors.teams.TeamProfileTabsProc;

import java.util.Random;

public class TeamsPage extends BasePage {

    private TeamsTableDataProc teamsTableDataProc;
    private TeamProfileTabsProc teamProfileTabsProc;


    public TeamsPage(Page page) {
        super(page);
        initializeProcessors(page);
    }

    @Override
    protected void initializeProcessors(Page page) {
        this.teamsTableDataProc = new TeamsTableDataProc(page);
        this.teamProfileTabsProc = new TeamProfileTabsProc(page);
    }

    public TeamsPage clickRandomTeam() {
        waitForElementToBeVisible(TeamsPageLocators.TEAM_NAME_COLUMN);
        int rowCount = page.locator(TeamsPageLocators.TEAM_NAME_COLUMN).count();

        if (rowCount == 0) {
            throw new RuntimeException("No teams found in the table.");
        }

        Random randomizer = new Random();
        int randomIndex = randomizer.nextInt(rowCount);
        page.locator(TeamsPageLocators.TEAM_NAME_COLUMN).nth(randomIndex).click();
        return this;
    }

    public TeamsPage clickOverviewTab() {
        page.click(TeamsPageLocators.OVERVIEW_TAB);
        return this;
    }

    public TeamsPage clickMatchesTab() {
        page.click(TeamsPageLocators.MATCHES_TAB);
        return this;
    }

    public TeamsPage clickHeroesTab() {
        page.click(TeamsPageLocators.HEROES_TAB);
        return this;
    }

    public TeamsPage clickPlayersTab() {
        page.click(TeamsPageLocators.PLAYERS_TAB);
        return this;
    }

    public boolean validateTeamsTableData() {
        return teamsTableDataProc.hasNoInvalidData();
    }

    public boolean areAllTabsClickable() {
        return teamProfileTabsProc.areAllTabsClickable();
    }
}
