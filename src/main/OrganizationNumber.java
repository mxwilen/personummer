package src.main;

import src.main.Exceptions.ParsingException;
import src.main.Helper.Credential;
import src.main.Helper.OrganizationMapper;
import src.main.Helper.OrganizationMapper.OrganizationType;

public class OrganizationNumber implements Credential {
    private final String credentialID;
    private final char checkSum;
    private final String birthNumber;
    private final String dateString;
    private OrganizationType orgType;

    public OrganizationNumber(String credentialID, char checkSum, String birthNumber, String dateString) {
        this.credentialID = credentialID;
        this.checkSum = checkSum;
        this.birthNumber = birthNumber;
        this.dateString = dateString;
        this.orgType = parseOrgType(dateString);
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
