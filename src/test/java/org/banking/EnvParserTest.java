package org.banking;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class EnvParserTest {
    @Test
    public void EnvGetterTest() throws IOException {
        HashMap<String, String> EnvVariables = EnvParser.parseEnvFile("C:/Users/Vahin/IdeaProjects/BankingProject/.env");

        assertEquals(EnvVariables.get("TEST_VALUE"), "Hello World");
    }

}
