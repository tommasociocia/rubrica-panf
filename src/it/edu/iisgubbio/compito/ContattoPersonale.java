package it.edu.iisgubbio.compito;

public class ContattoPersonale extends Contatto {
	String email;

	public ContattoPersonale(String nomeNuovo, String cognomeNuovo, String telefonoNuovo, String emailNuova) {
		super(nomeNuovo, cognomeNuovo, telefonoNuovo);
		email = emailNuova;
	}

	@Override
	String toCsv() {
		return "PERSONALE;" + nome + ";" + cognome + ";" + numeroTelefono + ";" + email;
	}

	@Override
	public String toString() {
		return "Personale: " + nome + " " + cognome + " - " + numeroTelefono + " - " + email;
	}
}
