package src.main;

import src.main.CredentialNumber.CredType;

public interface Credential {
    String getCredentialID();

    char getCheckSum();

    String getBirthNumber();

    String getMinimalDate();

    void outputResult();

}
