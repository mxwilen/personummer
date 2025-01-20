package src.main.Helper;

import java.util.Map;

public class OrganizationMapper {

    public enum OrganizationType {
        AKTIEBOLAG, // Aktiebolag, filialer, banker, försäkringsbolag, europabolag
        HANDELSBOLAG, // Handelsbolag och kommanditbolag
        BOSTADSRATTSFORENING, // Bostadsrättsföreningar, ekonomiska föreningar, etc.
        EKONOMISKFÖRENING, // Bostadsrättsföreningar, ekonomiska föreningar, etc.
        TROSSAMFUND, // Trossamfund
        // STATLIGA_MYNDIGHETER, // Statliga myndigheter
        UTLANDSKA_FORETAG, // Utländska företag
        UNKNOWN // Default if no match
    }

    private static final Map<String, OrganizationType> keyMap = Map.of(
            "5", OrganizationType.AKTIEBOLAG,
            "9", OrganizationType.HANDELSBOLAG,
            "7", OrganizationType.BOSTADSRATTSFORENING,
            "8", OrganizationType.BOSTADSRATTSFORENING,
            "2", OrganizationType.TROSSAMFUND,
            // "20", OrganizationType.STATLIGA_MYNDIGHETER,
            "3", OrganizationType.UTLANDSKA_FORETAG);

    public static OrganizationType getOrganizationType(String input) {
        return keyMap.getOrDefault(input, OrganizationType.UNKNOWN);
    }
}