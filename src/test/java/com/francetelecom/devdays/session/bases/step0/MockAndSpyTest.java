package com.francetelecom.devdays.session.bases.step0;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import org.junit.Test;
import org.mockito.Spy;

import com.francetelecom.devdays.simple.Eleve;
import com.francetelecom.devdays.simple.Enfant;
public class MockAndSpyTest {

	/**
	 * Par défaut les mock ne lèvent pas d'exceptions s'il sont appelés
	 * 
	 * Ils retournent "null" par défaut mais celle valeur peut être changée
	 */
	@Test
	public void testMock() throws Exception {
		//Ojectif : creer un mock, montrer qu'il retourne null sur les cas par défaut
		
		//1 - créer un Mock de Eleve
		Eleve eleve = null;
		
		//2 - vérifier ce que renvoie Eleve.prenom
		assertNull(eleve.prenom());
		
		//3 - stubber Eleve.prenom
		
		//4 - véfier ce que renvoie maintenant Eleve.prenom
		assertEquals("prénom",eleve.prenom());
	}
	
	/**
	 * Par défaut les mock ne lèvent pas d'exceptions s'il sont appelés
	 * 
	 * Ils retournent "null" par défaut mais celle valeur peut être changée
	 */
	@Test
	public void testMockWithDefault() throws Exception {
		//Ojectif : creer un mock, montrer qu'on peut determiner les valeurs par defaut du Mock (sans when...)
		//Usage   : interface Eleve
		//1 - créer un Mock de Eleve avec un Returns("test");
		Eleve eleve = null;
		
		//2 - vérifier ce que renvoie Eleve.prenom
		assertEquals("defautPrénom",eleve.prenom());
	
		//2 - créer un nouveau mock avec ReturnElementsOF
		
		//4 - véfier ce que renvoie maintenant Eleve.prenom
		assertEquals("prénom1",eleve.prenom());
		assertEquals("prénom2",eleve.prenom());
		assertEquals("prénom3",eleve.prenom());
		assertEquals("prénom3",eleve.prenom());
		assertEquals("prénom3",eleve.prenom());
	}
	
	
	/**
	 * Les {@link Spy} permettent d'espionner une implémentation réelle d'objet
	 * qui se comporte normalement mais sur lequel on peut vérifier des appels de méthodes
	 * et les paramètres de ces appels.
	 */
	@Test
	public void testSpy() throws Exception {
		Enfant ducobu = new Enfant("encorvou", "ducobu");
		//1 -créer un Spy sur ducobub
		
		//2 - vérifier que ducobuSpy.toString() est correct et appel l'implémentation
		//il doit retourner "encorvou ducobu [présent]"
		assertEquals("encorvou ducobu [présent]",ducobu.toString());
		
		//3 - vérifier les interactions générées par l'appel à toString()
		//on peut utiliser des arguments au verify (times(1), atLeastOnce() etc...)

		//4 - stubber ducobu.present()
		
		//5 - valider de nouveau le toString()
		assertEquals("encorvou ducobu [absent]",ducobu.toString());
	}
}
