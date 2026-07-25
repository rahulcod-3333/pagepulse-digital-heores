package com.digitalheroes.pagepulse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnalysisDto {
    private String title;

    private String metaDescription;

    private int h1Count;

    private int missingAltImages;

    private int wordCount;
}
