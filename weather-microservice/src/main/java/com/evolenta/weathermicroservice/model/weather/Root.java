package com.evolenta.weathermicroservice.model.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Root{
    private Coord coord;
    private ArrayList<Weather> weather;
    private Main main;
    private String base;
    private int visibility;
    private Wind wind;
    private Rain rain;
    private Clouds clouds;
    private int dt;
    private Sys sys;
    private int timezone;
    private int id;
    private String name;
    private int cod;
}
