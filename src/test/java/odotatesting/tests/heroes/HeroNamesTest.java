package odotatesting.tests.heroes;

import odotatesting.base.BaseTest;
import odotatesting.pages.HeroesPage;
import odotatesting.processors.heroes.HeroNames;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class HeroNamesTest extends BaseTest {

    private HeroesPage heroesPage;
    HeroNames heroNames;

    @BeforeMethod
    public void goToHeroesPage() {
        heroesPage = homePage.navigateToHeroesPage().clickProfessionalTab();

        heroNames = new HeroNames(heroesPage.getPage());
        logger.info("URL: {}", heroesPage.getPageURL());
    }

    @Test
    public void testHeroNames() {
        List<String> heroNamesFromWeb = heroNames.getHeroListFromWeb();
        List<String> heroNamesFromAPI = heroNames.getHeroListFromAPI();

        assertThat(heroNamesFromWeb)
                .as("List of hero names from the web")
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(heroNamesFromAPI);
    }
}
