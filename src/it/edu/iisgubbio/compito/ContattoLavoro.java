package it.edu.iisgubbio.compito;

public class ContattoLavoro extends Contatto {
	String azienda;

	public ContattoLavoro(String nomeNuovo, String cognomeNuovo, String telefonoNuovo, String aziendaNuova) {
		super(nomeNuovo, cognomeNuovo, telefonoNuovo);
		azienda = aziendaNuova;
	}

	@Override
	String toCsv() {
		return "LAVORO;" + nome + ";" + cognome + ";" + numeroTelefono + ";" + azienda;
	}

	@Override
	public String toString() {
		return "Lavoro: " + nome + " " + cognome + " - " + numeroTelefono + " - " + azienda;
	}
}
