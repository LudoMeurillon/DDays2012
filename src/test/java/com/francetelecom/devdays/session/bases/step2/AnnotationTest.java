package com.francetelecom.devdays.session.bases.step2;

import static junit.framework.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.francetelecom.devdays.simple.Classe;
import com.francetelecom.devdays.simple.Eleve;
import com.francetelecom.devdays.simple.Enfant;

public class AnnotationTest {
	//1 - faire un mock sur eleve
	Eleve eleve;
	
	//2 - faire un spy sur enfant
	//On peut faire un spy sans instance seulement s'il existe un constructeur sans paramètres
	Enfant enfant;
	
	//3 - faire un spy sur une instance déjà déclarée
	// Sinon on doit fournir une implémentation ici
	Classe classe = new Classe(30);
	
	//4 - générer un captor par annotation
	//Les captor peuvent être déclarés par annotations aussi
	ArgumentCaptor<Eleve> eleveCaptor;

	@Before
	public void init(){
		//On peut utiliser l'API mockito ici ou utiliser un Runner JUnit spécifique
	}
	
	@Test
	public void testAnnotations() throws Exception {
		assertNotNull("Le mock 'eleve' devrait être initialisé", eleve);
		assertNotNull("Le mock 'classe' devrait être initialisé", classe);
		assertNotNull("Le captor de valeurs Eleve devrait être initialisé", eleveCaptor);
	}
}
