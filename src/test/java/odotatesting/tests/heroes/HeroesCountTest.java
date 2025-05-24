package odotatesting.tests.heroes;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import odotatesting.base.BaseTest;
import odotatesting.pages.HeroesPage;
import odotatesting.processors.heroes.HeroesCount;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies that the count of heroes displayed on the OpenDota web's Heroes page
 * matches the number of heroes retrieved from the OpenDota /heroStats API.
 */
public class HeroesCountTest extends BaseTest {

    private HeroesPage heroesPage;
    HeroesCount heroesCount;

    @BeforeMethod
    public void goToHeroesPage() {
        heroesPage = homePage.navigateToHeroesPage().clickProfessionalTab();

        heroesCount = new HeroesCount(heroesPage.getPage());
        logger.info("URL: {}", heroesPage.getPageURL());
    }

    @Test
    public void testHeroesCount() {

        int heroesCountFromWeb = heroesCount.getHeroesCountFromWeb();
        int heroesCountFromAPI = heroesCount.getHeroesCountFromAPI();

        assertThat(heroesCountFromWeb)
                .as("Number of heroes in the table")
                .isNotNull()
                .isEqualTo(heroesCountFromAPI);
    }
}
