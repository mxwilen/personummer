package src.main.Helper;

import src.main.CoordinationNumber;
import src.main.OrganizationNumber;
import src.main.SocialSecurityNumber;

public class CredentialFactory {
    public Credential generateCredential(String credentialID) {
        String dateString = credentialID.substring(0, credentialID.length() - 4);

        if (dateString.charAt(dateString.length() - 1) == '+') {
            dateString = dateString.substring(0, dateString.length() - 1);
        } else if (dateString.charAt(dateString.length() - 1) == '-') {
            dateString = dateString.substring(0, dateString.length() - 1);
        }

        char checkSum = parseCheckSum(credentialID);
        String birthNumber = parseBirthNumber(credentialID);

        // SAM
        int dag = Integer.parseInt(dateString.substring(dateString.length() - 2, dateString.length() - 1));
        if (dag > 3)
            return new CoordinationNumber(credentialID, checkSum, birthNumber, dateString);

        // ORG
        int mon = Integer.parseInt(dateString.substring(dateString.length() - 4, dateString.length() - 2));
        if (mon > 12)
            return new OrganizationNumber(credentialID, checkSum, birthNumber, dateString);

        return new SocialSecurityNumber(credentialID, checkSum, birthNumber, dateString);
    }

    private char parseCheckSum(String credentialID) {
        return credentialID.charAt(credentialID.length() - 1);
    }

    private String parseBirthNumber(String credentialID) {
        return credentialID.substring(credentialID.length() - 4, credentialID.length() - 1);
    }
}
