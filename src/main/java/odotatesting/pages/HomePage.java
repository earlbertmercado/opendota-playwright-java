package odotatesting.pages;

import com.microsoft.playwright.Page;

import odotatesting.base.BasePage;
import odotatesting.constants.NavigationBarLocators;

public class HomePage extends BasePage {

    private final String SEARCH_FIELD = "#searchField";
    private final String MATCHES_LINK = "//a[normalize-space()='Matches']";

    public HomePage(Page page) {
        super(page);
    }

    public String getHomePageTitle() {
        String homePageTitle = page.title();
        System.out.println("Page title: " + homePageTitle);
        return homePageTitle;
    }

    public String getHomePageURL() {
        String homePageURL = page.url();
        System.out.println("Page URL: " + homePageURL);
        return homePageURL;
    }

    public MatchesPage navigateToMatchesPage() {
        page.click(NavigationBarLocators.MATCHES_NAV_BAR);
        return new MatchesPage(page);
    }

    public HeroesPage navigateToHeroesPage() {
        page.click(NavigationBarLocators.HEROES_NAV_BAR);
        return new HeroesPage(page);
    }

    public TeamsPage navigateToTeamsPage() {
        page.click(NavigationBarLocators.TEAMS_NAV_BAR);
        return new TeamsPage(page);
    }
}
