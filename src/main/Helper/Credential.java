package src.main.Helper;

/**
 * Interface used by the factory class and its implementatons
 */
public interface Credential {

    /**
     * Parsed checksum
     */
    char getCheckSum();

    /**
     * Parsed birthnumber with format BBB
     */
    String getBirthNumber();

    /**
     * Parsed datestring with format YYMMDD
     */
    String getMinimalDate();

    /**
     * Output result to system.out according to factory implementation's own
     * standards
     */
    void outputResult();

}
