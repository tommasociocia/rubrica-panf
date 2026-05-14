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
	TextField testoRicerca = new TextField();
	TextField testoNome = new TextField();
	TextField testoCognome = new TextField();
	TextField testoTelefono = new TextField();
	TextField testoExtra = new TextField();
	ComboBox<String> sceltaTipo = new ComboBox<String>();
	Button pulsanteAggiungi = new Button("aggiungi");
	Label etichettaExtra = new Label("email");
	Label etichettaErrore = new Label();
	ListView<Contatto> listaContatti = new ListView<Contatto>();
	ArrayList<Contatto> archivioContatti = new ArrayList<Contatto>();
	File fileRubrica = new File("rubrica.csv");

	@Override
	public void start(Stage primaryStage) throws Exception {
		GridPane griglia = new GridPane();
		griglia.setPadding(new Insets(10, 10, 10, 10));
		griglia.setHgap(10);
		griglia.setVgap(10);

		sceltaTipo.getItems().add("PERSONALE");
		sceltaTipo.getItems().add("LAVORO");
		sceltaTipo.setValue("PERSONALE");
		testoRicerca.setPromptText("cerca nome o cognome");

		griglia.add(new Label("ricerca"), 0, 0);
		griglia.add(testoRicerca, 1, 0);
		griglia.add(listaContatti, 0, 1, 2, 1);
		griglia.add(new Label("tipo"), 0, 2);
		griglia.add(sceltaTipo, 1, 2);
		griglia.add(new Label("nome"), 0, 3);
		griglia.add(testoNome, 1, 3);
		griglia.add(new Label("cognome"), 0, 4);
		griglia.add(testoCognome, 1, 4);
		griglia.add(new Label("telefono"), 0, 5);
		griglia.add(testoTelefono, 1, 5);
		griglia.add(etichettaExtra, 0, 6);
		griglia.add(testoExtra, 1, 6);
		griglia.add(pulsanteAggiungi, 1, 7);
		griglia.add(etichettaErrore, 0, 8, 2, 1);

		Scene scenaRubrica = new Scene(griglia, 520, 430);
		scenaRubrica.getStylesheets().add("style.css");
		primaryStage.setTitle("Rubrica telefonica");
		primaryStage.setScene(scenaRubrica);
		primaryStage.show();
		caricaContatti();
		aggiornaLista();
		sceltaTipo.setOnAction(evento -> cambiaTipo());
		pulsanteAggiungi.setOnAction(evento -> aggiungiContatto());
		testoRicerca.setOnKeyReleased(evento -> aggiornaLista());
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

		if (nomeNuovo.length() == 0 || cognomeNuovo.length() == 0 || telefonoNuovo.length() == 0) {
			etichettaErrore.setText("Completa nome, cognome e telefono");
			return;
		}
		if (sceltaTipo.getValue().equals("PERSONALE")) {
			contattoNuovo = new ContattoPersonale(nomeNuovo, cognomeNuovo, telefonoNuovo, extraNuovo);
		} else {
			contattoNuovo = new ContattoLavoro(nomeNuovo, cognomeNuovo, telefonoNuovo, extraNuovo);
		}
		archivioContatti.add(contattoNuovo);
		salvaContatto(contattoNuovo);
		testoNome.clear();
		testoCognome.clear();
		testoTelefono.clear();
		testoExtra.clear();
		etichettaErrore.setText("Contatto salvato");
		aggiornaLista();
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

	public static void main(String[] argomenti) {
		launch(argomenti);
	}
}
