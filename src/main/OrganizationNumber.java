package src.main;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;

public class OrganizationNumber implements Credential {
    String credentialID;
    char checkSum;
    String birthNumber;
    String parsedDateString;

    private enum OrgType {
        TEST1,
        TEST2
    }

    public OrganizationNumber(String credentialID, char checkSum, String birthNumber, String dateString) {
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
                         ORG        Birth Number          : %s
                                    Checksum              : %s
                                    Birth Date (YYYYMMDD) : %s
                                    Organization Type     : %s
                        -------------------------------------------------
                                """,
                this.credentialID,
                this.birthNumber,
                this.checkSum,
                this.parsedDateString,
                parseOrgType().toString());
    }

    private OrgType parseOrgType() {
        return OrgType.TEST1;
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

        if (unparsedDateString.length() == 8) { // yyyyMMdd format
            LocalDate date = LocalDate.parse(unparsedDateString, yyyyMMddFormatter);
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
            return date.format(yyyyMMddFormatter);
        } else {
            throw new ParsingException(
                    "ORG - ParsingException: Problem with parsing date. Date string neither 6 or 8 digits long");
        }
    }
}
