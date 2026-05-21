package it.edu.iisgubbio.compito;

public class Contatto {
	String nomeContatto;
	String cognomeContatto;
	String numeroTelefono;

	public Contatto(String nomeNuovo, String cognomeNuovo, String telefonoNuovo) {
		nomeContatto = nomeNuovo;
		cognomeContatto = cognomeNuovo;
		numeroTelefono = telefonoNuovo;
	}

	boolean contiene(String ricercaScritta) {
		// Restituisce true se il testo cercato compare nel nome o nel cognome.
		return nomeContatto.toLowerCase().contains(ricercaScritta) || cognomeContatto.toLowerCase().contains(ricercaScritta);
	}

	String testoCsv() {
		// Versione base del CSV (le sottoclassi la sovrascrivono con PERSONALE/LAVORO).
		return "CONTATTO;" + nomeContatto + ";" + cognomeContatto + ";" + numeroTelefono + ";";
	}

	@Override
	public String toString() {
		// Testo mostrato nella ListView.
		return nomeContatto + " " + cognomeContatto + " - " + numeroTelefono;
	}
}
