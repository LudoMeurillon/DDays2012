package com.francetelecom.devdays.simple;

import static junit.framework.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * On peut utiliser un {@link Runner} pour lancer les tests est utiliser les annotations Mockito.
 * On peut aussi utiliser {@link MockitoAnnotations#initMocks(Object)} sur l'instance de classe de test
 * dans un bloc {@link Before} 
 */
@Ignore
@RunWith(MockitoJUnitRunner.class)
public class AnnotationsTests {

	@Mock Eleve eleve;
	
	//On peut faire un spy sans instance seulement s'il existe un constructeur sans paramètres
	@Spy Enfant enfant;
	// Sinon on doit fournir une implémentation ici
	@Spy Classe classe = new Classe(30);
	
	//Les captor peuvent être déclarés par annotations aussi
	@Captor ArgumentCaptor<Eleve> eleveCaptor;

	@Before
	public void init(){
		//MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testAnnotations() throws Exception {
		assertNotNull(eleve);
		assertNotNull(classe);
		assertNotNull(eleveCaptor);
	}
}
