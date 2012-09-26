package com.francetelecom.devdays;

import static junit.framework.Assert.*;
import static org.mockito.Matchers.*;

import static org.mockito.Mockito.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
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
	 * Définition des Mock par annotation {@link Mockito}
	 * @see Mock
	 */
	@Mock
	private JavaMailSender mailSender;
	@Mock
	private EntityManager entityManager;
	@Mock
	private Query findTodoListQuery;
	
	/**
	 * L'injection des mocks dans une instance est prise en compte par Mockito si on le souhaite
	 * @see InjectMocks
	 */
	@InjectMocks @Spy
	private TodoService todoService;
	
	/**
	 * Les {@link ArgumentCaptor} permettent de récupérer les valeurs 
	 * qui ont été passées en paramètres d'appels aux mocks lors des tests
	 */
	@Captor
	private ArgumentCaptor<SimpleMailMessage> mailCaptor;
	@Captor
	private ArgumentCaptor<TodoList> listeCaptor;
	
	@Before
	public void init(){
		//Simulation du comportement de l'entityManager
		//Si on lui demande de créer une requête il renvoie un Mock
		when(entityManager.createQuery(matches("from "+TodoList.class.getSimpleName()+".*"))).thenReturn(findTodoListQuery);
		
		//Simulation du comportement de la requête
		//Si on lui demande de changer un paramètre elle se renvoie elle même (cohérence du fonctionnement nominal)
		when(findTodoListQuery.setParameter(eq("name"), any())).thenReturn(findTodoListQuery);
	}
	
	/**
	 * Teste le comportement de la récupération de liste vis à vis de la couche d'accès aux données.
	 */
	@Test
	public void testGetTodoList() throws Exception {
		//Lorsque la base est intéroggée on retourne la liste
		TodoList mockList = new TodoList();
		mockList.setName("masuperlistedetrucsafaire");
		mockList.setOwnerEmail("listowner@test.com");
		when(findTodoListQuery.getSingleResult()).thenReturn(mockList);
		
		//Action
		TodoList list = todoService.getTodoList("masuperlistedetrucsafaire");
		
		//Tests
		assertEquals(mockList.getName(), list.getName());
		verify(findTodoListQuery).setParameter("name", "masuperlistedetrucsafaire");
		verify(findTodoListQuery).getSingleResult();
	}

	/**
	 * Teste le comportement de la création d'une nouvelle liste (persistance d'une liste et envoi d'un mail)
	 */
	@Test
	public void testNewList() throws Exception {
		doNothing().when(todoService).notifyOwner(anyString(), anyString(), anyString());
		//Action
		TodoList newList = todoService.newList("masuperlistedetrucsafaire", "listowner@test.com");
		
		//Tests
		assertNotNull(newList);
		assertEquals("masuperlistedetrucsafaire", newList.getName());
		assertEquals("listowner@test.com", newList.getOwnerEmail());
		
		/* On vérifie ici que le service a tenté d'envoyer un mail via le javaMailSender */
		verify(mailSender).send(mailCaptor.capture());
		//La capture du message permet de valider que le mail a bien été envoyé au bon destinataire
		SimpleMailMessage emailSended = mailCaptor.getValue();
		assertEquals("listowner@test.com",emailSended.getTo()[0]);
		//On valide aussi que le contenu du mail contient bien le nom de la liste
		assertTrue(emailSended.getText().contains("masuperlistedetrucsafaire"));
		
		/* Validation de la persistance de la liste vers la base de données */
		verify(entityManager).persist(listeCaptor.capture()); 
		//On valide ici le contenu de la liste envoyée à la base via JPA
		assertEquals("masuperlistedetrucsafaire", listeCaptor.getValue().getName());
		assertEquals("listowner@test.com", listeCaptor.getValue().getOwnerEmail());
		assertTrue(listeCaptor.getValue().getTodos().isEmpty());
	}
	
	/**
	 * Ce test valide que l'on peut créer une todolist même si le serveur de mail n'est pas joignable
	 * (i.e.  l'API de {@link JavaMailSender} lève une exception de type {@link MailSendException}
	 */
	@Test
	public void testIfWeCantSendEmailThatDoNotImpactBusinessService() throws Exception {
		doThrow(new MailSendException("Server not found")).when(mailSender).send(any(SimpleMailMessage.class));
		
		TodoList newList = todoService.newList("malisteavecunmailenechec", "listowner@test.com");
		
		//On vérifie qu'on a tenté d'envoyer un mail (facultatif)
		verify(mailSender).send(any(SimpleMailMessage.class));
		verify(entityManager).persist(newList); //On valide ici que le système a tenté de persister l'objet
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
