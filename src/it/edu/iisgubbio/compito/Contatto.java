package it.edu.iisgubbio.compito;

public class Contatto {
	String nome;
	String cognome;
	String numeroTelefono;

	public Contatto(String nomeNuovo, String cognomeNuovo, String telefonoNuovo) {
		nome = nomeNuovo;
		cognome = cognomeNuovo;
		numeroTelefono = telefonoNuovo;
	}

	boolean contiene(String ricercaScritta) {
		return nome.toLowerCase().contains(ricercaScritta) || cognome.toLowerCase().contains(ricercaScritta);
	}

	String toCsv() {
		return "CONTATTO;" + nome + ";" + cognome + ";" + numeroTelefono + ";";
	}

	@Override
	public String toString() {
		return nome + " " + cognome + " - " + numeroTelefono;
	}
}
