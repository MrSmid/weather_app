package com.evolenta.locationmicroservice.service;

import com.evolenta.locationmicroservice.model.Geodata;
import com.evolenta.locationmicroservice.repository.GeodataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class GeodataService {

    GeodataRepository repository;

    @Autowired
    public GeodataService(GeodataRepository repository) {
        this.repository = repository;
    }

    public Optional<Geodata> getLocationByName(String name) {
        return repository.findByName(name);
    }

    public Geodata saveGeodata(Geodata geodata) {
        return repository.save(geodata);
    }

    public boolean geodataIsExistsByName(String name) {
        return repository.existsGeodataByName(name);
    }

    @Transactional
    public Geodata updateGeodataByName(String locationName, Geodata geodata) {
        Geodata oldGeodata = getLocationByName(locationName).get();
        oldGeodata.setName(geodata.getName());
        oldGeodata.setLat(geodata.getLat());
        oldGeodata.setLon(geodata.getLon());
        return saveGeodata(oldGeodata);
    }

    @Transactional
    public void deleteGeodataByName(String name) {
        repository.deleteByName(name);
    }
}
