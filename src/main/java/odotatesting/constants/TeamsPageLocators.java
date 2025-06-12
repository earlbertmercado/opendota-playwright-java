package odotatesting.constants;

public class TeamsPageLocators {

    // /teams
    public static final String TEAM_NAME_COLUMN = "//table/tbody/tr/td[2]//a";
    public static final String RATING_COLUMN ="//table/tbody/tr/td[3]/div";
    public static final String WINS_COLUMN ="//table/tbody/tr/td[4]/div";
    public static final String LOSSES_COLUMN ="//table/tbody/tr/td[5]/div";

    // /teams/<team_id>/
    public static final String OVERVIEW_TAB = "//span[normalize-space()='Overview']";
    public static final String MATCHES_TAB = "//span[normalize-space()='Matches']";
    public static final String HEROES_TAB = "//span[normalize-space()='Heroes']";
    public static final String PLAYERS_TAB = "//span[normalize-space()='Players']";
}
