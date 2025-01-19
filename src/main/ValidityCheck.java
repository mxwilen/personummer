package src.main;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ValidityCheck {

    public ValidityCheck(String credentialID) {
        String regex = "^((\\d{6}|\\d{8})[+-]\\d{4}|\\d{10,12})$";

        if (!isValidString(credentialID, regex)) {
            throw new ValidationException("Invalid input format!");
        }

        CredentialNumber credential = new CredentialNumber(credentialID);
        try {
            if (isValidLuhn(credential.getMinimalDate(),
                    credential.getbirthNumber(),
                    credential.getCheckSum())) {
                System.out.printf(
                        """
                                -------------------------------------------------
                                   VALID    Credential ID         : %s
                                            Birth Number          : %s
                                            Checksum              : %s
                                            Birth Date (YYYYMMDD) : %s
                                            Credential Type       : %s
                                            """,
                        credential.getCredentialID(),
                        credential.getbirthNumber(),
                        credential.getCheckSum(),
                        credential.getFullDate(),
                        credential.getCredType());
            }
        } catch (ValidationException e) {
            System.out.printf(
                    """
                            -------------------------------------------------
                              INVALID   Credential ID         : %s
                                        Birth Number          : %s
                                        Checksum              : %s
                                        Birth Date (YYYYMMDD) : %s
                                        Credential Type       : %s
                                        """,
                    credential.getCredentialID(),
                    credential.getbirthNumber(),
                    credential.getCheckSum(),
                    credential.getFullDate(),
                    credential.getCredType());
            System.err.printf("ValidationException for pn=%s: %s%n\n", credentialID, e.getMessage());
        }
    }

    /**
     * Validates a string against a regex pattern.
     *
     * @param input The input string to validate.
     * @param regex The regex pattern to match.
     * @return True if the string matches the pattern, false otherwise.
     */
    public static boolean isValidString(String input, String regex) {
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
            throw new ValidationException("Checksum is not valid: yymmdd=" + yymmdd);

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
            new ValidityCheck(credentialID);
        }

        System.out.println("\nSAMS Validation Results:");
        for (String credentialID : sams) {
            new ValidityCheck(credentialID);
        }

        System.out.println("\nORGS Validation Results:");
        for (String credentialID : orgs) {
            new ValidityCheck(credentialID);
        }
    }
}
