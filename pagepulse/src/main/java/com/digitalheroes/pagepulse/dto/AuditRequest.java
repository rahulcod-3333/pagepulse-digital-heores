package com.digitalheroes.pagepulse.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditRequest {
    @NotBlank(message = "URL cannot be empty")
    private String url;

}
