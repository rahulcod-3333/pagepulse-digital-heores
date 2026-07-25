package com.digitalheroes.pagepulse.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FetchResult {
    private Document document;

    private int statusCode;

    private long responseTime;

    private String contentType;
}
