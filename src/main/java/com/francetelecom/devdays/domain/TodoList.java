package com.francetelecom.devdays.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class TodoList {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private int id;

	private String name;

	private String ownerEmail;

	@OneToMany(fetch=FetchType.EAGER)
	private Set<Task> todos;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Task> getTodos() {
		return todos;
	}

	public void setTodos(Set<Task> todos) {
		this.todos = todos;
	}
	
	public String getOwnerEmail() {
		return ownerEmail;
	}

	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

}
