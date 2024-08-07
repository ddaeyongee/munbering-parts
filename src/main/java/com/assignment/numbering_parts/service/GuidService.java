package com.assignment.numbering_parts.service;

import com.assignment.numbering_parts.util.GuidGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

@Service
public class GuidService {
    /*
    * 17자리 타임스탬프 (yyyyMMddHHmmssSSS)
    * 4자리 서버 추적 정보
    * 9자리 랜덤 접미사
    */
    public String generateGuid() {
        String timestamp = GuidGenerator.generateTimestamp();
        String serverTrace = getServerTraceFromHeader();
        String randomSuffix = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 9);
        return String.format("%s%s%s", timestamp, serverTrace, randomSuffix);
    }

    private String getServerTraceFromHeader() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String serverTrace = request.getHeader("X-Server-Trace");
            if (serverTrace != null) {
                return String.format("%04s", serverTrace).substring(0, 4);
            }
        }
        return "0000";
    }
}