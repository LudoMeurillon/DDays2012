package com.francetelecom.devdays.simple;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class ClasseTest {
	
	Eleve ducobu;
	
	@Before
	public void init(){
		ducobu = mock(Eleve.class,"mock ducobu");
		given(ducobu.prenom()).willReturn("monsieur");
		when(ducobu.nom()).thenReturn("ducobu");
		when(ducobu.present()).thenReturn(false);
	}
	
	@Test
	public void faireAppelSansAbsent() throws Exception {
		when(ducobu.present()).thenReturn(true);
		Classe classe = new Classe(30, ducobu);
		assertTrue(classe.toutLeMondeEstLa());
	}

	@Test
	public void faireAppelAvecUnAbsent() throws Exception {
		Classe classe = new Classe(30, ducobu);
		assertFalse(classe.toutLeMondeEstLa());
	}
}
