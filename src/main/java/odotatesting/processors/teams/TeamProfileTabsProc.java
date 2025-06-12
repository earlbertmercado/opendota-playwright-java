package odotatesting.processors.teams;

import com.microsoft.playwright.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import odotatesting.pages.TeamsPage;

public class TeamProfileTabsProc {

    private static final Logger logger = LogManager.getLogger(TeamsTableDataProc.class);
    private final Page page;

    public TeamProfileTabsProc(Page page) {
        this.page = page;
    }

    public boolean areAllTabsClickable() {
        try {
            TeamsPage teamsPage = new TeamsPage(page);

            teamsPage.clickOverviewTab();
            logger.info("Successfully clicked the Overview tab.");

            teamsPage.clickMatchesTab();
            logger.info("Successfully clicked the Matches tab.");

            teamsPage.clickHeroesTab();
            logger.info("Successfully clicked the Heroes tab.");

            teamsPage.clickPlayersTab();
            logger.info("Successfully clicked the Players tab.");
            return true;
        }
        catch (Exception e) {
            logger.error("Failed to click one or more tabs: {}", e.getMessage(), e);
            return false;
        }
    }

}
