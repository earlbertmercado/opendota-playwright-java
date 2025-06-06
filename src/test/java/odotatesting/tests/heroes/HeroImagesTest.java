package odotatesting.tests.heroes;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import odotatesting.base.BaseTest;
import odotatesting.pages.HeroesPage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies if all hero images are loaded correctly by verifying their visibility
 * and natural width.
 */
public class HeroImagesTest extends BaseTest {

    private HeroesPage heroesPage;

    @BeforeMethod
    public void goToHeroesPage() {
        heroesPage = navigateToHeroesPage().clickProfessionalTab();
    }

    @Test
    public void testHeroImages() {
        boolean allImagesLoaded = heroesPage.areAllHeroImagesLoaded();

        assertThat(allImagesLoaded)
                .as("Check if all hero images are loaded")
                .isTrue();

        logger.info("{} validation finished.", this.getClass().getSimpleName());
    }
}
