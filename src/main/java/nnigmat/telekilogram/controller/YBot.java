package nnigmat.telekilogram.controller;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.Json;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class YBot {

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static YouTube youTube;

    public Pair<String, ArrayList<String>> getChannelInfo(String channelName) throws IOException {
        youTube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, null).setApplicationName("Searcher").build();
        YouTube.Search.List search = youTube.search().list("snippet");
        search.setKey("AIzaSyAonteAvhZ1HAB8lBeZ4cgKv8Y5X8jfi1s");

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

        if (channelId != null) {
            search.setChannelId(channelId);
            search.setMaxResults(5L);
            search.setQ("");
            search.setOrder("date");

            resp = search.execute();
            ArrayList<String> hrefs = new ArrayList<>();
            for (SearchResult item: resp.getItems()) {
                hrefs.add("https://youtube.com/watch?v=" + item.getId().getVideoId());
            }
            return Pair.of("https://youtube.com/channel/" + channelId, hrefs);
        }
        return null;
    }
}
