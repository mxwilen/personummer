package src.main;

import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import src.main.Exceptions.LuhnException;
import src.main.Exceptions.ParsingException;
import src.main.Exceptions.ValidationException;
import src.main.Helper.Credential;
import src.main.Helper.CredentialFactory;

public class ValidityCheck {

    public ValidityCheck(String credentialID) {
        Credential credential;

        try {
            // Checks if input matches required format. If not, skip the other tests/parsing
            if (!isValidString(credentialID)) {
                throw new ValidationException("ValidationException: Input doesn't match required format");
            }

            // Coordination and Social Security Number: Parse input and check valid if date
            // Orgnization: Parse input and map with organization type
            try {
                credential = new CredentialFactory().generateCredential(credentialID);
            } catch (ParsingException e) {
                throw new ValidationException("ParsingException: " + e.getMessage());
            } catch (DateTimeParseException e) {
                throw new ValidationException("DateTimeParseException: " + e.getMessage());
            }

            // Use parsed data to validate checksum
            try {
                if (isValidLuhn(credential.getMinimalDate(),
                        credential.getBirthNumber(),
                        credential.getCheckSum())) {
                    credential.outputResult();
                }
            } catch (LuhnException e) {
                throw new ValidationException("LuhnException: " + e.getMessage());
            }

        } catch (ValidationException e) {
            // Exception has occured <=> Parsing failed <=> Invalid input
            System.out.printf(
                    """
                            INVALID     Credential ID         : %s

                            """, credentialID);
            System.err.println(e.getMessage());
            System.err.print("-------------------------------------------------\n");
        }
    }

    /**
     * Validates a string against a regex pattern.
     * 
     * @param input unparsed input string
     * @return boolean
     */
    private static boolean isValidString(String input) {
        /*
         * Regex:
         * - 6 or 8 digits, then '+' or '-', then 4 digits
         * or
         * - 10 to 12 digits
         */
        String regex = "^((\\d{6}|\\d{8})[+-]\\d{4}|\\d{10,12})$";
        return Pattern.matches(regex, input);
    }

    /**
     * Validates checksum by performing Luhn calculation on parsed input strings
     * 
     * @param yymmdd
     * @param birthNumber
     * @param checksum
     * @return boolean
     */
    private static boolean isValidLuhn(String yymmdd, String birthNumber, char checkSum) {
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

        boolean isValid = (Character.getNumericValue(checkSum) == calculatedCheckSum);
        if (!isValid)
            throw new LuhnException("Checksum is not valid: yymmdd=" + yymmdd + ", calculated checksum="
                    + calculatedCheckSum);

        return true;
    }

    /**
     * Only used when runnning ´run_scanner.sh´
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input;

        while (true) {
            System.out.print("Provide input ('q' to quit): ");
            input = scanner.nextLine();
            if (input.equals("q")
                    || input.equals("quit")
                    || input.equals("exit"))
                break;

            new ValidityCheck(input);
        }
        scanner.close();
    }
}
