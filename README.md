# PAO PROJECT

## Chosen theme - Delivery App

Delivery app in cli.

### Actions/Interrogations

- LOGIN
- REGISTER
- LOGOUT
- CHANGE_PASSWORD
- EXIT
- HELP
- TEST
- SEE_LIST_LOCALS
- SEE_LOCAL_INFO
- CREATE_COMMAND
- CANCEL_COMMAND
- SEE_MY_COMMANDS
- SEE_MY_ADDRESSES
- ADD_ADDRESS
- CHANGE_CURRENT_ADDRESS
- SEE_COMMANDS_AVAILABLE_FOR_DELIVERY
- ACCEPT_COMMAND
- DELIVER_COMMAND
- ADD_LOCAL
- UPDATE_LOCAL
- DELETE_LOCAL
- SEE_MY_LOCALS
- SEE_LIST_PRODUCTS_FOR_LOCAL
- ADD_PRODUCT_FOR_LOCAL
- SEE_COMMANDS_FOR_LOCAL
- SEE_ACTIVE_COMMANDS_FOR_LOCAL

### Objects

- Address
- Command
- Product
    - CommandProduct
- User
    - Customer
    - DeliveryPerson
    - LocalAdministrator
- Local

### Enums

- CommandStatus
- TransportType
- UserType
- UserCommand

## TODO

- [x] add models
- [x] use collections
- [x] implement services
- [x] implement actions
- [x] add inheritance example
- [x] add mockup data example
- [x] implement csv services to load and save data
- [x] implement audit service
- [x] add description for each action
- [x] create services for each model
- [x] move from csv services to jdbc services
- [x] crud for models
- [x] refactor

## Requirements

### Etapa I

1) Definirea sistemului Să se creeze o lista pe baza temei alese cu cel puțin 10 acțiuni/interogări care se pot face în
   cadrul sistemului și o lista cu cel puțin 8 tipuri de obiecte.
2) Implementare Sa se implementeze în limbajul Java o aplicație pe baza celor definite la primul punct. Aplicația va
   conține:
   • clase simple cu atribute private / protected și metode de acces • cel puțin 2 colecții diferite capabile să
   gestioneze obiectele definite anterior (eg: List, Set, Map, etc.) dintre care cel puțin una sa fie sortata – se vor
   folosi array-uri uni- /bidimensionale în cazul în care nu se parcurg colectiile pana la data checkpoint-ului. •
   utilizare moștenire pentru crearea de clase adiționale și utilizarea lor încadrul colecțiilor; • cel puțin o clasă
   serviciu care sa expună operațiile sistemului • o clasa Main din care sunt făcute apeluri către servicii

### Etapa II

1) Extindeți proiectul din prima etapa prin realizarea persistentei utilizând fișiere:
   • Se vor realiza fișiere de tip CSV pentru cel puțin 4 dintre clasele definite în prima etapa. Fiecare coloana din
   fișier este separata de virgula. Exemplu:nume,prenume,varsta • Se vor realiza servicii singleton generice pentru
   scrierea și citirea din fișiere; • La pornirea programului se vor încărca datele din fișiere utilizând serviciile
   create;
2) Realizarea unui serviciu de audit Se va realiza un serviciu care sa scrie într-un fișier de tip CSV de fiecare data
   când este executată una dintre acțiunile descrise în prima etapa. Structura fișierului: nume_actiune, timestamp

### Etapa III

1) Înlocuiți serviciile realizate în etapa a II-a cu servicii care sa asigure persistenta utilizând baza de date
   folosind JDBC.
2) Să se realizeze servicii care sa expună operații de tip create, read, update si delete pentru cel puțin 4 dintre
   clasele definite.

### Teme sugerate

1) catalog (student, materie, profesor)
2) biblioteca (sectiuni, carti, autori, cititori)
3) programare cabinet medical (client, medic, programare)
4) gestiune stocuri magazin (categorii, produse, distribuitori)
5) aplicatie bancara (conturi,extras de cont, tranzactii, carduri, servicii)
6) platfora e-learning(cursuri, utilizatori, cursanti, quizuri)
7) sistem licitatii (licitatii, bids, produse, utilizatori)
8) platforma food delivery(localuri, comenzi, soferi, useri)
9) platforma imprumuturi carti - tip bookster (companii afiliate, utilizatori, carti)
10) platforma e-ticketing (evenimente, locatii, clienti)