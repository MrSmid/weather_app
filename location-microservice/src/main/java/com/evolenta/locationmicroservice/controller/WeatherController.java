package com.evolenta.locationmicroservice.controller;

import com.evolenta.locationmicroservice.model.Geodata;
import com.evolenta.locationmicroservice.model.Weather;
import com.evolenta.locationmicroservice.service.GeodataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RestController
public class WeatherController {

    private GeodataService service;
    private RestTemplate restTemplate;
    @Value("${weather.url}")
    String weatherUrl;

    @Autowired
    public WeatherController(GeodataService service, RestTemplate restTemplate) {
        this.service = service;
        this.restTemplate = restTemplate;
    }

    // Подразумевается, что в БД нет второго "Саранска". Для обхода проблемы с дубликатами имен следует
    // использовать больше подробностей при поиске. Например расширить модель Geodata, добавив туда регион, страну и т.д
    @GetMapping("/weather")
    public ResponseEntity<Weather> redirectRequestWeather(@RequestParam String location) {
        Optional<Geodata> geodata = service.getLocationByName(location);
        if (geodata.isPresent()) {
            String url = String.format("http://%s?lat=%s&lon=%s", weatherUrl, geodata.get().getLat(), geodata.get().getLon());
            return new ResponseEntity<>(restTemplate.getForObject(url, Weather.class), HttpStatus.OK);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/location")
    public ResponseEntity<Geodata> getLocation(@RequestParam String location) {
        Optional<Geodata> geodata = service.getLocationByName(location);
        return geodata.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/location")
    public Geodata addLocation(@RequestBody Geodata geodata) {
        return service.saveGeodata(geodata);
    }

    @PutMapping("/location/{locationName}")
    public ResponseEntity<Geodata> updateLocationData(@PathVariable String locationName, @RequestBody Geodata geodata) {
        if (service.geodataIsExistsByName(locationName)) {
            Geodata updatedGeodata = service.updateGeodataByName(locationName, geodata);
            return new ResponseEntity<>(updatedGeodata, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(service.saveGeodata(geodata), HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/location/{locationName}")
    public ResponseEntity<Object> deleteLocation(@PathVariable String locationName) {
        if (service.geodataIsExistsByName(locationName)) {
            service.deleteGeodataByName(locationName);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
