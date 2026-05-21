package it.edu.iisgubbio.compito;

public class ContattoLavoro extends Contatto {
	String aziendaContatto;

	public ContattoLavoro(String nomeNuovo, String cognomeNuovo, String telefonoNuovo, String aziendaNuova) {
		super(nomeNuovo, cognomeNuovo, telefonoNuovo);
		aziendaContatto = aziendaNuova;
	}

	@Override
	String testoCsv() {
		// Salva nel CSV con tipo LAVORO.
		return "LAVORO;" + nomeContatto + ";" + cognomeContatto + ";" + numeroTelefono + ";" + aziendaContatto;
	}

	@Override
	public String toString() {
		// Testo mostrato nella lista per distinguere subito il tipo lavoro.
		return "Lavoro: " + nomeContatto + " " + cognomeContatto + " - " + numeroTelefono + " - " + aziendaContatto;
	}
}
