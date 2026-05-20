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
	Button pulsanteModifica = new Button("modifica");
	Button pulsanteOrdina = new Button("ordina");
	ListView<Contatto> listaContatti = new ListView<Contatto>();
	ArrayList<Contatto> archivioContatti = new ArrayList<Contatto>();
	File fileRubrica = new File("rubrica.csv");

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
		griglia.add(pulsanteModifica, 1, 2);
		griglia.add(pulsanteOrdina, 2, 2);
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
		sceltaTipo.setOnAction(e -> cambiaTipo());
		pulsanteAggiungi.setOnAction(e -> aggiungiContatto());
		pulsanteElimina.setOnAction(e -> eliminaContatto());
		pulsanteModifica.setOnAction(e -> modificaContatto());
		pulsanteOrdina.setOnAction(e -> ordinaContatti());
		testoRicerca.setOnKeyReleased(e -> aggiornaLista());
		// Se fai doppio click su un contatto, apre la modifica di quel contatto.
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
		String nomeNuovo = testoNome.getText();
		String cognomeNuovo = testoCognome.getText();
		String telefonoNuovo = testoTelefono.getText();
		String extraNuovo = testoExtra.getText();
		Contatto contattoNuovo;
		Contatto contattoScelto = listaContatti.getSelectionModel().getSelectedItem();

		if (nomeNuovo.length() == 0 || cognomeNuovo.length() == 0 || telefonoNuovo.length() == 0) {
			etichettaErrore.setText("Completa nome, cognome e telefono");
			return;
		}
		if (sceltaTipo.getValue().equals("PERSONALE")) {
			contattoNuovo = new ContattoPersonale(nomeNuovo, cognomeNuovo, telefonoNuovo, extraNuovo);
		} else {
			contattoNuovo = new ContattoLavoro(nomeNuovo, cognomeNuovo, telefonoNuovo, extraNuovo);
		}
		if (pulsanteAggiungi.getText().equals("modifica") && contattoScelto != null) {
			int posizioneContatto = archivioContatti.indexOf(contattoScelto);
			archivioContatti.set(posizioneContatto, contattoNuovo);
			salvaRubrica();
			pulsanteAggiungi.setText("aggiungi");
			etichettaErrore.setText("Contatto modificato");
		} else {
			archivioContatti.add(contattoNuovo);
			salvaContatto(contattoNuovo);
			etichettaErrore.setText("Contatto salvato");
		}
		testoNome.clear();
		testoCognome.clear();
		testoTelefono.clear();
		testoExtra.clear();
		aggiornaLista();
	}

	void eliminaContatto() {
		Contatto contattoSelezionato = listaContatti.getSelectionModel().getSelectedItem();
		if (contattoSelezionato == null) {
			etichettaErrore.setText("Seleziona un contatto");
			return;
		}
		archivioContatti.remove(contattoSelezionato);
		salvaRubrica();
		pulsanteAggiungi.setText("aggiungi");
		etichettaErrore.setText("Contatto eliminato");
		aggiornaLista();
	}

	void modificaContatto() {
		Contatto contattoScelto = listaContatti.getSelectionModel().getSelectedItem();
		if (contattoScelto == null) {
			etichettaErrore.setText("Seleziona un contatto");
			return;
		}
		testoNome.setText(contattoScelto.nomeContatto);
		testoCognome.setText(contattoScelto.cognomeContatto);
		testoTelefono.setText(contattoScelto.numeroTelefono);
		if (contattoScelto instanceof ContattoPersonale) {
			ContattoPersonale contattoPersonale = (ContattoPersonale) contattoScelto;
			sceltaTipo.setValue("PERSONALE");
			testoExtra.setText(contattoPersonale.emailContatto);
		} else {
			ContattoLavoro contattoLavoro = (ContattoLavoro) contattoScelto;
			sceltaTipo.setValue("LAVORO");
			testoExtra.setText(contattoLavoro.aziendaContatto);
		}
		cambiaTipo();
		pulsanteAggiungi.setText("modifica");
		etichettaErrore.setText("Modifica il contatto");
	}

	void ordinaContatti() {
		for (int primoIndice = 0; primoIndice < archivioContatti.size(); primoIndice++) {
			for (int secondoIndice = primoIndice + 1; secondoIndice < archivioContatti.size(); secondoIndice++) {
				Contatto primoContatto = archivioContatti.get(primoIndice);
				Contatto secondoContatto = archivioContatti.get(secondoIndice);
				if (primoContatto.cognomeContatto.compareToIgnoreCase(secondoContatto.cognomeContatto) > 0) {
					archivioContatti.set(primoIndice, secondoContatto);
					archivioContatti.set(secondoIndice, primoContatto);
				}
			}
		}
		salvaRubrica();
		aggiornaLista();
		etichettaErrore.setText("Lista ordinata");
	}

	void aggiornaLista() {
		String ricercaScritta = testoRicerca.getText().toLowerCase();
		listaContatti.getItems().clear();
		for (Contatto contattoLetto : archivioContatti) {
			if (contattoLetto.contiene(ricercaScritta)) {
				listaContatti.getItems().add(contattoLetto);
			}
		}
	}

	void caricaContatti() {
		if (fileRubrica.exists() == false) {
			return;
		}
		try (
				FileReader lettoreFile = new FileReader(fileRubrica);
				BufferedReader lettoreRighe = new BufferedReader(lettoreFile);
				) {
			String rigaLetta;
			while ((rigaLetta = lettoreRighe.readLine()) != null) {
				Contatto contattoLetto = creaContatto(rigaLetta);
				if (contattoLetto != null) {
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

	void salvaContatto(Contatto contattoNuovo) {
		try {
			FileWriter scrittoreFile = new FileWriter(fileRubrica, true);
			scrittoreFile.write(contattoNuovo.testoCsv() + "\n");
			scrittoreFile.close();
		} catch (IOException errore) {
			etichettaErrore.setText("Errore salvataggio CSV");
		}
	}

	void salvaRubrica() {
		try {
			FileWriter scrittoreFile = new FileWriter(fileRubrica);
			for (Contatto contattoLetto : archivioContatti) {
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
