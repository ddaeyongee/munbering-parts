package com.assignment.numbering_parts.service;

import com.assignment.numbering_parts.util.GuidGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GuidService {
    private final String serverId;

    public GuidService(@Value("${server.id}") String serverId) {
        this.serverId = normalizeServerId(serverId);
    }

    private String normalizeServerId(String id) {
        if (id == null) {
            id = "";
        }
        if (id.length() < 4) {
            return String.format("%4s", id).replace(' ', '0');
        } else if (id.length() > 4) {
            return id.substring(0, 4);
        }
        return id;
    }

    //serverId 4자리
    //timestamp 17자리
    //randomSuffix 9자리
    //4+17+9=30자리
    public String generateGuid() {
        String timestamp = GuidGenerator.generateTimestamp();
        String randomSuffix = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 9);
        return String.format("%s%s%s", serverId, timestamp, randomSuffix);
    }
}