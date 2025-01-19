package src.main;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class CredentialNumber {
    enum CredType {
        PER,
        SAM,
        ORG
    }

    static class ParseDateResponse {
        boolean success;
        Exception exception;
        String fullDate;
        String minimalDate;
    }

    private final String credentialID;
    private final char checkSum;
    private final String birthNumber;
    private CredType credType;
    private final String dateString;
    private final ParseDateResponse parseDateResponse;
    private final String fullDate;
    private final String minimalDate;
    private Exception exception;

    public CredentialNumber(String credentialID) {
        this.credentialID = credentialID;
        this.checkSum = parseCheckSum(credentialID);
        this.birthNumber = parseBirthNumber(credentialID);
        this.dateString = parseDate(credentialID);

        this.parseDateResponse = getDate(this.dateString);
        if (this.parseDateResponse.success) {
            this.credType = CredType.PER;
            this.fullDate = this.parseDateResponse.fullDate;
            this.minimalDate = this.parseDateResponse.minimalDate;
        } else {
            this.fullDate = this.minimalDate = null;
            this.exception = this.parseDateResponse.exception;
        }
    }

    private char parseCheckSum(String credentialID) {
        return credentialID.charAt(credentialID.length() - 1);
    }

    private String parseBirthNumber(String credentialID) {
        return credentialID.substring(credentialID.length() - 4, credentialID.length() - 1);
    }

    private String parseDate(String credentialID) {
        String dateString = credentialID.substring(0, credentialID.length() - 4);

        if (dateString.charAt(dateString.length() - 1) == '+') {
            dateString = dateString.substring(0, dateString.length() - 1);
        } else if (dateString.charAt(dateString.length() - 1) == '-') {
            dateString = dateString.substring(0, dateString.length() - 1);
        } else {
            dateString = dateString.substring(0, dateString.length());
        }

        // SAM
        int dag = Integer.parseInt(dateString.substring(dateString.length() - 2, dateString.length() - 1));
        if (dag > 3)
            this.credType = CredType.SAM;

        // ORG
        int mon = Integer.parseInt(dateString.substring(dateString.length() - 4, dateString.length() - 2));
        if (mon > 12)
            this.credType = CredType.ORG;

        return dateString;

    }

    private ParseDateResponse getDate(String dateString) {
        DateTimeFormatter yyyyMMddFormatter = new DateTimeFormatterBuilder()
                .appendPattern("uuuuMMdd")
                .toFormatter()
                .withResolverStyle(ResolverStyle.STRICT);

        DateTimeFormatter yyMMddFormatter = new DateTimeFormatterBuilder()
                .appendPattern("uuMMdd")
                .toFormatter()
                .withResolverStyle(ResolverStyle.STRICT);

        ParseDateResponse parseDateResponse = new ParseDateResponse();

        try {
            if (dateString.length() == 8) { // yyyyMMdd format
                LocalDate date = LocalDate.parse(dateString, yyyyMMddFormatter);
                parseDateResponse.success = true;
                parseDateResponse.fullDate = date.format(yyyyMMddFormatter);
                parseDateResponse.minimalDate = date.format(yyMMddFormatter);
                return parseDateResponse;
            } else if (dateString.length() == 7) { // yyMMdd format
                LocalDate date = LocalDate.parse(dateString, yyMMddFormatter);

                // Adjust the year dynamically
                int year = date.getYear();
                if (year % 100 > LocalDate.now().getYear() % 100) {
                    year = 1900 + (year % 100); // Previous century
                } else {
                    year = 2000 + (year % 100); // Current century
                }

                date = LocalDate.of(year, date.getMonthValue(), date.getDayOfMonth());
                parseDateResponse.success = true;
                parseDateResponse.fullDate = date.format(yyyyMMddFormatter);
                parseDateResponse.minimalDate = date.format(yyMMddFormatter);
                return parseDateResponse;
            } else {
                throw new ParsingException("Problem with parsing the date.");
            }
        } catch (DateTimeParseException e) {
            // Log the exception and update the response
            parseDateResponse.success = false;
            parseDateResponse.exception = e;
            return parseDateResponse;
        } catch (ParsingException e) {
            // Handle custom exceptions
            parseDateResponse.success = false;
            parseDateResponse.exception = e;
            return parseDateResponse;
        }
    }

    public String getCredentialID() {
        return this.credentialID;
    }

    public char getCheckSum() {
        return this.checkSum;
    }

    public String getbirthNumber() {
        return this.birthNumber;
    }

    public CredType getCredType() {
        return this.credType;
    }

    public String getFullDate() {
        return this.fullDate;
    }

    public String getMinimalDate() {
        return this.minimalDate;
    }

    public Exception getException() {
        return this.exception;
    }
}
