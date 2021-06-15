package com.gfa.weather.services;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfa.weather.models.CityData;
import java.util.ArrayList;
import java.util.List;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.stereotype.Service;

/*
    HttpResponse<String> response = Unirest.get("https://community-open-weather-map.p.rapidapi.com/weather?q=London%2Cuk")
        .header("x-rapidapi-key", "5d009d0dcfmsh32ecaaf32f25194p1d6c39jsnae800280ebb9")
        .header("x-rapidapi-host", "community-open-weather-map.p.rapidapi.com")
        .asString();
    ObjectMapper mapper = new ObjectMapper();
    JsonNode node = mapper.readTree(response.getBody());



    implementation 'com.konghq:unirest-java:3.11.09'
    implementation 'com.fasterxml.jackson.core:jackson-databind:2.11.1'
 */


@Service
public class WeatherService {

  private List<String> successfulRequests;

  public WeatherService() {
    successfulRequests = new ArrayList<>();
  }


  public Integer getTemperatureForCity(CityData city) throws Throwable {
    HttpResponse<String> response = getResponse(city);
    JsonNode content = getContent(response);

    if (hasTemperature(content)){
      return processContent(city, content);
    }

    return null;
  }

  public List<String> getSuccessfulRequests() {
    return successfulRequests;
  }

  public boolean hasValidCityData(CityData city){
    return !city.getCountryCode().isEmpty() && !city.getCityName().isEmpty();
  }

  
  private HttpResponse getResponse(CityData city){
    String requestURL =  "https://community-open-weather-map.p.rapidapi.com/weather?q=";
    HttpResponse<String> response = Unirest.get(requestURL + city.getCityName() + "%2C" + city.getCountryCode())
        //TODO move sensitive data to environment variable
        .header("x-rapidapi-key", System.getenv("rapidApiKey"))
        .header("x-rapidapi-host", "community-open-weather-map.p.rapidapi.com")
        .asString();
    return response;
  }

  private JsonNode getContent(HttpResponse<String> response) throws JsonMappingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readTree(response.getBody());
  }

  private boolean hasTemperature(JsonNode content) {
    return content.has("main") && content.get("main").has("temp");
  }

  private int processContent(CityData city, JsonNode content) {
    int temperature = getTemperature(content);
    storeRequestData(city, temperature);
    return temperature;
  }

  private void storeRequestData(CityData city, int temperature) {
    successfulRequests.add("Request for "
        + city.getCityName()
        + " with code "
        + city.getCountryCode()
        + ": "
        + temperature);
  }

  private int getTemperature(JsonNode content) {
    double temperatureInKelvin = content.get("main").get("temp").asDouble();
    int temperature = (int)convertKelvinToCelsius(temperatureInKelvin);
    return temperature;
  }

  private double convertKelvinToCelsius(double temperature){
    return temperature - 273.15;
  }
}
