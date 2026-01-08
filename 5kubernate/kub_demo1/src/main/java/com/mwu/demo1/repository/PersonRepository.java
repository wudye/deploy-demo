package com.mwu.demo1.repository;

import com.mwu.demo1.domain.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface PersonRepository extends CrudRepository<Person, String> {

	Set<Person> findByFirstNameAndLastName(String firstName, String lastName);
	Set<Person> findByAge(int age);
	Set<Person> findByAgeGreaterThan(int age);

}
