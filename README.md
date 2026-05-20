# Rubrica Telefonica

Questo progetto e una rubrica telefonica fatta in JavaFX.
Serve per salvare, caricare, cercare, aggiungere, eliminare, modificare e ordinare contatti.
I contatti vengono salvati nel file `rubrica.csv`, cosi rimangono anche quando il programma viene chiuso.

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
