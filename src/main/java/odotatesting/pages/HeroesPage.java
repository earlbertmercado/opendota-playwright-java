package odotatesting.pages;

import java.util.List;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;

import odotatesting.base.BasePage;
import odotatesting.constants.HeroesPageLocators;
import odotatesting.processors.heroes.HeroImagesProc;
import odotatesting.processors.heroes.HeroNamesProc;
import odotatesting.processors.heroes.HeroesCountProc;
import odotatesting.processors.heroes.HeroesProTableDataProc;

public class HeroesPage extends BasePage {

    private HeroesCountProc heroesCountProc;
    private HeroesProTableDataProc heroesProTableDataProc;
    private HeroImagesProc heroImagesProc;
    private HeroNamesProc heroNamesProc;

    public HeroesPage(Page page) {
        super(page);
        initializeProcessors(page);
    }

    private void initializeProcessors(Page page) {
        this.heroesCountProc = new HeroesCountProc(page);
        this.heroesProTableDataProc = new HeroesProTableDataProc(page);
        this.heroImagesProc = new HeroImagesProc(page);
        this.heroNamesProc = new HeroNamesProc(page);
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
        return  page.locator(HeroesPageLocators.ALL_HERO_IMAGES).all();
    }

    public Integer getHeroesCountFromWeb() {
        return heroesCountProc.getHeroesCountFromWeb();
    }

    public Integer getHeroesCountFromAPI() {
        return heroesCountProc.getHeroesCountFromAPI();
    }

    public boolean validateHeroesProTableData() {
        return heroesProTableDataProc.doesTableContainValidData();
    }

    public boolean areAllHeroImagesLoaded() {
        return heroImagesProc.areAllHeroImagesLoaded();
    }

    public List<String> getHeroNamesFromWeb() {
        return heroNamesProc.getHeroListFromWeb();
    }

    public List<String> getHeroNamesFromAPI() {
        return heroNamesProc.getHeroListFromAPI();
    }
}
