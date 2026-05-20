# Rubrica Telefonica

Questo progetto e una rubrica telefonica fatta in JavaFX.
Serve per salvare, caricare, cercare, aggiungere, eliminare, modificare e ordinare i contatti.
I contatti vengono salvati nel file `rubrica.csv`, cosi rimangono anche quando il programma viene chiuso.

## Come funziona

Quando il programma si apre, legge i contatti gia salvati nel file `rubrica.csv` e li mostra nella lista.

Per aggiungere un contatto bisogna compilare nome, cognome e telefono. Poi si sceglie il tipo di contatto:

- `PERSONALE`: il campo extra diventa `email`.
- `LAVORO`: il campo extra diventa `azienda`.

Dopo aver scritto i dati si preme il bottone `aggiungi`. Il contatto viene inserito nella lista e salvato nel file CSV.

Per cercare un contatto si scrive nella casella `ricerca`. La lista si aggiorna mentre si scrive e mostra solo i contatti che contengono quel nome o cognome.

Per eliminare un contatto bisogna selezionarlo dalla lista e premere il bottone `elimina`.

Per ordinare i contatti si preme il bottone `ordina`. I contatti vengono ordinati in base al cognome.

## Modifica dei contatti

Non c'e un bottone `modifica`.
Per modificare un contatto bisogna fare doppio click sul contatto nella lista.

Quando si fa doppio click, i dati del contatto vengono copiati nei campi di testo.
Il bottone `aggiungi` cambia testo e diventa `modifica`.
A questo punto si possono cambiare i dati e premere `modifica`.
Il programma sostituisce il vecchio contatto con quello nuovo e riscrive il file `rubrica.csv`.

## File CSV

Il file `rubrica.csv` contiene tutti i contatti salvati.
Ogni riga rappresenta un contatto e i dati sono separati con il punto e virgola.

Esempio contatto personale:

```text
PERSONALE;Mario;Rossi;123456789;mario@email.it
```

Esempio contatto di lavoro:

```text
LAVORO;Luca;Bianchi;987654321;Azienda
```

## Suddivisione del progetto

`Rubrica.java`
Classe principale del programma. Crea la finestra JavaFX con una griglia e gestisce ricerca, aggiunta, eliminazione, modifica, ordinamento e file CSV.

`Contatto.java`
Classe base dei contatti. Contiene nome, cognome e numero di telefono.

`ContattoPersonale.java`
Sottoclasse di `Contatto`. Rappresenta un contatto personale e aggiunge l'email.

`ContattoLavoro.java`
Sottoclasse di `Contatto`. Rappresenta un contatto di lavoro e aggiunge l'azienda.

`rubrica.csv`
File dove sono salvati i contatti. Ogni riga contiene un contatto separato con il punto e virgola.

`style.css`
File con alcune impostazioni grafiche semplici, come colore dello sfondo, bottoni, lista ed etichette.
