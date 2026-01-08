package com.mwu.demo1.service;

import com.mwu.demo1.domain.Person;
import com.mwu.demo1.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashSet;
import java.util.Set;

@Service
public class PersonService {

	@Autowired
	PersonRepository repository;
	@Autowired
	ApplicationEventPublisher applicationEventPublisher;

	@Transactional
	public Set<Person> doIt(Person... persons) {
		Set<Person> personSet = new HashSet<>();
		for (Person p: persons) {
			personSet.add(repository.save(p));
		}
//		applicationEventPublisher.publishEvent(new PersonAddEvent());
		return personSet;
	}
}
