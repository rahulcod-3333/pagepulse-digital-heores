package com.digitalheroes.pagepulse.controller;

import com.digitalheroes.pagepulse.dto.AuditRequest;
import com.digitalheroes.pagepulse.dto.AuditResponse;
import com.digitalheroes.pagepulse.service.AuditService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:5173" , "https://pagepulse-digital-heores.vercel.app"})
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {
    private final AuditService auditService;
    @PostMapping
    public ResponseEntity<AuditResponse> audit(@RequestBody @Valid AuditRequest auditRequest){
        return ResponseEntity.ok(auditService.audit(auditRequest.getUrl()));
    }

}
