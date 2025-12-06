package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FormParser {

    public static Map<String, String> parseForm(InputStream input) throws IOException {
        String body = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));

        return parseFormData(body);
    }

    private static Map<String, String> parseFormData(String formData) {
        Map<String, String> map = new HashMap<>();
        if (formData == null || formData.isEmpty()) {
            return map;
        }
        for (String pair : formData.split("&")) {
            String[] entry = pair.split("=");
            if (entry.length == 2) {
                try {
                    map.put(
                            URLDecoder.decode(entry[0], StandardCharsets.UTF_8.name()),
                            URLDecoder.decode(entry[1], StandardCharsets.UTF_8.name())
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }
}
