package it.edu.iisgubbio.compito;

public class ContattoPersonale extends Contatto {
	String emailContatto;

	public ContattoPersonale(String nomeNuovo, String cognomeNuovo, String telefonoNuovo, String emailNuova) {
		super(nomeNuovo, cognomeNuovo, telefonoNuovo);
		emailContatto = emailNuova;
	}

	@Override
	String testoCsv() {
		return "PERSONALE;" + nomeContatto + ";" + cognomeContatto + ";" + numeroTelefono + ";" + emailContatto;
	}

	@Override
	public String toString() {
		return "Personale: " + nomeContatto + " " + cognomeContatto + " - " + numeroTelefono + " - " + emailContatto;
	}
}
