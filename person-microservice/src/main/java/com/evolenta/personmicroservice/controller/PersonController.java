package com.evolenta.personmicroservice.controller;

import com.evolenta.personmicroservice.model.Person;
import com.evolenta.personmicroservice.model.Weather;
import com.evolenta.personmicroservice.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {

    private PersonService service;
    private RestTemplate restTemplate;
    @Value("${location.weather.url}")
    String locationWeatherUrl;

    @Autowired
    public PersonController(PersonService service, RestTemplate restTemplate) {
        this.service = service;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/weather/{id}")
    public ResponseEntity<Weather> redirectRequestWeatherByPersonId(@PathVariable int id) {
        Optional<Person> person = service.getPersonById(id);
        if (person.isPresent()) {
            String url = String.format("http://%s%s", locationWeatherUrl, person.get().getLocationName());
            return new ResponseEntity<>(restTemplate.getForObject(url, Weather.class), HttpStatus.OK);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping
    public Iterable<Person> getAllPersons() {
        return service.getAllPersons();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> GetPersonById(@PathVariable int id) {
        Optional<Person> person = service.getPersonById(id);
        return person.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping
    public ResponseEntity<Person> savePerson(@RequestBody Person person) {
        return service.personIsExistById(person.getId())
                ? new ResponseEntity<>(service.getPersonById(person.getId()).get(), HttpStatus.BAD_REQUEST)
                : new ResponseEntity<>(service.savePerson(person), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable int id, @RequestBody Person person) {
        if (service.personIsExistById(id)) {
            Person updatedPerson = service.updatePerson(id, person);
            return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(service.savePerson(person), HttpStatus.CREATED);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletePerson(@PathVariable int id) {
        if (service.personIsExistById(id)) {
            service.deletePerson(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
