package odotatesting.tests.heroes;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import odotatesting.base.BaseTest;
import odotatesting.pages.HeroesPage;
import odotatesting.processors.heroes.HeroImagesProc;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies if all hero images are loaded correctly by verifying their visibility
 * and natural width.
 */
public class HeroImagesTest extends BaseTest {

    private HeroesPage heroesPage;
    HeroImagesProc heroImages;

    @BeforeMethod
    public void goToHeroesPage() {
        heroesPage = homePage.navigateToHeroesPage().clickProfessionalTab();

        heroImages = new HeroImagesProc(heroesPage.getPage());
        logger.info("URL: {}", heroesPage.getPageURL());
    }

    @Test
    public void testHeroImages() {
        boolean allImagesLoaded = heroImages.areAllHeroImagesLoaded(heroesPage);

        assertThat(allImagesLoaded)
                .as("Check if all hero images are loaded")
                .isTrue();

        logger.info("Hero images test completed successfully.");
    }
}
