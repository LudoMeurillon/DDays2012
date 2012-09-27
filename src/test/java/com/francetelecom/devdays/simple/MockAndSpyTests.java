package com.francetelecom.devdays.simple;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class MockAndSpyTests {

	/**
	 * Par défaut les mock ne lèvent pas d'exceptions s'il sont appelés
	 * 
	 * Ils retournent "null" par défaut mais celle valeur peut être changée
	 * 
	 */
	@Test
	public void testMock() throws Exception {
		Eleve eleve = Mockito.mock(Eleve.class);

		assertNull(eleve.prenom());
		
		when(eleve.prenom()).thenReturn("nimportequoi");
		
		assertEquals("nimportequoi",eleve.prenom());
	}

	@Test
	public void testMockWithDefaultSettings() throws Exception {
		Eleve eleve = Mockito.mock(Eleve.class,new Answer<String>() {
			@Override
			public String answer(InvocationOnMock invocation) throws Throwable {
				//On peut ici manipuler l'appel au mock pour retourner 
				//des valeurs différentes selon les cas
				return "chaineParDefaut";
			}
		});
		assertEquals("chaineParDefaut",eleve.prenom());
	}
	
	/**
	 * Les {@link Spy} permettent d'espionner une implémentation réelle d'objet
	 * qui se comporte normalement mais sur lequel on peut vérifier des appels de méthodes
	 * et les paramètres de ces appels.
	 */
	@Test
	public void testSpy() throws Exception {
		Enfant ducobu = new Enfant("encorvou", "ducobu");
		
		//Un Spy ne se crée que sur une instance
		Eleve eleve = Mockito.spy(ducobu);
		
		assertEquals("encorvou ducobu [présent]",eleve.toString());
		
		//On peut vérifier les intéractions en signalant 
		//ou non le nombre d'interactions prévues
		verify(eleve).prenom();
		verify(eleve, times(1)).nom(); //Si il y en a 2 => Exception!
		verify(eleve, atLeastOnce()).present(); // Si il y en a 2 aucun problème
		
		when(eleve.present()).thenReturn(false);

		assertEquals("encorvou ducobu [absent]",eleve.toString());
	}
}
