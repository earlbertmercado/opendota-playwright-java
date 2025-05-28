package odotatesting.tests.heroes;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import odotatesting.base.BaseTest;
import odotatesting.pages.HeroesPage;
import odotatesting.processors.heroes.HeroesTableDataProc;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test class validates the data displayed in the Heroes
 * table on the Heroes page. It collects data from the table,
 * and then performs checks if there are null or empty entries
 */
public class HeroesTableDataTest extends BaseTest {

    private HeroesPage heroesPage;
    HeroesTableDataProc heroesTableDataProc;

    @BeforeMethod
    public void goToHeroesPage() {
        heroesPage = homePage.navigateToHeroesPage().clickProfessionalTab();

        heroesTableDataProc = new HeroesTableDataProc(heroesPage.getPage());
        logger.info("URL: {}", heroesPage.getPageURL());
    }

    @Test
    public void testHeroesTableData() {
        String heroStatsFromTable = heroesTableDataProc.getHeroStatsAsJson();
        logger.info("Hero stats from table: {}", heroStatsFromTable);

        boolean doesTableContainValidData = heroesTableDataProc.doesTableContainValidData();

        assertThat(doesTableContainValidData)
                .as("Check if heroes table data is valid")
                .isTrue();

        logger.info("{} completed successfully.", this.getClass().getSimpleName());
    }
}
