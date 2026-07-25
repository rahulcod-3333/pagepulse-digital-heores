package com.digitalheroes.pagepulse.fetcher;

import com.digitalheroes.pagepulse.Exceptions.FetchException;
import com.digitalheroes.pagepulse.Exceptions.InvalidUrlExceptions;
import com.digitalheroes.pagepulse.dto.FetchResult;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

@Component
@Slf4j
public class WebFetcher {
    private static final int TIMEOUT = 10000;

    public FetchResult fetch(String url){
        validateUrl(url);
        try{
            long start = System.currentTimeMillis();
             Connection connection =  Jsoup.connect(url)
                    .timeout(TIMEOUT)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                            "AppleWebKit/537.36 (KHTML, like Gecko) " +
                            "Chrome/138.0.0.0 Safari/537.36")
                    .ignoreHttpErrors(true);
             Connection.Response response = connection.execute();
            long responseTime = System.currentTimeMillis() - start;
            String contentType = response.contentType();
            if (contentType == null || !contentType.contains("text/html")) {
                throw new FetchException("URL does not point to an HTML page.");
            }
            Document document = response.parse();

            log.info("Successfully fetched {} — status={}, responseTime={}ms, contentType={}",
                    url, response.statusCode(), responseTime, contentType);
            return FetchResult.builder()
                    .document(document)
                    .statusCode(response.statusCode())
                    .responseTime(responseTime)
                    .contentType(contentType)
                    .build();

        } catch (IOException e) {
            log.error("Failed to fetch {}", url, e);
            throw new FetchException("Unable to fetch webpage: " + e.getClass().getSimpleName());
        }
    }
    private void validateUrl(String url){
        try{
            URI.create(url).toURL();

        } catch (IllegalArgumentException | MalformedURLException e) {
            log.error("Invalid Url format {}",url,e);
            throw new InvalidUrlExceptions("Invalid Url Format...");
        }
    }
}
