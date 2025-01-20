package src.test;

import java.util.Arrays;
import java.util.List;

import src.main.ValidityCheck;

public class ValidityCheckTest {
    public static void main(String[] args) {
        List<String> creds = Arrays.asList(
                // PER
                "201701102384", // Valid
                "141206-2380", // Valid
                "20080903-2386", // Valid
                "7101169295", // Valid
                "198107249289", // Valid
                "19021214-9819", // Valid
                "190910199827", // Valid
                "191006089807", // Valid
                "192109099180", // Valid
                "4607137454", // Valid
                "194510168885", // Valid
                "900118+9811", // Valid
                "189102279800", // Valid
                "189912299816", // Valid
                "201701272394", // Invalid
                "190302299813", // Invalid
                "4607137455", // Invalid
                "19900118+9811", // Valid ish?

                // SAM
                "190910799824", // Valid

                // ORG
                "556614-3185", // Valid
                "16556601-6399", // Valid
                "262000-1111", // Valid
                "857202-7566" // Valid
        );

        for (String credentialID : creds) {
            new ValidityCheck(credentialID);
        }
    }
}
