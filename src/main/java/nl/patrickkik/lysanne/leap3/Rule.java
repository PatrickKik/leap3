package nl.patrickkik.lysanne.leap3;

import static nl.patrickkik.lysanne.leap3.Test.*;

enum Rule {

    STEPS_X (TEST_A1, TEST_A3),
    COMPLETED_X (TEST_A2, TEST_A4),
    STEPS_I (TEST_B1, TEST_B3),
    COMPLETED_I (TEST_B2, TEST_B4);

    private final Test[] tests;

    Rule(Test... tests) {
        this.tests = tests;
    }

    Test[] tests() {
        return tests;
    }
}
