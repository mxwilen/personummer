package src.main.Helper;

public interface Credential {
    String getCredentialID();

    char getCheckSum();

    String getBirthNumber();

    String getMinimalDate();

    void outputResult();

}
