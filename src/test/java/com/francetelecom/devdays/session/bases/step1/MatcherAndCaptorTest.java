package com.francetelecom.devdays.session.bases.step1;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.francetelecom.devdays.simple.Classe;
import com.francetelecom.devdays.simple.Eleve;
import com.francetelecom.devdays.simple.PlusDePlaceException;

public class MatcherAndCaptorTest {

	@Test
	public void testArgumentMatcher() throws Exception {
		Classe classe = spy(new Classe(30));
		
		//1 - faire en sorte que classe.nouvelEleve("superprénom", nimpoquelnom) fasse un appel normal
		//doCallRealMethod...
		
		classe.nouvelEleve("superprénom", "supernom");
		classe.nouvelEleve("superprénom", "ducobu 1er");
		
		//1 - faire en sorte que tous les .*ducobu.* ne soient pas acceptés dans la classe par faute de place PlusDePlaceException
		//doThrow...
		
		//En cas de conflit, les dernières règles énnoncées gagnent
		classe.nouvelEleve("superprénom", "supernom");
		try{
			classe.nouvelEleve("superprénom", "ducobu 1er");
			fail();
		}catch(PlusDePlaceException e){
			assertEquals(classe,e.getClasse());
		}
		try{
			classe.nouvelEleve("prénom","ducobu");
			fail();
		}catch(PlusDePlaceException e){
			assertEquals(classe,e.getClasse());
		}
	}
	
	@Test
	public void testArgumentCaptor() throws Exception {
		Classe classe = spy(new Classe(30));
		classe.nouvelEleve("superprénom", "supernom");
		classe.nouvelEleve("unautresuperprénom", "supernom");
		
		//La création du captor peut se faire à n'importe quel moment

		//1 - créer un ArgumentCaptor d'Eleve pour capture les valeurs générées.
		
		//On vérifie les appels concernés en utilisant le captor
		//2 - cabler une vérification utilisation le captor

		List<Eleve> elevesAjoutes = new ArrayList<Eleve>();//WTF?
		assertEquals(2,elevesAjoutes.size());
		
		assertEquals("superprénom",elevesAjoutes.get(0).prenom());
		assertEquals("supernom",elevesAjoutes.get(0).nom());
		assertEquals("unautresuperprénom",elevesAjoutes.get(1).prenom());
		assertEquals("supernom",elevesAjoutes.get(1).nom());
		//Remarque : L'ordre des valeurs est conservés dans la liste en commencant pas le premier appel
	}
}
