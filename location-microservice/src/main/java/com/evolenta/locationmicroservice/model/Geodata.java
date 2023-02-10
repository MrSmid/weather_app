package com.evolenta.locationmicroservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Geodata {
    @Id
    @GeneratedValue
    int id;
    @NotNull
    private double lon;
    @NotNull
    private double lat;
    @NotNull
    private String name;
}
