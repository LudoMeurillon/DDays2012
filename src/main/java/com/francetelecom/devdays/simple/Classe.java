package com.francetelecom.devdays.simple;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Classe {

	private Set<Eleve> eleves;
	private int capacite = 0;
	
	public Classe(int capacite, Eleve...eleves){
		this.eleves = new HashSet<Eleve>(Arrays.asList(eleves));
		this.capacite = capacite;
	}
	
	public void nouveauxEleves(Eleve...eleves){
		this.eleves.addAll(Arrays.asList(eleves));
	}
	
	public boolean resteDesPlaces(){
		return capacite() > effectifs();
	}
	
	
	public boolean surchargee(){
		return capacite() < effectifs();
	}
	
	public Integer capacite(){
		return this.capacite;
	}
	
	public Map<String, Boolean> faireAppel(){
		Map<String, Boolean> appel = new HashMap<String, Boolean>();
		for (Eleve eleve : eleves) {
			appel.put(eleve.prenom()+" "+eleve.nom(), eleve.present());
		}
		return appel;
	}
	
	public Set<Eleve> absents(){
		Set<Eleve> absents = new HashSet<Eleve>();
		for (Eleve eleve : eleves) {
			if(!eleve.present()){
				absents.add(eleve);
			}
		}
		return absents;
	}
	
	public boolean toutLeMondeEstLa(){
		return absents().isEmpty();
	}
	
	public Integer effectifs(){
		return eleves.size();
	}
}
