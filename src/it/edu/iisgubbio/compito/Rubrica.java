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
		listaContatti.setPrefWidth(440);
		listaContatti.setPrefHeight(390);
		griglia.add(etichettaRicerca, 0, 0);
		griglia.add(testoRicerca, 1, 0);
		griglia.add(listaContatti, 0, 1, 2, 6);
		griglia.add(pulsanteElimina, 0, 7);
		griglia.add(pulsanteOrdina, 1, 7);
		griglia.add(etichettaTipo, 3, 1);
		griglia.add(sceltaTipo, 4, 1);
		griglia.add(etichettaNome, 3, 2);
		griglia.add(testoNome, 4, 2);
		griglia.add(etichettaCognome, 3, 3);
		griglia.add(testoCognome, 4, 3);
		griglia.add(etichettaTelefono, 3, 4);
		griglia.add(testoTelefono, 4, 4);
		griglia.add(etichettaExtra, 3, 5);
		griglia.add(testoExtra, 4, 5);
		griglia.add(pulsanteAggiungi, 4, 6);
		griglia.add(etichettaErrore, 0, 8, 5, 1);
		Scene scenaRubrica = new Scene(griglia, 760, 600);
		scenaRubrica.getStylesheets().add("style.css");
		primaryStage.setTitle("Rubrica telefonica");
		primaryStage.setScene(scenaRubrica);
		primaryStage.show();
		
		caricaContatti();
		aggiornaLista();
		sceltaTipo.setOnAction(e -> cambiaTipo());
		pulsanteAggiungi.setOnAction(e -> aggiungiContatto());
		pulsanteElimina.setOnAction(e -> eliminaContatto());
		pulsanteOrdina.setOnAction(e -> ordinaContatti());
		testoRicerca.setOnKeyReleased(e -> aggiornaLista());
		listaContatti.setOnMouseClicked(evento -> {
			if (evento.getClickCount() == 2) {
				modificaContatto();
			}
		});
	}

	void cambiaTipo() {
		if (sceltaTipo.getValue().equals("PERSONALE")) {
			etichettaExtra.setText("email");
		} else {
			etichettaExtra.setText("azienda");
		}
	}

	void aggiungiContatto() {
		// Prende il testo scritto dall'utente nei campi.
		String nome = testoNome.getText();
		String cognome = testoCognome.getText();
		String telefono = testoTelefono.getText();
		String extra = testoExtra.getText();
		Contatto nuovo;
		// Se nome, cognome o telefono sono vuoti, mostra un errore e ferma il metodo.
		if (nome.length() == 0 || cognome.length() == 0 || telefono.length() == 0) {
			etichettaErrore.setText("Completa nome, cognome e telefono");
			return;
		}
		// Crea un contatto personale se nella ComboBox e scelto PERSONALE.
		if (sceltaTipo.getValue().equals("PERSONALE")) {
			nuovo = new ContattoPersonale(nome, cognome, telefono, extra);
		} else {
			// Altrimenti crea un contatto di lavoro.
			nuovo = new ContattoLavoro(nome, cognome, telefono, extra);
		}
		// Se il bottone dice "modifica", sostituisce il contatto vecchio con quello nuovo.
		if (pulsanteAggiungi.getText().equals("modifica") && posizioneModifica != -1) {
			archivioContatti.set(posizioneModifica, nuovo);
			salvaRubrica();
			posizioneModifica = -1;
			pulsanteAggiungi.setText("aggiungi");
			etichettaErrore.setText("Contatto modificato");
		} else {
			// Se non siamo in modifica, aggiunge un nuovo contatto alla rubrica.
			archivioContatti.add(nuovo);
			salvaContatto(nuovo);
			etichettaErrore.setText("Contatto salvato");
		}
		// Svuota i campi di testo dopo aver salvato.
		testoNome.setText("");
		testoCognome.setText("");
		testoTelefono.setText("");
		testoExtra.setText("");
		// Aggiorna la lista visibile.
		aggiornaLista();
	}

	void eliminaContatto() {
		// Prende il contatto selezionato nella lista.
		Contatto contattoSelezionato = listaContatti.getSelectionModel().getSelectedItem();
		int posizioneElimina = -1;
		// Se non e stato scelto nessun contatto, mostra un errore e ferma il metodo.
		if (contattoSelezionato == null) {
			etichettaErrore.setText("Seleziona un contatto");
			return;
		}
		// Cerca nell'ArrayList la posizione del contatto da eliminare.
		for (int i = 0; i < archivioContatti.size(); i++) {
			if (archivioContatti.get(i) == contattoSelezionato) {
				posizioneElimina = i;
			}
		}
		// Se il contatto e stato trovato, lo rimuove dall'ArrayList.
		if (posizioneElimina != -1) {
			archivioContatti.remove(posizioneElimina);
		}
		// Riscrive il file senza il contatto eliminato.
		salvaRubrica();
		posizioneModifica = -1;
		pulsanteAggiungi.setText("aggiungi");
		etichettaErrore.setText("Contatto eliminato");
		// Aggiorna la lista mostrata a video.
		aggiornaLista();
	}

	void modificaContatto() {
		// Prende il contatto selezionato nella lista.
		Contatto contattoScelto = listaContatti.getSelectionModel().getSelectedItem();
		posizioneModifica = -1;
		// Se non e stato scelto nessun contatto, mostra un errore e ferma il metodo.
		if (contattoScelto == null) {
			etichettaErrore.setText("Seleziona un contatto");
			return;
		}
		// Cerca la posizione del contatto selezionato dentro l'ArrayList.
		for (int i = 0; i < archivioContatti.size(); i++) {
			if (archivioContatti.get(i) == contattoScelto) {
				posizioneModifica = i;
			}
		}
		// Trasforma il contatto in una riga CSV.
		String testoContatto = contattoScelto.testoCsv();
		// Divide la riga CSV in parti usando il punto e virgola.
		String partiContatto[] = testoContatto.split(";");
		// Copia i dati del contatto nei campi di testo.
		sceltaTipo.setValue(partiContatto[0]);
		testoNome.setText(partiContatto[1]);
		testoCognome.setText(partiContatto[2]);
		testoTelefono.setText(partiContatto[3]);
		testoExtra.setText(partiContatto[4]);
		cambiaTipo();
		// Cambia il testo del bottone per far capire che ora si sta modificando.
		pulsanteAggiungi.setText("modifica");
		etichettaErrore.setText("Modifica il contatto");
	}

	void ordinaContatti() {
		// Primo ciclo: sceglie un contatto da confrontare.
		for (int primoIndice = 0; primoIndice < archivioContatti.size(); primoIndice++) {
			// Secondo ciclo: confronta il primo contatto con quelli successivi.
			for (int secondoIndice = primoIndice + 1; secondoIndice < archivioContatti.size(); secondoIndice++) {
				Contatto primoContatto = archivioContatti.get(primoIndice);
				Contatto secondoContatto = archivioContatti.get(secondoIndice);
				// Se il primo cognome viene dopo il secondo in ordine alfabetico, li scambia.
				if (primoContatto.cognomeContatto.toLowerCase().compareTo(secondoContatto.cognomeContatto.toLowerCase()) > 0) {
					archivioContatti.set(primoIndice, secondoContatto);
					archivioContatti.set(secondoIndice, primoContatto);
				}
			}
		}
		// Salva e mostra la lista ordinata.
		salvaRubrica();
		aggiornaLista();
		etichettaErrore.setText("Lista ordinata");
	}

	void aggiornaLista() {
		// Legge il testo cercato e lo trasforma in minuscolo.
		String ricerca = testoRicerca.getText().toLowerCase();
		// Svuota la lista grafica prima di riempirla di nuovo.
		listaContatti.getItems().clear();
		// Controlla tutti i contatti presenti nell'archivio.
		for (int i = 0; i < archivioContatti.size(); i++) {
			Contatto contattoLetto = archivioContatti.get(i);
			String nome = contattoLetto.nomeContatto.toLowerCase();
			String cognome = contattoLetto.cognomeContatto.toLowerCase();
			// Mostra il contatto solo se nome o cognome contengono il testo cercato.
			if (nome.contains(ricerca) || cognome.contains(ricerca)) {
				listaContatti.getItems().add(contattoLetto);
			}
		}
	}

	void caricaContatti() {
		// Se il file non esiste, non c'e niente da caricare.
		if (fileRubrica.exists() == false) {
			return;
		}
		try {
			// Apre il file rubrica.csv in lettura.
			FileReader lettoreFile = new FileReader(fileRubrica);
			BufferedReader lettoreRighe = new BufferedReader(lettoreFile);
			String rigaLetta;
			// Legge il file una riga alla volta finche non finisce.
			while ((rigaLetta = lettoreRighe.readLine()) != null) {
				// Divide la riga nei vari campi: tipo, nome, cognome, telefono, extra.
				String partiRiga[] = rigaLetta.split(";");
				// Controlla che la riga abbia almeno 5 campi.
				if (partiRiga.length >= 5) {
					// Se il tipo e PERSONALE, crea un ContattoPersonale.
					if (partiRiga[0].equals("PERSONALE")) {
						ContattoPersonale contattoPersonale = new ContattoPersonale(partiRiga[1], partiRiga[2], partiRiga[3], partiRiga[4]);
						archivioContatti.add(contattoPersonale);
					} else if (partiRiga[0].equals("LAVORO")) {
						// Se il tipo e LAVORO, crea un ContattoLavoro.
						ContattoLavoro contattoLavoro = new ContattoLavoro(partiRiga[1], partiRiga[2], partiRiga[3], partiRiga[4]);
						archivioContatti.add(contattoLavoro);
					}
				}
			}
			// Chiude i lettori del file.
			lettoreRighe.close();
			lettoreFile.close();
		} catch (IOException errore) {
			etichettaErrore.setText("Errore lettura CSV");
		}
	}

	void salvaContatto(Contatto contattoNuovo) {
		try {
			// Apre il file in modalita append, cioe aggiunge in fondo senza cancellare.
			FileWriter scrittoreFile = new FileWriter(fileRubrica, true);
			// Scrive il contatto in formato CSV e va a capo.
			scrittoreFile.write(contattoNuovo.testoCsv() + "\n");
			// Chiude il file dopo la scrittura.
			scrittoreFile.close();
		} catch (IOException errore) {
			etichettaErrore.setText("Errore salvataggio CSV");
		}
	}

	void salvaRubrica() {
		try {
			// Apre il file in scrittura normale, quindi riscrive tutto da capo.
			FileWriter scrittoreFile = new FileWriter(fileRubrica);
			// Scorre tutti i contatti presenti nell'ArrayList.
			for (int i = 0; i < archivioContatti.size(); i++) {
				Contatto contattoLetto = archivioContatti.get(i);
				// Scrive ogni contatto in formato CSV.
				scrittoreFile.write(contattoLetto.testoCsv() + "\n");
			}
			// Chiude il file dopo aver scritto tutti i contatti.
			scrittoreFile.close();
		} catch (IOException errore) {
			etichettaErrore.setText("Errore salvataggio CSV");
		}
	}

	public static void main(String[] argomenti) {
		launch(argomenti);
	}
}
