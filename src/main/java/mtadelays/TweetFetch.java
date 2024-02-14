package mtadelays;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import org.json.JSONObject; // You need a JSON library, such as org.json

public class TweetFetch {

    private static final String BEARER_TOKEN = "AAAAAAAAAAAAAAAAAAAAAPMusQEAAAAA%2BcmNexW0R%2F%2FCbcoWyDtLKxSX5Ac%3DdR1rQbYmv2CO0Gsl9OwPGnIqvt1qKzUrrqFM7bck7Xz2UIRtB7";

    public static void main(String[] args) throws Exception {
        String username = "NYCTSubway";
        String userId = getUserIdByUsername(username);
        getRecentTweetsByUserId(userId, 5);
    }

    private static String getUserIdByUsername(String username) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.twitter.com/2/users/by/username/" + username))
                .header("Authorization", "Bearer " + BEARER_TOKEN)
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        JSONObject json = new JSONObject(response.body());
        System.out.println(json);
        
        return json.getJSONObject("data").getString("id");
    }

    private static void getRecentTweetsByUserId(String userId, int tweetCount) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("https://api.twitter.com/2/users/%s/tweets?max_results=%d", userId, tweetCount)))
                .header("Authorization", "Bearer " + BEARER_TOKEN)
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        JSONObject json = new JSONObject(response.body());
        json.getJSONArray("data").forEach(item -> {
            JSONObject tweet = (JSONObject) item;
            System.out.println(tweet.getString("text"));
        });
    }
}
