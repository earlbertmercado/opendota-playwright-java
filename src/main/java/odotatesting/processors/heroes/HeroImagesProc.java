package odotatesting.processors.heroes;

import java.util.List;
import java.util.Objects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import odotatesting.constants.HeroesPageLocators;
import odotatesting.pages.HeroesPage;

public class HeroImagesProc {

    private static final Logger logger = LogManager.getLogger(HeroImagesProc.class);
    private final Page page;

    public HeroImagesProc(Page page) {
        this.page = page;
    }

    /**
     * Checks if all hero images on the Heroes page are loaded correctly.
     * It verifies that each image is visible and has a non-zero natural width.
     *
     * @return true if all hero images are loaded correctly, false otherwise.
     */
    public boolean areAllHeroImagesLoaded() {
        waitForHeroImagesToLoad();
        HeroesPage heroesPage = new HeroesPage(page);
        List<Locator> heroImages = heroesPage.getHeroImages();

        if (heroImages.isEmpty()) {
            logger.warn("No hero images found.");
            return false;
        }

        logger.info("Found {} images. Validating...", heroImages.size());

        boolean allImagesValid = true;
        for (int i = 0; i < heroImages.size(); i++) {
            Locator image = heroImages.get(i);
            if (!isValidHeroImage(image, i)) {
                allImagesValid = false;
            }
        }

        if (allImagesValid) {
            logger.info("All {} hero images loaded correctly.", heroImages.size());
        } else {
            logger.error("Some hero images failed to load.");
        }

        return allImagesValid;
    }

    private void waitForHeroImagesToLoad() {
        try {
            page.waitForSelector(HeroesPageLocators.ALL_HERO_IMAGES, new Page.WaitForSelectorOptions()
                    .setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
            logger.debug("Image selector visible.");
        } catch (Exception e) {
            logger.error("Wait for image selector failed: {}", e.getMessage());
        }
    }

    // Validates a single hero image's visibility and natural width.
    private boolean isValidHeroImage(Locator image, int index) {
        String src = "N/A";
        boolean isVisible = false;
        double naturalWidth = 0.0;

        try {
            src = Objects.requireNonNullElse(image.getAttribute("src"), "MISSING_SRC");
            isVisible = image.isVisible();
            naturalWidth = getNaturalWidth(image);

            if (!isVisible || naturalWidth <= 0.0) {
                logger.error("Broken image [{}]: src='{}', visible={}, width={}",
                        index, src, isVisible, naturalWidth);
                return false;
            } else {
//                logger.debug("Image [{}] OK: src='{}', visible={}, width={}",
//                        index, src, isVisible, naturalWidth);
                return true;
            }
        } catch (Exception e) {
            logger.error("Image validation failed [{}]: src='{}' - {}",
                    index, src, e.getMessage());
            return false;
        }
    }

    // Retrieves the natural width of an image using JavaScript evaluation.
    private double getNaturalWidth(Locator image) {
        try {
            Number widthValue = (Number) image.evaluate("img => img.naturalWidth", image.elementHandle());
            return (widthValue != null) ? widthValue.doubleValue() : 0.0;
        } catch (ClassCastException e) {
            logger.error("Cast error for naturalWidth: {}", e.getMessage());
            return 0.0;
        } catch (Exception e) {
            logger.error("NaturalWidth evaluation error: {}", e.getMessage());
            return 0.0;
        }
    }
}