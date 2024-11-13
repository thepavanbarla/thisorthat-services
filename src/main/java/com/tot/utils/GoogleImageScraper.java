package com.tot.utils;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;

/**
 * @author karthik on 12/12/22.
 * @project totservices
 */

@Slf4j
public class GoogleImageScraper {

    public static void main(String[] args) throws Exception {

        String content = getSearchContent("cafe racer");
        System.out.println(content);

        //        String outPath = "/Users/karthik/Documents/this_that/totservices/download_images/";
        //        String regex = "s='data:image/jpeg;base64,(.*?)';";
        parseImageUrls(content, 30);

    }

    public static List<String> getImagesFromGoogle(String keywords, Integer limit)
        throws Exception {
        String content = getSearchContent(keywords);
        return parseImageUrls(content, limit);
    }

    private static List<String> parseImageUrls(String content, Integer limit) {
        String regex = "http(s)?://[^\":]*?\\.(jpg|jpeg)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        List<String> urls = new ArrayList<>();

        int count = 0;
        while (matcher.find() && count < limit) {
            String img = matcher.group();
            urls.add(img);
            count++;
            //            downloadImageFromUrlToLocal(img, outPath);
        }
        return urls;
    }

    private static void downloadImageFromUrlToLocal(String img, String outPath) {
        try (InputStream in = new URL(img).openStream()) {
            Files.copy(in, Paths.get(outPath + UUID.randomUUID() + ".jpg"));
            System.out.println("Download successful: " + img);

        } catch (IOException e) {
            // handle IOException
            System.err.println("Failed to download: " + img);
        }
    }

    public static String getSearchContent(String googleSearchQuery) throws Exception {
        //URL encode string in JAVA to use with google search
        log.info("Searching for: " + googleSearchQuery);
        googleSearchQuery = googleSearchQuery.trim();
        googleSearchQuery = URLEncoder.encode(googleSearchQuery, StandardCharsets.UTF_8.toString());
        //        String queryUrl = "https://www.google.com/search?q=" + googleSearchQuery + "&num=10";
        String queryUrl = String.format(
            "https://www.google.com/search?safe=off&site=&tbm=isch&source=hp&q=%1$s&oq=%1$s&gs_l=img",
            googleSearchQuery);
        //        queryUrl = String.format("https://www.google.com/search?q=%1$s&tbm=isch&ved=2ahUKEwiHgIP_kvT7AhWgzqACHTnPCZ8Q2-cCegQIABAA&oq=%1$s&gs_lcp=CgNpbWcQAzIICAAQgAQQsQMyBQgAEIAEMgUIABCABDIFCAAQgAQyBQgAEIAEMgUIABCABDIFCAAQgAQyBQgAEIAEMgUIABCABDIFCAAQgAQ6BwgAELEDEEM6BAgAEENQwQJY9gxg1Q5oAHAAeACAAYkBiAGGBZIBAzYuMZgBAKABAaoBC2d3cy13aXotaW1nwAEB&sclient=img&ei=lyeXY8fYI6Cdg8UPuZ6n-Ak&bih=821&biw=1440", googleSearchQuery);

        log.info("Search url formed: " + queryUrl);

        //        final String agent = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
        final String agent =
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36";
        URL url = new URL(queryUrl);
        final URLConnection connection = url.openConnection();
        /**
         * User-Agent is mandatory otherwise Google will return HTTP response
         * code: 403
         */
        connection.setRequestProperty("User-Agent", agent);
        final InputStream stream = connection.getInputStream();
        String result = new BufferedReader(new InputStreamReader(stream)).lines().parallel()
            .collect(Collectors.joining("\n"));

        return result;
    }


}
