package odotatesting.tests.teams;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import odotatesting.base.BaseTest;
import odotatesting.pages.TeamsPage;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamProfileTabsTest extends BaseTest {

    private TeamsPage teamsPage;

    @BeforeMethod
    public void goToTeamsPage() {
        teamsPage = navigateToTeamsPage().clickRandomTeam();
    }

    @Test
    public void testTeamProfileTabs() {
        boolean allTabsClickable = teamsPage.areAllTabsClickable();

        assertThat(allTabsClickable)
                .as("Check if all team profile tabs are clickable")
                .isTrue();
        logger.info("{} validation finished.", this.getClass().getSimpleName());
    }
}
