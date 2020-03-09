package interview;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Crawler implements ICrawler {
    public Map<String, Integer> getUrlLinks(String url, int depth) {

        System.out.println("Start of crawling url: " + url + " with depth " + depth);

        final ConcurrentMap<String, Integer> urlStatusesDictionary = new ConcurrentHashMap<>(100);
        AddLinksAsync(url, depth, urlStatusesDictionary);

        System.out.println("End of crawling url: " + url + " with depth " + depth);
        return urlStatusesDictionary;
    }

    private void AddLinksAsync(String url, int depth, ConcurrentMap<String, Integer> urlStatusesDictionary) {
        if (url == null || url.trim() == "" || urlStatusesDictionary.containsKey(url.trim()))
            return;

        System.out.println("Adding links for url: " + url + " with depth:" + depth);
        int httpResponse = CheckUrlAvailability(url);

        urlStatusesDictionary.putIfAbsent(url.trim(), httpResponse);

        if (depth <= 0 || httpResponse != HttpURLConnection.HTTP_OK){
            System.out.println("No more nested for url: " + url);
            return;
        }

        Set<String> innerLinks = GetInnerLinks(url);
        depth--;
        int finalDepth = depth;

        innerLinks.parallelStream().forEach(innerLink ->
            AddLinksAsync(innerLink, finalDepth, urlStatusesDictionary)
        );
    }

    private int CheckUrlAvailability(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return responseCode;
        } catch (Exception e) {
            System.out.println(String.format("Failed while checking availability for %s, due to %s", url, e));
            return HttpURLConnection.HTTP_UNAVAILABLE;
        }
    }

    private Set<String> GetInnerLinks(String url) {

        try {
            Document document = Jsoup.connect(url).get();
            Elements pageLinks = document.select("a[href]");

            Set<String> innerLinks = new HashSet<String>();
            for (Element linkElement : pageLinks) {
                innerLinks.add(linkElement.attr("abs:href"));
            }
            System.out.println(String.format("Found %d inner links for url:%s", innerLinks.size(), url));
            return innerLinks;
        } catch (IOException e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }
}
