## Filstruktur
```bash
.
├── README.md
├── start.sh                               # bash script för kompilering och exekvering
└── src
    ├── main
    │   ├── Exceptions                     # Diverse exceptions
    │   │   ├── LuhnException.java
    │   │   ├── ParsingException.java
    │   │   └── ValidationException.java 
    │   ├── Helper
    │   │   ├── OrganizationMapper.java    # Hjälper mappa typ av juridisk organisation
    │   │   ├── Credential.java            # Abstrakta klassen för alla "nummer"
    │   │   └── CredentialFactory.java     # Sköter byggning av rätt "nummer"-implementation
    │   ├── CoordinationNumber.java        # Samordningsnummer
    │   ├── OrganizationNumber.java        # Organisationsnummer
    │   ├── SocialSecurityNumber.java      # Personnummer
    │   └── ValidityCheck.java             # huvudklass (main, valideringscheck av luhn, mm)
    └── test
```

## Exekvering (från CLI)
> Ska även funka fint genom Intellij :)

Kör startscriptet från root dir genom 
```
sh start.sh
```

Alternativt:
```
$ chmod +x start.sh

$ ./start.sh
```

## Överskådlig struktur över programmet
<img src="programkarta.png" width="800px">

## Mina egna notes från problemuppställning

ValidityCheck
- logga kontroller som görs
- input är string

Personnummer:
- 6 siffror
  - ÅÅMMDD
- 3 siffror födelsenummer
- 1 kontrollsiffra (luhns algoritm)
- ålder > 100 år
  - + mellan ÅÅDDMM och födelsenummer
  eller
  - århundrade (18, 19, 20) före födelsedatum

ex. 10-siffrigt PN utan avdelar = person yngre än 100

Samordningsnummer:
- samma regler som PN, men 60 läggs till datumet
  - dag + 60 = [61, 91]

Organisationsnummer:
- första siffran är vilken juridisk form organisationen har
- mittersta siffran måste vara minst 20
- sista siffran är kontrollsiffra
  - samma som för PN
- kan inledas med "århundrade" 16 om det anges på 12-siffrig form



LÄNGD
- PER: [10, 13]
- SAM: [10, 13]
- ORG: [11, 13]