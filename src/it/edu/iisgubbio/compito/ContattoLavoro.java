package it.edu.iisgubbio.compito;

public class ContattoLavoro extends Contatto {
	String aziendaContatto;

	public ContattoLavoro(String nomeNuovo, String cognomeNuovo, String telefonoNuovo, String aziendaNuova) {
		super(nomeNuovo, cognomeNuovo, telefonoNuovo);
		aziendaContatto = aziendaNuova;
	}

	@Override
	String testoCsv() {
		return "LAVORO;" + nomeContatto + ";" + cognomeContatto + ";" + numeroTelefono + ";" + aziendaContatto;
	}

	@Override
	public String toString() {
		return "Lavoro: " + nomeContatto + " " + cognomeContatto + " - " + numeroTelefono + " - " + aziendaContatto;
	}
}
