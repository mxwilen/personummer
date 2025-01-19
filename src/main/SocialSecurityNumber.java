package src.main;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;

public class SocialSecurityNumber implements Credential {
    String credentialID;
    char checkSum;
    String birthNumber;
    String parsedDateString;
    int age;

    public SocialSecurityNumber(String credentialID, char checkSum, String birthNumber, String dateString) {
        this.credentialID = credentialID;
        this.checkSum = checkSum;
        this.birthNumber = birthNumber;
        this.parsedDateString = parseDateString(dateString);
    }

    @Override
    public String getCredentialID() {
        return this.credentialID;
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
        return this.parsedDateString.substring(2, this.parsedDateString.length());
    }

    @Override
    public void outputResult() {
        System.out.printf(
                """
                        VALID       Credential ID         : %s
                         PER        Birth Number          : %s
                                    Checksum              : %s
                                    Birth Date (YYMMDD)   : %s
                                    Age                   : %d
                        -------------------------------------------------
                                """,
                this.credentialID,
                this.birthNumber,
                this.checkSum,
                this.getMinimalDate(),
                this.age);
    }

    private void setAge(int age) {
        this.age = age;
    }

    private String parseDateString(String unparsedDateString) {
        DateTimeFormatter yyyyMMddFormatter = new DateTimeFormatterBuilder()
                .appendPattern("uuuuMMdd")
                .toFormatter()
                .withResolverStyle(ResolverStyle.STRICT);

        DateTimeFormatter yyMMddFormatter = new DateTimeFormatterBuilder()
                .appendPattern("uuMMdd")
                .toFormatter()
                .withResolverStyle(ResolverStyle.STRICT);

        // LocalDate will throw exception if date is invalid
        if (unparsedDateString.length() == 8) { // yyyyMMdd format
            LocalDate date = LocalDate.parse(unparsedDateString, yyyyMMddFormatter);

            // Calculate the age using Period (between now and date)
            setAge(Period.between(date, LocalDate.now()).getYears());

            return date.format(yyyyMMddFormatter);
        } else if (unparsedDateString.length() == 6) { // yyMMdd format
            LocalDate date = LocalDate.parse(unparsedDateString, yyMMddFormatter);

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

            return date.format(yyyyMMddFormatter);
        } else {
            throw new ParsingException(
                    "PER - ParsingException: Problem with parsing date. Date string neither 6 or 8 digits long");
        }
    }
}
