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
        // Only returns 6 digits
        return this.dateString.length() == 6 ? this.dateString : this.dateString.substring(2);
    }

    @Override
    public void outputResult() {
        System.out.printf(
                """
                        VALID       Credential ID         : %s
                         ORG        Birth Number          : %s
                                    Checksum              : %s
                                    Organization Type     : %s
                        -------------------------------------------------
                                """,
                this.credentialID,
                this.birthNumber,
                this.checkSum,
                this.orgType.toString());
    }

    /**
     * Takes the unparsed datestring and tries to match first digits to a
     * organization type.
     * - Organization mapper is now defaulting to a 'UNKNOWN' type, so the exception
     * is kind of useless.
     * 
     * @param unparsedDateString
     * @return OrganizationType
     */
    private OrganizationType parseOrgType(String dateString) {
        String firstDigit;
        if (dateString.length() == 8) { // yyyyMMdd format
            firstDigit = dateString.substring(2, 3);
            this.orgType = OrganizationMapper.getOrganizationType(firstDigit);
        } else if (dateString.length() == 6) { // yyMMdd format
            firstDigit = dateString.substring(0, 1);
            this.orgType = OrganizationMapper.getOrganizationType(firstDigit);
        } else {
            throw new ParsingException(
                    "Problem with parsing organization type");
        }

        return this.orgType;
    }
}
