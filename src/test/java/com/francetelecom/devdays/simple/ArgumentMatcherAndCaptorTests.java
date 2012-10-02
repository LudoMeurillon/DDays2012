package com.francetelecom.devdays.simple;

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

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

@Ignore
public class ArgumentMatcherAndCaptorTests {

	@Test
	public void testArgumentMatcher() throws Exception {
		Classe classe = spy(new Classe(30));
		
		//On peut remarquer qu'il est possible de "stubber" des méthodes void
		//Si on utilise un matcher (any, eq, ...) on doit le faire pour tous les paramètres
		//d'une méthode
		doCallRealMethod().when(classe).nouvelEleve(eq("superprénom"), anyString());
		
		classe.nouvelEleve("superprénom", "supernom");
		classe.nouvelEleve("superprénom", "ducobu 1er");

		//On définit une nouvelle règle qui lève une exception si un Docubu est affecté dans la classe
		doThrow(new PlusDePlaceException(classe)).when(classe).nouvelEleve(anyString(),matches(".*ducobu.*"));
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
		ArgumentCaptor<Eleve> eleveCaptor = ArgumentCaptor.forClass(Eleve.class);
		
		//On vérifie les appels concernés en utilisant le captor
		verify(classe, times(2)).nouvelEleve(eleveCaptor.capture());
		
		//On peut ensuite vérifier le contenu des appels
		List<Eleve> elevesAjoutes = eleveCaptor.getAllValues();
		assertEquals(2,elevesAjoutes.size());
		//L'ordre des valeurs est conservés dans la liste en commencant pas le premier appel
		assertEquals("superprénom",elevesAjoutes.get(0).prenom());
		assertEquals("supernom",elevesAjoutes.get(0).nom());
		assertEquals("unautresuperprénom",elevesAjoutes.get(1).prenom());
		assertEquals("supernom",elevesAjoutes.get(1).nom());
	}
}
