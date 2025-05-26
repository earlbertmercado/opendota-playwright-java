package odotatesting.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;

import odotatesting.base.BasePage;
import odotatesting.constants.HeroesPageLocators;

import java.util.List;

public class HeroesPage extends BasePage {

    public HeroesPage(Page page) {
        super(page);
    }

    public HeroesPage clickProfessionalTab() {
        page.click(HeroesPageLocators.PROFESSIONAL_TAB);
        return this;
    }

    public HeroesPage clickPublicTab() {
        page.click(HeroesPageLocators.PUBLIC_TAB);
        return this;
    }

    public HeroesPage clickTurboTab() {
        page.click(HeroesPageLocators.TURBO_TAB);
        return this;
    }

    public List<Locator> getHeroImages() {
        return  page.locator(HeroesPageLocators.HERO_IMAGE).all();
    }
}
