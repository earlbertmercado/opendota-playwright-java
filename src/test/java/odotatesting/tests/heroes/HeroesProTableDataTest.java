package odotatesting.tests.heroes;

import odotatesting.processors.heroes.HeroesProTableDataProc;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import odotatesting.base.BaseTest;
import odotatesting.pages.HeroesPage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test class validates the data displayed in the Heroes
 * table on the Heroes page Pro tab. It collects data from the
 * table, and then performs checks if there are null or empty
 * entries
 */
public class HeroesProTableDataTest extends BaseTest {

    private HeroesPage heroesPage;
    HeroesProTableDataProc heroesTableDataProc;

    @BeforeMethod
    public void goToHeroesPage() {
        heroesPage = navigateToHeroesPage().clickProfessionalTab();
        heroesTableDataProc = new HeroesProTableDataProc(heroesPage.getPage());
    }

    @Test
    public void testHeroesTableData() {
        String heroStatsFromTable = heroesTableDataProc.getHeroStatsAsJson();
        logger.info("Hero stats from table: {}", heroStatsFromTable);

        boolean doesTableContainValidData = heroesTableDataProc.doesTableContainValidData();

        assertThat(doesTableContainValidData)
                .as("Check if heroes table data is valid")
                .isTrue();

        logger.info("{} validation finished.", this.getClass().getSimpleName());
    }
}
