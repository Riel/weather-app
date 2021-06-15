package com.gfa.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WeatherApplication {


  // Write an app where users can provide cities with country codes
  // Based on these data the application shall display actual temperature for the given city in celsius
  //       Display format: "Temperature for {city name}  is: {temperature} °C'"
  // Successful requests shall be listed at the bottom of the page containing
  //       city name, country code and temperature

  // When there is no data provided by the form, display on the main page:
  //      "No location specified"
  // When there is no data available for the city an error page shall appear, saying:
  //     "There is no data available for city {city name} with country code {code}"
  // Handle all other error scenarios on a different error page saying:
  //     "An unknown error happened, sorry for the inconvenience"
  public static void main(String[] args) {
    SpringApplication.run(WeatherApplication.class, args);
  }
}
