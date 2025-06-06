package odotatesting.pages;

import com.microsoft.playwright.Page;

import odotatesting.base.BasePage;
import odotatesting.processors.teams.TeamsTableDataProc;

public class TeamsPage extends BasePage {

    private final TeamsTableDataProc teamsTableDataProc;

    public TeamsPage(Page page) {
        super(page);

        this.teamsTableDataProc = new TeamsTableDataProc(page);
    }

    // The following methods are from processor classes.

    public boolean validateTeamsTableData() {
        return teamsTableDataProc.hasNoInvalidData();
    }
}
