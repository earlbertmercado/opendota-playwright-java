package odotatesting.utils;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import odotatesting.constants.ApiConstants;

public class CallOpendotaAPI {

    /**
     * Sends a GET request to the specified URL and parses the response
     * body into the desired JSON type (JSONObject or JSONArray).
     */
    private static <T> T sendGetRequest(String url, Class<T> jsonType) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse getResponse = httpClient.execute(new HttpGet(url))) {

            String getResponseBody = EntityUtils.toString(getResponse.getEntity());

            if (jsonType.equals(JSONObject.class)) {
                return jsonType.cast(new JSONObject(getResponseBody));
            } else if (jsonType.equals(JSONArray.class)) {
                return jsonType.cast(new JSONArray(getResponseBody));
            } else {
                System.err.println("Unsupported JSON type requested: " + jsonType.getName());
                return null;
            }
        } catch (IOException e) {
            System.err.println("Error during GET request to " + url + ": " + e.getMessage());
            return null;
        }
    }

    public static JSONObject getMatchDetails(String matchId) {
        return sendGetRequest(ApiConstants.MATCHES_ENDPOINT + "/" + matchId, JSONObject.class);
    }

    public static JSONObject constants(String resource) {
        return sendGetRequest(ApiConstants.CONSTANTS_ENDPOINT + "/" + resource, JSONObject.class);
    }

    public static JSONArray heroStats() {
        return sendGetRequest(ApiConstants.HERO_STATS_ENDPOINT, JSONArray.class);
    }
}