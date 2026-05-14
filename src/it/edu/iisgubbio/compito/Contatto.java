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
		return nomeContatto.toLowerCase().contains(ricercaScritta) || cognomeContatto.toLowerCase().contains(ricercaScritta);
	}

	String testoCsv() {
		return "CONTATTO;" + nomeContatto + ";" + cognomeContatto + ";" + numeroTelefono + ";";
	}

	@Override
	public String toString() {
		return nomeContatto + " " + cognomeContatto + " - " + numeroTelefono;
	}
}
