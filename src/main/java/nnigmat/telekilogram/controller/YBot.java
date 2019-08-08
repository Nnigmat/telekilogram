package nnigmat.telekilogram.controller;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.Json;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import org.springframework.data.util.Pair;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Component
public class YBot {

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static YouTube youTube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, null).setApplicationName("Searcher").build();
    private static String key = "AIzaSyAonteAvhZ1HAB8lBeZ4cgKv8Y5X8jfi1s";

    public Pair<String, ArrayList<String>> getChannelInfo(String channelName) throws IOException {
        String channelId = getChannelIdByName(channelName);
        YouTube.Search.List search = youTube.search().list("snippet");
        search.setKey(key);

        if (channelId != null) {
            search.setChannelId(channelId);
            search.setMaxResults(5L);
            search.setQ("");
            search.setOrder("date");

            SearchListResponse resp = search.execute();
            ArrayList<String> hrefs = new ArrayList<>();
            for (SearchResult item: resp.getItems()) {
                hrefs.add("https://youtube.com/watch?v=" + item.getId().getVideoId());
            }
            return Pair.of("https://youtube.com/channel/" + channelId, hrefs);
        }
        return null;
    }

    public HashMap<String, String> getVideoInfo(String channelName, String videoName) throws IOException {
        String channelId = getChannelIdByName(channelName);
        if (channelId == null) {
            return null;
        }

        YouTube.Search.List search = youTube.search().list("snippet");
        search.setKey(key);
        search.setChannelId(channelId);
        search.setQ(videoName);
        search.setOrder("date");
        search.setMaxResults(1L);

        SearchResult resp = search.execute().getItems().get(0);
        YouTube.Videos.List video = youTube.videos().list("snippet,contentDetails,statistics");
        video.setKey(key);
        video.setId(resp.getId().getVideoId());

        VideoListResponse response = video.execute();
        return new HashMap<String, String>() {{
            put("href", "https://youtube.com/watch?v=" + resp.getId().getVideoId());
            put("views", String.valueOf(response.getItems().get(0).getStatistics().getViewCount()));
            put("likes", String.valueOf(response.getItems().get(0).getStatistics().getLikeCount()));
        }};
    }

    public String getChannelIdByName(String channelName) throws IOException {
        YouTube.Search.List search = youTube.search().list("snippet");
        search.setKey(key);

        // Execute request
        search.setQ(channelName);
        SearchListResponse resp = search.execute();

        String channelId = null;
        for (SearchResult item: resp.getItems()) {
            if (item.getSnippet().getChannelTitle().toLowerCase().equals(channelName.toLowerCase())) {
                channelId = item.getSnippet().getChannelId();
                break;
            }
        }
        return channelId;
    }

    public Pair<String, String> getRandomVideoComment(String channelName, String videoName) throws IOException {
        HashMap<String, String> videoInfo = getVideoInfo(channelName, videoName);
        String videoId = videoInfo.get("href").split("v=")[1];

        YouTube.CommentThreads.List comments = youTube.commentThreads().list("snippet,replies");
        comments.setKey(key);
        comments.setVideoId(videoId);

        CommentThreadListResponse response = comments.execute();
        Random random = new Random();
        CommentThread comment = response.getItems().get(random.nextInt(Math.min(20, response.getItems().size())));
        return Pair.of(comment.getSnippet().getTopLevelComment().getSnippet().getAuthorDisplayName(),
                comment.getSnippet().getTopLevelComment().getSnippet().getTextDisplay());
    }
}
