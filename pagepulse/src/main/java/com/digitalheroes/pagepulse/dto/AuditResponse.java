package com.digitalheroes.pagepulse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditResponse {
    private int status;
    private long responseTime;
    private String title;
    private String metaDescription;
    private int h1Count;
    private int missingAltImages;
    private int wordCount;
}
