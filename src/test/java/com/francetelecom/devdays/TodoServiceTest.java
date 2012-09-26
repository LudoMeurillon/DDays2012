package com.francetelecom.devdays;

import static junit.framework.Assert.*;
import static org.mockito.Matchers.*;

import static org.mockito.Mockito.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.francetelecom.devdays.domain.Task;
import com.francetelecom.devdays.domain.TodoList;

/**
 * Test d'intégration vérifiant le comportement global de {@link TodoService}
 * 
 * @author Ludovic Meurillon
 */
@RunWith(MockitoJUnitRunner.class)
public class TodoServiceTest {
	/**
	 * Définition du Mock par annotation {@link Mockito}
	 * @see Mock
	 */
	@Mock
	private JavaMailSender mailSender;
	
	private TodoService todoService;
	
	@Before
	public void init(){
		todoService = new TodoService();
		//On met en place le Mock explicitement sur le Service testé
		todoService.setJavaMailSender(mailSender);
	}

	@Test
	public void testCreateAndRetrieveAFreshTodoList() throws Exception {
		TodoList newList = todoService.newList("masuperlistedetrucsafaire", "listowner@test.com");
		assertNotNull(newList);
		
		Task newTask = task("Faire les courses","faire les courses hebdo en prenant commande pour le drive", DATE_FORMAT.parse("12/10/2012 19:30"));
		
		todoService.addTask(newList.getName(),newTask);
		
		//Action
		TodoList list = todoService.getTodoList("masuperlistedetrucsafaire");

		assertNotNull(list);
		assertTrue(list.getTodos().contains(newTask));
		
		//On vérifie ici que le service a tenté d'envoyer un mail via le javaMailSender
		verify(mailSender).send(any(SimpleMailMessage.class));
	}

	
	
	/**
	 * Ce test valide que l'on peut créer une todolist même si le serveur de mail n'est pas joignable
	 * (i.e.  l'API de {@link JavaMailSender} lève une exception de type {@link MailSendException}
	 */
	@Test
	public void testIfWeCantSendEmailThatDoNotImpactBusinessService() throws Exception {
		doThrow(new MailSendException("Server not found")).when(mailSender).send(any(SimpleMailMessage.class));
		
		TodoList newList = todoService.newList("malisteavecunmailenechec", "listowner@test.com");
		TodoList myList = todoService.getTodoList("malisteavecunmailenechec");
		
		assertNotNull(myList);
		assertEquals(newList.getName(),myList.getName());
		
		//On vérifie qu'on a tenté d'envoyer un mail (facultatif)
		verify(mailSender).send(any(SimpleMailMessage.class));
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
