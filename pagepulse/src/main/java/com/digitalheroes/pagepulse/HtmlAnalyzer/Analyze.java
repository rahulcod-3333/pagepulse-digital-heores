package com.digitalheroes.pagepulse.HtmlAnalyzer;

import com.digitalheroes.pagepulse.dto.AnalysisDto;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class Analyze {
    public AnalysisDto analyze(Document document){
        return AnalysisDto.builder()
                .title(getTitle(document))
                .metaDescription(getMetadescription(document))
                .h1Count(getH1Count(document))
                .missingAltImages(getMissingAltImages(document))
                .wordCount(getWordCount(document))
                .build();
    }
    private String getTitle(Document document){
        return document.title();
    }
    private String getMetadescription(Document document){
        Element metaTag = document.selectFirst("meta[name=description]");
        return metaTag != null ?  metaTag.attr("content")
                : "No meta description found";
    }
    private int getH1Count(Document document) {
        return document.select("h1").size();
    }
    private int getMissingAltImages(Document document) {

        Elements images = document.select("img");
        int count = 0;
        for (Element image : images) {
            if (!image.hasAttr("alt")
                    || image.attr("alt").isBlank()) {

                count++;
            }
        }

        return count;
    }
    private int getWordCount(Document document) {
        String text = document.body().text();
        if (text == null || text.isBlank()) {
            return 0;
        }
        return text.trim().split("\\s+").length;
    }
}
