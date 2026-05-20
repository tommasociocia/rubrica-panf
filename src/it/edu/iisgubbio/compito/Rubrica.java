package it.edu.iisgubbio.compito;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Rubrica extends Application {
	GridPane griglia = new GridPane();
	Label etichettaRicerca = new Label("ricerca");
	Label etichettaTipo = new Label("tipo");
	Label etichettaNome = new Label("nome");
	Label etichettaCognome = new Label("cognome");
	Label etichettaTelefono = new Label("telefono");
	Label etichettaExtra = new Label("email");
	Label etichettaErrore = new Label();
	TextField testoRicerca = new TextField();
	TextField testoNome = new TextField();
	TextField testoCognome = new TextField();
	TextField testoTelefono = new TextField();
	TextField testoExtra = new TextField();
	ComboBox<String> sceltaTipo = new ComboBox<String>();
	Button pulsanteAggiungi = new Button("aggiungi");
	Button pulsanteElimina = new Button("elimina");
	Button pulsanteOrdina = new Button("ordina");
	ListView<Contatto> listaContatti = new ListView<Contatto>();
	ArrayList<Contatto> archivioContatti = new ArrayList<Contatto>();
	File fileRubrica = new File("rubrica.csv");
	int posizioneModifica = -1;

	@Override
	public void start(Stage primaryStage) throws Exception {
		griglia.setPadding(new Insets(10, 10, 10, 10));
		griglia.setHgap(10);
		griglia.setVgap(10);
		sceltaTipo.getItems().add("PERSONALE");
		sceltaTipo.getItems().add("LAVORO");
		sceltaTipo.setValue("PERSONALE");
		testoRicerca.setPromptText("cerca nome o cognome");
		listaContatti.setPrefWidth(560);
		listaContatti.setPrefHeight(260);
		griglia.add(etichettaRicerca, 0, 0);
		griglia.add(testoRicerca, 1, 0);
		griglia.add(listaContatti, 0, 1, 4, 1);
		griglia.add(pulsanteElimina, 0, 2);
		griglia.add(pulsanteOrdina, 1, 2);
		griglia.add(etichettaTipo, 0, 3);
		griglia.add(sceltaTipo, 1, 3);
		griglia.add(etichettaNome, 0, 4);
		griglia.add(testoNome, 1, 4);
		griglia.add(etichettaCognome, 0, 5);
		griglia.add(testoCognome, 1, 5);
		griglia.add(etichettaTelefono, 0, 6);
		griglia.add(testoTelefono, 1, 6);
		griglia.add(etichettaExtra, 0, 7);
		griglia.add(testoExtra, 1, 7);
		griglia.add(pulsanteAggiungi, 1, 8);
		griglia.add(etichettaErrore, 0, 9, 4, 1);
		Scene scenaRubrica = new Scene(griglia, 700, 560);
		scenaRubrica.getStylesheets().add("style.css");
		primaryStage.setTitle("Rubrica telefonica");
		primaryStage.setScene(scenaRubrica);
		primaryStage.show();
		
		// Carica i contatti salvati nel file CSV e li mostra nella lista.
		caricaContatti();
		aggiornaLista();
		// Quando cambi tipo, cambia anche l'etichetta tra email e azienda.
		sceltaTipo.setOnAction(e -> cambiaTipo());
		// Quando premi aggiungi, salva un nuovo contatto oppure modifica quello scelto.
		pulsanteAggiungi.setOnAction(e -> aggiungiContatto());
		// Quando premi elimina, cancella il contatto selezionato.
		pulsanteElimina.setOnAction(e -> eliminaContatto());
		// Quando premi ordina, mette i contatti in ordine di cognome.
		pulsanteOrdina.setOnAction(e -> ordinaContatti());
		// Ogni volta che scrivi nella casella di ricerca, aggiorna la lista dei contatti.
		testoRicerca.setOnKeyReleased(e -> aggiornaLista());
		// Se fai doppio click su un contatto, apre la modifica di quel contatto.
		listaContatti.setOnMouseClicked(evento -> {
			if (evento.getClickCount() == 2) {
				modificaContatto();
			}
		});
	}

	void cambiaTipo() {
		// Se il tipo scelto e PERSONALE, il campo extra serve per l'email.
		if (sceltaTipo.getValue().equals("PERSONALE")) {
			etichettaExtra.setText("email");
		} else {
			// Altrimenti il campo extra serve per l'azienda.
			etichettaExtra.setText("azienda");
		}
	}

	void aggiungiContatto() {
		// Legge i dati scritti nei campi di testo.
		String nomeNuovo = testoNome.getText();
		String cognomeNuovo = testoCognome.getText();
		String telefonoNuovo = testoTelefono.getText();
		String extraNuovo = testoExtra.getText();
		Contatto contattoNuovo;

		// Controlla che i campi principali siano stati compilati.
		if (nomeNuovo.length() == 0 || cognomeNuovo.length() == 0 || telefonoNuovo.length() == 0) {
			etichettaErrore.setText("Completa nome, cognome e telefono");
			return;
		}
		// Crea un contatto personale oppure di lavoro in base al tipo scelto.
		if (sceltaTipo.getValue().equals("PERSONALE")) {
			contattoNuovo = new ContattoPersonale(nomeNuovo, cognomeNuovo, telefonoNuovo, extraNuovo);
		} else {
			contattoNuovo = new ContattoLavoro(nomeNuovo, cognomeNuovo, telefonoNuovo, extraNuovo);
		}
		// Se il pulsante e in modalita modifica, sostituisce il contatto selezionato.
		if (pulsanteAggiungi.getText().equals("modifica") && posizioneModifica != -1) {
			// Mette il contatto nuovo al posto di quello vecchio.
			archivioContatti.set(posizioneModifica, contattoNuovo);
			// Riscrive tutto il file CSV con i dati aggiornati.
			salvaRubrica();
			posizioneModifica = -1;
			pulsanteAggiungi.setText("aggiungi");
			etichettaErrore.setText("Contatto modificato");
		} else {
			// Se non sta modificando, aggiunge il nuovo contatto all'archivio e al file.
			archivioContatti.add(contattoNuovo);
			salvaContatto(contattoNuovo);
			etichettaErrore.setText("Contatto salvato");
		}
		// Svuota i campi dopo aver aggiunto o modificato.
		testoNome.setText("");
		testoCognome.setText("");
		testoTelefono.setText("");
		testoExtra.setText("");
		// Aggiorna la lista visibile.
		aggiornaLista();
	}

	// Elimina dalla lista e dal file il contatto selezionato.
	void eliminaContatto() {
		// Prende il contatto selezionato nella lista.
		Contatto contattoSelezionato = listaContatti.getSelectionModel().getSelectedItem();
		int posizioneElimina = -1;
		if (contattoSelezionato == null) {
			etichettaErrore.setText("Seleziona un contatto");
			return;
		}
		// Cerca con un for la posizione del contatto da eliminare.
		for (int i = 0; i < archivioContatti.size(); i++) {
			if (archivioContatti.get(i) == contattoSelezionato) {
				posizioneElimina = i;
			}
		}
		// Toglie il contatto dall'ArrayList usando la posizione trovata.
		if (posizioneElimina != -1) {
			archivioContatti.remove(posizioneElimina);
		}
		salvaRubrica();
		posizioneModifica = -1;
		pulsanteAggiungi.setText("aggiungi");
		etichettaErrore.setText("Contatto eliminato");
		// Aggiorna la lista visibile dopo l'eliminazione.
		aggiornaLista();
	}

	// Copia nei campi di testo i dati del contatto scelto, cosi possono essere modificati.
	void modificaContatto() {
		// Prende il contatto scelto dalla lista.
		Contatto contattoScelto = listaContatti.getSelectionModel().getSelectedItem();
		posizioneModifica = -1;
		if (contattoScelto == null) {
			etichettaErrore.setText("Seleziona un contatto");
			return;
		}
		// Cerca con un for la posizione del contatto da modificare.
		for (int i = 0; i < archivioContatti.size(); i++) {
			if (archivioContatti.get(i) == contattoScelto) {
				posizioneModifica = i;
			}
		}
		// Trasforma il contatto in testo CSV e divide i dati con il punto e virgola.
		String testoContatto = contattoScelto.testoCsv();
		String partiContatto[] = testoContatto.split(";");
		// Copia i dati del contatto nei TextField.
		sceltaTipo.setValue(partiContatto[0]);
		testoNome.setText(partiContatto[1]);
		testoCognome.setText(partiContatto[2]);
		testoTelefono.setText(partiContatto[3]);
		testoExtra.setText(partiContatto[4]);
		cambiaTipo();
		// Cambia il testo del pulsante, cosi aggiungiContatto capisce che deve modificare.
		pulsanteAggiungi.setText("modifica");
		etichettaErrore.setText("Modifica il contatto");
	}

	// Ordina i contatti in base al cognome e poi salva la rubrica aggiornata.
	void ordinaContatti() {
		// Confronta ogni contatto con quelli dopo di lui.
		for (int primoIndice = 0; primoIndice < archivioContatti.size(); primoIndice++) {
			for (int secondoIndice = primoIndice + 1; secondoIndice < archivioContatti.size(); secondoIndice++) {
				Contatto primoContatto = archivioContatti.get(primoIndice);
				Contatto secondoContatto = archivioContatti.get(secondoIndice);
				// Se il primo cognome viene dopo il secondo, li scambia.
				if (primoContatto.cognomeContatto.toLowerCase().compareTo(secondoContatto.cognomeContatto.toLowerCase()) > 0) {
					archivioContatti.set(primoIndice, secondoContatto);
					archivioContatti.set(secondoIndice, primoContatto);
				}
			}
		}
		// Salva e aggiorna la lista ordinata.
		salvaRubrica();
		aggiornaLista();
		etichettaErrore.setText("Lista ordinata");
	}

	// Aggiorna la ListView mostrando solo i contatti che corrispondono alla ricerca.
	void aggiornaLista() {
		// Legge quello che e scritto nella casella ricerca.
		String ricercaScritta = testoRicerca.getText().toLowerCase();
		// Svuota la lista visibile prima di riempirla di nuovo.
		listaContatti.getItems().clear();
		for (Contatto contattoLetto : archivioContatti) {
			// Aggiunge solo i contatti che contengono il testo cercato.
			if (contattoLetto.contiene(ricercaScritta)) {
				listaContatti.getItems().add(contattoLetto);
			}
		}
	}

	// Legge i contatti dal file CSV e li mette nell'archivio.
	void caricaContatti() {
		// Se il file non esiste, non deve caricare niente.
		if (fileRubrica.exists() == false) {
			return;
		}
		try (
				FileReader lettoreFile = new FileReader(fileRubrica);
				BufferedReader lettoreRighe = new BufferedReader(lettoreFile);
				) {
			String rigaLetta;
			// Legge una riga alla volta dal file CSV.
			while ((rigaLetta = lettoreRighe.readLine()) != null) {
				Contatto contattoLetto = creaContatto(rigaLetta);
				if (contattoLetto != null) {
					// Aggiunge il contatto letto all'archivio.
					archivioContatti.add(contattoLetto);
				}
			}
		} catch (IOException errore) {
			etichettaErrore.setText("Errore lettura CSV");
		}
	}

	Contatto creaContatto(String rigaLetta) {
		String partiRiga[] = rigaLetta.split(";");
		// Formato CSV: TIPO;nome;cognome;telefono;email oppure TIPO;nome;cognome;telefono;azienda
		if (partiRiga.length < 5) {
			return null;
		}
		if (partiRiga[0].equals("PERSONALE")) {
			return new ContattoPersonale(partiRiga[1], partiRiga[2], partiRiga[3], partiRiga[4]);
		}
		if (partiRiga[0].equals("LAVORO")) {
			return new ContattoLavoro(partiRiga[1], partiRiga[2], partiRiga[3], partiRiga[4]);
		}
		return null;
	}

	// Aggiunge un solo contatto alla fine del file CSV.
	void salvaContatto(Contatto contattoNuovo) {
		try {
			// Apre il file in modalita append, quindi scrive in fondo.
			FileWriter scrittoreFile = new FileWriter(fileRubrica, true);
			// Scrive il contatto in formato CSV e va a capo.
			scrittoreFile.write(contattoNuovo.testoCsv() + "\n");
			scrittoreFile.close();
		} catch (IOException errore) {
			etichettaErrore.setText("Errore salvataggio CSV");
		}
	}

	// Riscrive tutto il file CSV con tutti i contatti presenti nell'archivio.
	void salvaRubrica() {
		try {
			// Apre il file senza append, quindi cancella il contenuto vecchio.
			FileWriter scrittoreFile = new FileWriter(fileRubrica);
			for (Contatto contattoLetto : archivioContatti) {
				// Scrive ogni contatto dell'archivio nel file.
				scrittoreFile.write(contattoLetto.testoCsv() + "\n");
			}
			scrittoreFile.close();
		} catch (IOException errore) {
			etichettaErrore.setText("Errore salvataggio CSV");
		}
	}

	public static void main(String[] argomenti) {
		launch(argomenti);
	}
}
