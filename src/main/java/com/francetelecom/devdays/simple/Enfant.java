package com.francetelecom.devdays.simple;

public class Enfant implements Eleve{
	
	private String prenom;
	private String nom;
	private boolean present;
	
	public Enfant(){}
	
	public Enfant(String prenom, String nom){
		this.prenom = prenom;
		this.nom = nom;
		this.present = true;
	}
	
	@Override
	public boolean present(){
		return this.present;
	}

	@Override
	public String prenom() {
		return prenom;
	}

	@Override
	public String nom() {
		return nom;
	}
	
	@Override
	public String toString() {
		return prenom()+" "+nom()+" "+(present()?"[présent]":"[absent]");
	}
}
