package nl.patrickkik.lysanne.leap3;

import java.util.Arrays;

enum Test {

    TEST_A1, TEST_A2, TEST_A3, TEST_A4,
    TEST_B1, TEST_B2, TEST_B3, TEST_B4;

    String code() {
        return "Test" + name().substring(5, 7);
    }

    public static Test of(String code) {
        return Arrays.stream(Test.values())
                .filter(testType -> testType.code().equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code));
    }
}
