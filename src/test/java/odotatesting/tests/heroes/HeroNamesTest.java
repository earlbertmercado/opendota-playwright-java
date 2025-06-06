package odotatesting.tests.heroes;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import odotatesting.base.BaseTest;
import odotatesting.pages.HeroesPage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the consistency of hero names displayed on the web interface
 * against hero names retrieved from the API.
 */
public class HeroNamesTest extends BaseTest {

    private HeroesPage heroesPage;

    @BeforeMethod
    public void goToHeroesPage() {
        heroesPage = navigateToHeroesPage().clickProfessionalTab();
    }

    @Test
    public void testHeroNames() {
        List<String> heroNamesFromWeb = heroesPage.getHeroNamesFromWeb();
        List<String> heroNamesFromAPI = heroesPage.getHeroNamesFromAPI();

        assertThat(heroNamesFromWeb)
                .as("List of hero names from the web")
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(heroNamesFromAPI);

        logger.info("{} validation finished.", this.getClass().getSimpleName());
    }
}
