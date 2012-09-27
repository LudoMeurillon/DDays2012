package com.francetelecom.devdays.simple;

public class PlusDePlaceException extends RuntimeException {

	private Classe classe;
	
	public PlusDePlaceException(Classe classe){
		this.classe = classe;
	}

	public Classe getClasse() {
		return classe;
	}
}
