package org.banking;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class EnvParser {
    public static HashMap<String, String> parseEnvFile(String filePath) throws IOException {
        HashMap<String, String> envMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath)) ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    String key = parts[0];
                    String value = parts[1];
                    envMap.put(key, value);
                }
            }
        }

        return envMap;
    }
}

