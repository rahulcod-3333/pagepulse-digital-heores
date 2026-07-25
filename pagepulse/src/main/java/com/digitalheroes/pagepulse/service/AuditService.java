package com.digitalheroes.pagepulse.service;

import com.digitalheroes.pagepulse.dto.AuditResponse;

public interface AuditService {
    AuditResponse audit(String url);
}
