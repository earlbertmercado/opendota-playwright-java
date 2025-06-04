package odotatesting.tests.teams;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import odotatesting.base.BaseTest;
import odotatesting.pages.TeamsPage;
import odotatesting.processors.teams.TeamsTableDataProc;

import static org.assertj.core.api.Assertions.assertThat;

// Validates if there are null or empty data displayed in the teams table.
public class TeamsTableDataTest extends BaseTest {

    private TeamsPage teamsPage;
    TeamsTableDataProc teamsTableDataProc;

    @BeforeMethod
    public void goToTeamsPage() {
//        teamsPage = homePage.navigateToTeamsPage();
        teamsPage = navigateToTeamsPage();
        teamsTableDataProc = new TeamsTableDataProc(teamsPage.getPage());
    }

    @Test
    public void testTeamsTableData() {
        boolean teamTableHasNoInvalidData = teamsTableDataProc.hasNoInvalidData();
        assertThat(teamTableHasNoInvalidData)
                .as("Check if teams table data has invalid data")
                .isTrue();
        logger.info("{} validation finished.", this.getClass().getSimpleName());
    }
}
