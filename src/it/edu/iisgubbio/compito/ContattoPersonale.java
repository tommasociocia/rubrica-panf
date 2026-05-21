package it.edu.iisgubbio.compito;

public class ContattoPersonale extends Contatto {
	String emailContatto;

	public ContattoPersonale(String nomeNuovo, String cognomeNuovo, String telefonoNuovo, String emailNuova) {
		super(nomeNuovo, cognomeNuovo, telefonoNuovo);
		emailContatto = emailNuova;
	}

	@Override
	String testoCsv() {
		// Salva nel CSV con tipo PERSONALE.
		return "PERSONALE;" + nomeContatto + ";" + cognomeContatto + ";" + numeroTelefono + ";" + emailContatto;
	}

	@Override
	public String toString() {
		// Testo mostrato nella lista per distinguere subito il tipo personale.
		return "Personale: " + nomeContatto + " " + cognomeContatto + " - " + numeroTelefono + " - " + emailContatto;
	}
}
