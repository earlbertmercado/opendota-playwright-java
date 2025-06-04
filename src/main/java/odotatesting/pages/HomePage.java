package odotatesting.pages;

import com.microsoft.playwright.Page;

import odotatesting.base.BasePage;
import odotatesting.constants.BurgerMenuLocators;
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
        //implement this in other navigation methods as well
        final int BURGER_MENU_APPEAR_THRESHOLD = 1500;

        boolean isBurgerMenuVisible = (int)page.evaluate("() => window.innerWidth") < BURGER_MENU_APPEAR_THRESHOLD;

        if (isBurgerMenuVisible) {
            clickBurgerMenu();
            page.click(BurgerMenuLocators.TEAMS);
        }else{
            page.click(NavigationBarLocators.TEAMS_NAV_BAR);
        }
        return new TeamsPage(page);
    }

    public void clickBurgerMenu() {
        page.click(NavigationBarLocators.BURGER_MENU);
    }
}
