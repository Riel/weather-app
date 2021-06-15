package com.gfa.weather.controllers;


import com.gfa.weather.models.CityData;
import com.gfa.weather.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WeatherController {

  private WeatherService service;

  @Autowired
  public WeatherController(WeatherService service) {
    this.service = service;
  }

  // Todo: handle exception
  @RequestMapping(path="/", method = RequestMethod.GET)
  public String displayWeatherForCity(Model model) {
    model.addAttribute("requests", service.getSuccessfulRequests());
    return "index";
  }


  @RequestMapping(path="/nodata", method = RequestMethod.GET)
  public String displayNoDataPage() {
    return "missingdata";
  }

  @RequestMapping(path="/general-error", method = RequestMethod.GET)
  public String displayErrorPage() {
    return "general-error";
  }

  // Todo: handle exception
  @RequestMapping(path="/add", method = RequestMethod.POST)
  public String displayWeatherForCity(RedirectAttributes redirectAttribute, @ModelAttribute CityData city)
      throws Throwable {
    if (!service.hasValidCityData(city)){
      redirectAttribute.addFlashAttribute("missingDataError", true);
      return "redirect:/";
    }

    Integer temperature;
    try {
      temperature = service.getTemperatureForCity(city);
      redirectAttribute.addFlashAttribute("city", city);
    } catch (Exception e) {
      return "redirect:/general-error";
    }

    if (temperature != null){
      redirectAttribute.addFlashAttribute("temperature", temperature);
      return "redirect:/";
    } else {
      return "redirect:/nodata";
    }
  }
}
