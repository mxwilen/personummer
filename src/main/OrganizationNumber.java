package src.main;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;

import src.main.Exceptions.ParsingException;
import src.main.Helper.Credential;
import src.main.Helper.OrganizationMapper;
import src.main.Helper.OrganizationMapper.OrganizationType;

public class OrganizationNumber implements Credential {
    String credentialID;
    char checkSum;
    String birthNumber;
    String dateString;
    OrganizationType orgType;

    public OrganizationNumber(String credentialID, char checkSum, String birthNumber, String dateString) {
        this.credentialID = credentialID;
        this.checkSum = checkSum;
        this.birthNumber = birthNumber;
        this.dateString = dateString;
        this.orgType = parseOrgType(dateString);
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
        return this.dateString.length() == 6 ? this.dateString : this.dateString.substring(2);
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
                this.dateString,
                this.orgType.toString());
    }

    public OrganizationType getOrgType() {
        return this.orgType;
    }

    private OrganizationType parseOrgType(String unparsedDateString) {
        String firstDigit;
        if (unparsedDateString.length() == 8) { // yyyyMMdd format
            firstDigit = unparsedDateString.substring(2, 3);
            this.orgType = OrganizationMapper.getOrganizationType(firstDigit);
        } else if (unparsedDateString.length() == 6) { // yyMMdd format
            firstDigit = unparsedDateString.substring(0, 1);
            this.orgType = OrganizationMapper.getOrganizationType(firstDigit);
        } else {
            throw new ParsingException(
                    "ORG - ParsingException: Problem with parsing organization type");
        }

        return this.orgType;
    }
}
