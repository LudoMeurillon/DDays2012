package com.francetelecom.devdays;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.francetelecom.devdays.domain.Task;
import com.francetelecom.devdays.domain.TodoList;

@Service
public class TodoService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JavaMailSender mailSender;

	@Transactional
	public TodoList getTodoList(String name) {
		return (TodoList) entityManager.createQuery("from " + TodoList.class.getSimpleName() + " where name=:name").setParameter("name", name)
				.getSingleResult();
	}

	@Transactional
	public void addTask(String listname, Task task) {
		entityManager.persist(task);
		TodoList list = getTodoList(listname);
		list.getTodos().add(task);
	}

	@Transactional
	public TodoList newList(String name, String owner) {
		TodoList list = new TodoList();
		list.setName(name);
		list.setOwnerEmail(owner);
		entityManager.persist(list);
		notifyOwner(list.getOwnerEmail(), "Liste créée", "Une nouvelle liste [" + name
				+ "] a été créée pour vous\nVous recevrez des notification liées à cette liste dans le futur");
		return list;
	}

	private void notifyOwner(String recipient, String title, String content) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("mockito.devdays2012@orange.com");
		message.setTo(recipient);
		message.setSubject(title);
		message.setText(content);
		try {
			mailSender.send(message);
		} catch (MailException e) {
			LOGGER.error("Une erreur est survenue lors de l'envoi du mail", e);
		}
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TodoService.class);

}
