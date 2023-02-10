package com.evolenta.weathermicroservice.model.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Coord{
    private double lon;
    private double lat;
}
