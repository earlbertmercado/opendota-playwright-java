package odotatesting.tests.matches;

import java.util.Map;

import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import odotatesting.base.BaseTest;
import odotatesting.pages.MatchesPage;
import odotatesting.utils.CallOpendotaAPI;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies the accuracy of match summary (team won, radiant kills,
 * dire kills, and match duration) displayed on the OpenDota web's
 * Matches Overview section against the data retrieved directly from
 * the OpenDota /matches API.
 */
public class MatchSummaryTest extends BaseTest {

    private MatchesPage matchesPage;

    @BeforeMethod
    public void findMatch() {
        matchesPage = navigateToMatchesPage()
                .clickTopPublicTab()
                .clickFirstTopPubMatchOnList()
                .clickMatchesOverview();
    }

    @Test
    public void testMatchSummary() {
        logger.info("Match ID: {}", matchesPage.getMatchId());
        JSONObject matchDetailsFromAPI = CallOpendotaAPI.matches(matchesPage.getMatchId());

        Map<String, Object> matchSummaryDataFromWeb = matchesPage.getMatchSummaryFromWeb();
        Map<String, Object> matchSummaryDataFromAPI = matchesPage.getMatchSummaryFromAPI(matchDetailsFromAPI);

        assertThat(matchSummaryDataFromWeb)
                .as("Match summary data from web")
                .isNotNull()
                .isEqualTo(matchSummaryDataFromAPI);

        logger.info("{} validation finished.", this.getClass().getSimpleName());
    }
}
