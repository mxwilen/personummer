package src.main;

import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ValidityCheck {

    public ValidityCheck(String credentialID) {
        Credential credential;
        if (!isValidString(credentialID)) {
            throw new ValidationException("ValidationException: Input doesn't match required format");
        }

        try {
            // credential = new CredentialNumber(credentialID);
            credential = new CredentialFactory().generateCredential(credentialID);
        } catch (ParsingException e) {
            throw new ValidationException(e.getMessage());
        } catch (DateTimeParseException e) {
            throw new ValidationException("DateTimeParseException: " + e.getMessage());
        }

        try {
            if (isValidLuhn(credential.getMinimalDate(),
                    credential.getBirthNumber(),
                    credential.getCheckSum())) {
                credential.outputResult();
            }
        } catch (LuhnException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    /**
     * Validates a string against a regex pattern.
     *
     * @param input The input string to validate.
     * @return True if the string matches the pattern, false otherwise.
     */
    public static boolean isValidString(String input) {
        String regex = "^((\\d{6}|\\d{8})[+-]\\d{4}|\\d{10,12})$";
        return Pattern.matches(regex, input);
    }

    // Method to check if a number is valid using the Luhn algorithm
    public static boolean isValidLuhn(String yymmdd, String birthNumber, char checksum) {
        String localString = yymmdd + birthNumber;
        int digit;
        boolean alternate = true;
        int nSum = 0;

        // Iterate over the digits from right to left
        for (int i = localString.length() - 1; i >= 0; i--) {
            digit = Character.getNumericValue(localString.charAt(i));

            if (alternate) {
                // Double every second digit
                digit *= 2;
                if (digit > 9) {
                    digit -= 9; // Subtract 9 if the doubled value is greater than 9
                }
            }

            nSum += digit;
            alternate = !alternate; // Flip the alternate flag
        }
        int calculatedCheckSum = ((10 - (nSum % 10)) % 10);

        boolean isValid = (Character.getNumericValue(checksum) == calculatedCheckSum);
        if (!isValid)
            throw new LuhnException("LuhnException: Checksum is not valid: yymmdd=" + yymmdd + ", calculated checksum="
                    + calculatedCheckSum);

        return isValid;
    }

    public static void main(String[] args) {
        List<String> pns = Arrays.asList(
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
                "1666013-1111", // Invalid
                "16660133-1111" // Invalid
        );

        List<String> sams = Arrays.asList(
                "190910799824" // Valid
        );

        List<String> orgs = Arrays.asList(
                "556614-3185", // Valid
                "16556601-6399", // Valid
                "262000-1111", // Valid
                "857202-7566" // Valid
        );

        System.out.println("PNS Validation Results:");
        for (String credentialID : pns) {
            try {
                new ValidityCheck(credentialID);
            } catch (ValidationException e) {
                System.out.printf(
                        """
                                INVALID     Credential ID         : %s

                                """, credentialID);
                System.err.println(e.getMessage());
                System.err.printf("-------------------------------------------------\n");
            }
        }

        System.out.println("\n\nSAMS Validation Results:");
        for (String credentialID : sams) {
            try {
                new ValidityCheck(credentialID);
            } catch (ValidationException e) {
                System.out.printf(
                        """
                                INVALID     Credential ID         : %s

                                """, credentialID);
                System.err.println(e.getMessage());
                System.err.printf("-------------------------------------------------\n");
            }
        }

        System.out.println("\n\nORGS Validation Results:");
        for (String credentialID : orgs) {
            try {
                new ValidityCheck(credentialID);
            } catch (ValidationException e) {
                System.out.printf(
                        """
                                INVALID     Credential ID         : %s

                                """, credentialID);
                System.err.println(e.getMessage());
                System.err.printf("-------------------------------------------------\n");
            }
        }
    }
}
