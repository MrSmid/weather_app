package com.evolenta.weathermicroservice.controller;

import com.evolenta.weathermicroservice.model.weather.Main;
import com.evolenta.weathermicroservice.model.weather.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Value("${appid}")
    private String appId;
    @Value("${url.weather}")
    private String urlWeather;
    RestTemplate restTemplate;
    private static final Logger LOG = LoggerFactory.getLogger(WeatherController.class);

    @Autowired
    public WeatherController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Принятие решения о кэшировании данных именно в данном контроллере обосновано тем,
    // что наиболее логичным является кэширование данных именно на той конечной точке,
    // в которой генерируется запрос на сторонний ресурс. В данном случае, ключ кэша будет составным,
    // состоящим из широты и долготы. Следовательно, необходимо сделать допущение, что между городом и
    // и его геолокацией существует взаимно однозначное отношение. Т.е каждый город в БД существует
    // в единственном экземпляре, и нет "второго Саранска" с координатами, оличающимися от "первого" парой метров.
    // Иначе может образоваться избыточность как в БД, так и в кэше.
    // Добавлены логи для тестирования кэширования (в этом методе и в методе для очистки кэша)
    @Cacheable("${caching.spring.cachename}")
    @GetMapping
    public Main getWeather(@RequestParam String lat, @RequestParam String lon) {
        String request = String.format("%s?lat=%s&lon=%s&units=metric&appid=%s", urlWeather, lat, lon, appId);
        LOG.info("Request to openweathermap was sent");
        return restTemplate.getForObject(request, Root.class).getMain();
    }

    // Недостаток данного подхода к очистке кэша в том, что каждую минуту очищается весь кэш
    // Но решение ставить на каждый отдельный элемент в кэше таймер, тоже не кажется целесообразным
    // Самое оптимальное, на мой взгляд, в данном случае, увеличить TTL хотя бы до 10 минут
    @CacheEvict(value = "${caching.spring.cachename}", allEntries = true)
    @Scheduled(fixedRateString = "${caching.spring.weatherTTL}")
    public void emptyHotelsCache() {
        LOG.info("Cache cleared");
    }
}
