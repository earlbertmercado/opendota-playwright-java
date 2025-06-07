package odotatesting.pages;

import com.microsoft.playwright.Page;

import odotatesting.base.BasePage;
import odotatesting.processors.teams.TeamsTableDataProc;

public class TeamsPage extends BasePage {

    private TeamsTableDataProc teamsTableDataProc;

    public TeamsPage(Page page) {
        super(page);
        initializeProcessors(page);
    }

    public void initializeProcessors(Page page) {
        this.teamsTableDataProc = new TeamsTableDataProc(page);
    }

    public boolean validateTeamsTableData() {
        return teamsTableDataProc.hasNoInvalidData();
    }
}
