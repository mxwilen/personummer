package src.main.Helper;

import src.main.CoordinationNumber;
import src.main.OrganizationNumber;
import src.main.SocialSecurityNumber;

public class CredentialFactory {

    /**
     * @param credentialID Unparsed id number (multiple formats possible)
     * @return Credential with last 4 digits parsed
     */
    public static Credential generateCredential(String credentialID) {
        // New string without BBBC (YYMMDD-BBBC)
        String dateString = credentialID.substring(0, credentialID.length() - 4);

        // Removes '-' and '+' from dateString
        if (dateString.charAt(dateString.length() - 1) == '+') {
            dateString = dateString.substring(0, dateString.length() - 1);
        } else if (dateString.charAt(dateString.length() - 1) == '-') {
            dateString = dateString.substring(0, dateString.length() - 1);
        }

        char checkSum = parseCheckSum(credentialID);
        String birthNumber = parseBirthNumber(credentialID);

        // Coordination number. Checks whether 60 has been added to the birth day
        int day = Integer.parseInt(dateString.substring(dateString.length() - 2, dateString.length() - 1));
        if (day > 3)
            return new CoordinationNumber(credentialID, checkSum, birthNumber, dateString);

        // Organization number. Checks whether middle digit pair is at least 20
        int month = Integer.parseInt(dateString.substring(dateString.length() - 4, dateString.length() - 2));
        if (month > 12)
            return new OrganizationNumber(credentialID, checkSum, birthNumber, dateString);

        return new SocialSecurityNumber(credentialID, checkSum, birthNumber, dateString);
    }

    private static char parseCheckSum(String credentialID) {
        return credentialID.charAt(credentialID.length() - 1);
    }

    private static String parseBirthNumber(String credentialID) {
        return credentialID.substring(credentialID.length() - 4, credentialID.length() - 1);
    }
}
