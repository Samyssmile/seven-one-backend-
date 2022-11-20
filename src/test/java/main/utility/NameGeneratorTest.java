package main.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NameGeneratorTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void generateRealName() throws IOException {
        String name = NameGenerator.generateUsername();
        assertNotNull(name);
        assertTrue(name.length() > 0);
    }

    @Test
    void generateUserName() throws IOException {
        String[] names = NameGenerator.getUserNames();
        assertNotNull(names);
        assertTrue(names.length > 0);

    }

}
