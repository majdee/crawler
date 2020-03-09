package interview;

import java.util.Map;

public class Program {
    public static void main(String[] args) {

        try {
            ValidateParameters(args);
            String url = args[0];
            int depth = Integer.parseInt(args[1]);

            ICrawler crawler = new Crawler();
            Map<String, Integer> urlLinks = crawler.getUrlLinks(url, depth);

            PrintResult(urlLinks);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void PrintResult(Map<String, Integer> urlLinks) {
        System.out.println("******************** Crawler Result ********************");
        System.out.println("Here are the result of the crawler");

        urlLinks.forEach((url, status) ->System.out.println(url + ":" + status));

        System.out.println("Total: " + urlLinks.size());
        System.out.println("******************** END ********************");
    }

    private static void ValidateParameters(String[] args) throws Exception {
        if (args == null || args.length != 2) {
            throw new Exception("Invalid arguments, please provide both url and depth number");
        }
        Validator validator = new Validator();
        validator.ValidateUrl(args[0],"Invalid url");
        validator.ValidateInt(args[1],"Invalid depth number");
    }
}
