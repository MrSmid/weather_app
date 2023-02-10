package com.evolenta.personmicroservice.service;

import com.evolenta.personmicroservice.model.Person;
import com.evolenta.personmicroservice.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PersonService {

    private PersonRepository repository;

    @Autowired
    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    public boolean personIsExistById(int id) {
        return repository.existsById(id);
    }

    public Optional<Person> getPersonById(int id) {
        return repository.findById(id);
    }

    public Person savePerson(Person person) {
        return repository.save(person);
    }

    public Iterable<Person> getAllPersons() {
        return repository.findAll();
    }

    @Transactional
    public Person updatePerson(int id, Person person) {
        Person oldPerson = getPersonById(id).get();
        oldPerson.setName(person.getName());
        oldPerson.setLocationName(person.getLocationName());
        return savePerson(oldPerson);
    }

    public void deletePerson(int id) {
        repository.deleteById(id);
    }
}
