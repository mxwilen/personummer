package src.main;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;

import src.main.Exceptions.ParsingException;
import src.main.Helper.Credential;

public class CoordinationNumber implements Credential {
    private final String credentialID;
    private final char checkSum;
    private final String birthNumber;
    private final String parsedDateString;
    private final String yyyymmdd;
    private int age;

    // Only used by isValidDate()
    public static class DateCheckerResponse {
        boolean isValid;
        String coordDate;
        String yyyymmdd;
    }

    public CoordinationNumber(String credentialID, char checkSum, String birthNumber, String dateString) {
        this.credentialID = credentialID;
        this.checkSum = checkSum;
        this.birthNumber = birthNumber;

        DateCheckerResponse response = isValidDate(dateString);
        if (!response.isValid)
            throw new ParsingException("Not a valid date");
        this.parsedDateString = response.coordDate;
        this.yyyymmdd = response.yyyymmdd;
    }

    @Override
    public char getCheckSum() {
        return this.checkSum;
    }

    @Override
    public String getBirthNumber() {
        return this.birthNumber;
    }

    @Override
    public String getMinimalDate() {
        return this.parsedDateString;
    }

    @Override
    public void outputResult() {
        System.out.printf(
                """
                        VALID       Credential ID         : %s
                         SAM        Birth Number          : %s
                                    Checksum              : %s
                                    Birth Date (YYYYMMDD) : %s
                                    Age                   : %d
                        -------------------------------------------------
                                """,
                this.credentialID,
                this.birthNumber,
                this.checkSum,
                this.yyyymmdd,
                this.age);
    }

    private void setAge(int age) {
        this.age = age;
    }

    /**
     * Takes the unparsed datestring and tries to match it to a valid date.
     * - If valid date, calulate age, correct century
     * - If not valid date, throw exception (catched in root)
     * 
     * @param unparsedDateString
     * @return DateCheckerResponse
     */
    private DateCheckerResponse isValidDate(String unparsedDateString) {
        DateTimeFormatter yyyyMMddFormatter = new DateTimeFormatterBuilder()
                .appendPattern("uuuuMMdd")
                .toFormatter()
                .withResolverStyle(ResolverStyle.STRICT);

        DateTimeFormatter yyMMddFormatter = new DateTimeFormatterBuilder()
                .appendPattern("uuMMdd")
                .toFormatter()
                .withResolverStyle(ResolverStyle.STRICT);

        DateCheckerResponse response = new DateCheckerResponse();

        // LocalDate will throw exception if date is invalid
        if (unparsedDateString.length() == 8) { // yyyyMMdd format

            LocalDate date = LocalDate.parse(convertToDate(unparsedDateString), yyyyMMddFormatter);

            // Calculate the age using Period (between now and date)
            setAge(Period.between(date, LocalDate.now()).getYears());

            response.coordDate = unparsedDateString.substring(2);
            response.yyyymmdd = date.format(yyyyMMddFormatter);
        } else if (unparsedDateString.length() == 6) { // yyMMdd format
            LocalDate date = LocalDate.parse(convertToDate(unparsedDateString), yyMMddFormatter);

            // Adjust the year dynamically
            int year = date.getYear();
            if (year % 100 > LocalDate.now().getYear() % 100) {
                year = 1900 + (year % 100); // Previous century
            } else {
                year = 2000 + (year % 100); // Current century
            }

            date = LocalDate.of(year, date.getMonthValue(), date.getDayOfMonth());

            // Calculate the age using Period (between now and date)
            setAge(Period.between(date, LocalDate.now()).getYears());

            response.coordDate = unparsedDateString;
            response.yyyymmdd = date.format(yyyyMMddFormatter);
        } else {
            response.isValid = false;
            throw new ParsingException(
                    "Problem with parsing date. Date string neither 6 or 8 digits long");
        }

        response.isValid = true;
        return response;
    }

    /**
     * Takes coordinationnumber's date string and subtracts 60 from the day number,
     * and returns the new (valid) date string
     * 
     * @param coordDateString
     * @return String
     */
    private String convertToDate(String coordDateString) {
        int day = Integer.parseInt(
                coordDateString.substring(coordDateString.length() - 2, coordDateString.length() - 1));
        day -= 6;

        // Convert the modified value back to a character
        char dayChar = Character.forDigit(day, 10);

        // Insert the modified character back into the string at the same index
        StringBuilder date = new StringBuilder(coordDateString);
        date.setCharAt(coordDateString.length() - 2, dayChar);

        return date.toString();
    }
}
