package com.digitalheroes.pagepulse.service.impl;

import com.digitalheroes.pagepulse.HtmlAnalyzer.Analyze;
import com.digitalheroes.pagepulse.dto.AnalysisDto;
import com.digitalheroes.pagepulse.dto.AuditResponse;
import com.digitalheroes.pagepulse.dto.FetchResult;
import com.digitalheroes.pagepulse.fetcher.WebFetcher;
import com.digitalheroes.pagepulse.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.Document;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {
    private final WebFetcher webFetcher;
    private final Analyze analyzer;

    @Override
    public AuditResponse audit(String url) {
        FetchResult fetchResult = webFetcher.fetch(url);
        AnalysisDto analysisDto = analyzer.analyze(fetchResult.getDocument());

        return AuditResponse.builder()
                .status(fetchResult.getStatusCode())
                .responseTime(fetchResult.getResponseTime())
                .title(analysisDto.getTitle())
                .metaDescription(analysisDto.getMetaDescription())
                .h1Count(analysisDto.getH1Count())
                .missingAltImages(analysisDto.getMissingAltImages())
                .wordCount(analysisDto.getWordCount())
                .build();

    }
}
