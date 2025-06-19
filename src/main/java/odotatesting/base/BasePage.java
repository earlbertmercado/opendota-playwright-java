package odotatesting.base;

import com.microsoft.playwright.Page;

import com.microsoft.playwright.options.WaitForSelectorState;
import odotatesting.constants.BurgerMenuLocators;
import odotatesting.constants.NavigationBarLocators;
import odotatesting.pages.HeroesPage;
import odotatesting.pages.HomePage;
import odotatesting.pages.MatchesPage;
import odotatesting.pages.TeamsPage;

public class BasePage {

    protected Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    public Page getPage() {
        return page;
    }

    protected void initializeProcessors(Page page) {}

    public String getPageURL() {
        return page.url();
    }

    public String getPageTitle() {
        return page.title();
    }

    public boolean isBurgerMenuVisible() {
        final int BURGER_MENU_APPEAR_THRESHOLD = 1500;
        return (int)page.evaluate("() => window.innerWidth") < BURGER_MENU_APPEAR_THRESHOLD;
    }

    public void clickBurgerMenu() {
        page.click(NavigationBarLocators.BURGER_MENU);
    }

    public void waitForElementToBeVisible(String locator) {
        page.waitForSelector(locator,
                new Page.WaitForSelectorOptions().
                        setState(WaitForSelectorState.VISIBLE));
    }

    public HomePage navigateToHomePage() {
        //home button is always visible
        page.click(NavigationBarLocators.HOME_NAV_BAR);
        return new HomePage(page);
    }

    public MatchesPage navigateToMatchesPage() {
        if (isBurgerMenuVisible()) {
            clickBurgerMenu();
            page.click(BurgerMenuLocators.MATCHES);
        } else {
            page.click(NavigationBarLocators.MATCHES_NAV_BAR);
        }
        return new MatchesPage(page);
    }

    public HeroesPage navigateToHeroesPage() {
        if (isBurgerMenuVisible()) {
            clickBurgerMenu();
            page.click(BurgerMenuLocators.HEROES);
        } else {
            page.click(NavigationBarLocators.HEROES_NAV_BAR);
        }
        return new HeroesPage(page);
    }

    public TeamsPage navigateToTeamsPage() {
        if (isBurgerMenuVisible()) {
            clickBurgerMenu();
            page.click(BurgerMenuLocators.TEAMS);
        } else {
            page.click(NavigationBarLocators.TEAMS_NAV_BAR);
        }
        return new TeamsPage(page);
    }
}
