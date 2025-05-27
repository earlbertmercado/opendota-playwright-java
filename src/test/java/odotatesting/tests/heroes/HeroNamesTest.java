package odotatesting.tests.heroes;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import odotatesting.base.BaseTest;
import odotatesting.pages.HeroesPage;
import odotatesting.processors.heroes.HeroNamesProc;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the consistency of hero names displayed on the web interface
 * against hero names retrieved from the API.
 */
public class HeroNamesTest extends BaseTest {

    private HeroesPage heroesPage;
    HeroNamesProc heroNames;

    @BeforeMethod
    public void goToHeroesPage() {
        heroesPage = homePage.navigateToHeroesPage().clickProfessionalTab();

        heroNames = new HeroNamesProc(heroesPage.getPage());
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

        logger.info("{} completed successfully.", this.getClass().getSimpleName());
    }
}
