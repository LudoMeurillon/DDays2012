package com.francetelecom.devdays;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.francetelecom.devdays.domain.Task;
import com.francetelecom.devdays.domain.TodoList;

/**
 * Test d'intégration vérifiant le comportement global de {@link TodoService}
 * 
 * @author Ludovic Meurillon
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/applicationContext.xml"})
public class TodoServiceTest {
	
	/*
	 * 1 - On peut éviter l'exception générée par le mail en déclarant un mock dans le contexte Spring
	 * 
	 * 2 - Une fois le mock crée on peut déjà l'utiliser dans le test s'il est en scope singleton
	 * 
	 * 3 - On peut injecter le mock depuis le Test en créant un Setter
	 * 
	 * 4 - On peut remplacer le context Spring et le(s) test(s) "intégré(s)" par des tests focalisés sur l'API du Service
	 */
	
	
	@Autowired
	private TodoService todoService;
	
	@Autowired
	private JavaMailSender javaMailSender;

	@Test
	public void testCreateAndReatrieveAFreshTodoList() throws Exception {
		TodoList newList = todoService.newList("masuperlistedetrucsafaire", "listowner@test.com");
		assertNotNull(newList);
		
		Task newTask = task("Faire les courses","faire les courses hebdo en prenant commande pour le drive", DATE_FORMAT.parse("12/10/2012 19:30"));
		
		todoService.addTask(newList.getName(),newTask);
		
		//Action
		TodoList list = todoService.getTodoList("masuperlistedetrucsafaire");

		assertNotNull(list);
		assertTrue(list.getTodos().contains(newTask));
	}

	
	private static final SimpleDateFormat DATE_FORMAT	= new SimpleDateFormat("dd/MM/yyyy HH:mm");
	
	private static final Task task(String name, String description, Date limit){
		Task task = new Task();
		task.setName(name);
		task.setDescription(description);
		task.setDeadline(limit);
		return task;
	}
}
