package com.assignment.numbering_parts.service;

import com.assignment.numbering_parts.util.GuidGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GuidService {
    private final String serverId;

    public GuidService(@Value("${server.id}") String serverId) {
        this.serverId = serverId;
    }

    public String generateGuid() {
        String timestamp = GuidGenerator.generateTimestamp();
        String randomSuffix = UUID.randomUUID().toString().substring(0, 8).replace("-", "");
        return String.format("%s-%s-%s", serverId, timestamp, randomSuffix);
    }
}
