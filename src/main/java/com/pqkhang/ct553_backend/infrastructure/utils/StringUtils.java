package com.pqkhang.ct553_backend.infrastructure.utils;

import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.regex.Pattern;

@Component
public class StringUtils {
    public String normalizeString(String input) {
        if (input == null) {
            return null;
        }
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{M}+");
        return pattern.matcher(normalized).replaceAll("");
    }
}
