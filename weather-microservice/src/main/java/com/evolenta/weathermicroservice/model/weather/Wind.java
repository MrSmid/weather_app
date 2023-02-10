package com.evolenta.weathermicroservice.model.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Wind{
    private double speed;
    private int deg;
    private double gust;
}
