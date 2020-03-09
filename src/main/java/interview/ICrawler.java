package interview;

import java.util.Map;

public interface ICrawler {
    Map<String, Integer> getUrlLinks(String url, int depth);
}
