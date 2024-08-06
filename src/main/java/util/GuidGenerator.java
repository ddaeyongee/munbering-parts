package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GuidGenerator {
    private static final DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    public static String generateTimestamp() {
        return LocalDateTime.now().format(timestampFormatter);
    }
}