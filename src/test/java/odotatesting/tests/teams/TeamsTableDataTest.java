package odotatesting.tests.teams;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import odotatesting.base.BaseTest;
import odotatesting.pages.TeamsPage;

import static org.assertj.core.api.Assertions.assertThat;

// Validates if there are null or empty data displayed in the teams table.
public class TeamsTableDataTest extends BaseTest {

    private TeamsPage teamsPage;

    @BeforeMethod
    public void goToTeamsPage() {
        teamsPage = navigateToTeamsPage();
    }

    @Test
    public void testTeamsTableData() {
        boolean teamTableHasNoInvalidData = teamsPage.validateTeamsTableData();
        assertThat(teamTableHasNoInvalidData)
                .as("Check if teams table data has invalid data")
                .isTrue();
        logger.info("{} validation finished.", this.getClass().getSimpleName());
    }
}
