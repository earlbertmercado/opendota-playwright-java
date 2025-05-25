package odotatesting.tests.matches;

import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import odotatesting.base.BaseTest;
import odotatesting.pages.MatchesPage;
import odotatesting.processors.matches.OverviewBasicStatsProc;
import odotatesting.utils.CallOpendotaAPI;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies the accuracy of basic match statistics (level, kills,
 * deaths, assists, last hits, denies, networth, gpm, xpm, hero damage,
 * tower damage, and hero healing) of each player displayed on the
 * OpenDota web's Matches Overview section against the data retrieved
 * directly from the OpenDota /matches API.
 */
public class OverviewBasicStatsTest extends BaseTest {

    private MatchesPage matchesPage;
    private OverviewBasicStatsProc overviewBasicStatsProc;

    private static final List<String> STAT_KEYS = Arrays.asList(
            "level", "kills", "deaths", "assists", "last_hits", "denies",
            "net_worth", "gold_per_min", "xp_per_min", "hero_damage",
            "tower_damage", "hero_healing"
    );

    @BeforeMethod
    public void findMatch() {
        matchesPage = homePage
                .navigateToMatchesPage()
                .clickTopPublicTab()
                .clickFirstTopPubMatchOnList()
                .clickMatchesOverview();

        overviewBasicStatsProc = new OverviewBasicStatsProc(matchesPage.getPage());
        logger.info("URL: {}", matchesPage.getPageURL());
    }

    @Test
    public void testMatchesOverviewBasicStats() {
        logger.info("Match ID: {}", matchesPage.getMatchId());

        JSONObject matchesAPI = CallOpendotaAPI.matches(matchesPage.getMatchId());

        logger.info("RADIANT TEAM");
        String radiantStatsFromWeb = overviewBasicStatsProc.extractMatchStatsFromWeb(STAT_KEYS, "Radiant");
        String radiantStatsFromAPI = overviewBasicStatsProc.extractMatchStatsFromAPI(matchesAPI, "Radiant");

        logger.info("DIRE TEAM");
        String direStatsFromWeb = overviewBasicStatsProc.extractMatchStatsFromWeb(STAT_KEYS, "Dire");
        String direStatsFromAPI = overviewBasicStatsProc.extractMatchStatsFromAPI(matchesAPI, "Dire");

        assertThat(radiantStatsFromWeb)
                .as("Radiant stats from web")
                .isNotNull()
                .isEqualTo(radiantStatsFromAPI);

        assertThat(direStatsFromWeb)
                .as("Dire stats from web")
                .isNotNull()
                .isEqualTo(direStatsFromAPI);
    }
}