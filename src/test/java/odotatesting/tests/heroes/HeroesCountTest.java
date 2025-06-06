package odotatesting.tests.heroes;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import odotatesting.base.BaseTest;
import odotatesting.pages.HeroesPage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies that the count of heroes displayed on the OpenDota web's Heroes page
 * matches the number of heroes retrieved from the OpenDota /heroStats API.
 */
public class HeroesCountTest extends BaseTest {

    private HeroesPage heroesPage;

    @BeforeMethod
    public void goToHeroesPage() {
        heroesPage = navigateToHeroesPage().clickProfessionalTab();
    }

    @Test
    public void testHeroesCount() {
        int heroesCountFromWeb = heroesPage.getHeroesCountFromWeb();
        int heroesCountFromAPI = heroesPage.getHeroesCountFromAPI();

        assertThat(heroesCountFromWeb)
                .as("Number of heroes in the table")
                .isNotNull()
                .isEqualTo(heroesCountFromAPI);

        logger.info("{} validation finished.", this.getClass().getSimpleName());
    }
}
