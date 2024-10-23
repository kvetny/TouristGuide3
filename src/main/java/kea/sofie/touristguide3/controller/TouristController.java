// Controller-pakken indeholder klasser, der håndterer HTTP-forespørgelser fra klienter (en browser)
package kea.sofie.touristguide3.controller;

import kea.sofie.touristguide3.model.TouristAttraction;
import kea.sofie.touristguide3.repository.TouristRepository;
import kea.sofie.touristguide3.service.TouristService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;


// Betyder at klassen er en controller, der håndterer HTTP-forespørgelser
@Controller
@RequestMapping("attractions") // definerer basis-URL'en for alle endpoints i denne controller
public class TouristController {

    private final TouristService touristService;
    private final TouristRepository touristRepository;

    // Konstruktør Injection
    public TouristController(TouristService touristService, TouristRepository touristRepository) {
        this.touristService = touristService;
        this.touristRepository = touristRepository;
    }


    // Definerer et HTTP GET-endpoint, der returnerer alle turistattraktioner
    @GetMapping("")
    public String getTouristAttraction(Model model) {
        List<TouristAttraction> touristAttractions = touristService.getAttractions(); // Hent attraktionerne fra servicen
        // Tilføj attraktionerne til modellen, så Thymeleaf kan få adgang til dem
        model.addAttribute("attractions", touristAttractions);
        // Returnér HTML-siden 'attractionList.html' placeret i 'templates' mappen
        return "attractionList";
    }

    @GetMapping("/edit/{name}")
    public String showEditForm(@PathVariable String name, Model model) {
        TouristAttraction attraction = touristService.getOneAttraction(name);
        model.addAttribute("touristAttraction", attraction);
        model.addAttribute("cities", touristRepository.getCities()); // Henter bynavne
        model.addAttribute("availableTags", touristRepository.getTags());
        model.addAttribute("city", touristRepository.getCities());
        model.addAttribute("city_id", touristRepository.getCities()); // nicolai
        return "editAttraction";
    }


    /*
    // Metode til at redigere i en attraktion
    @GetMapping("/edit/{name}")
    public String showEditForm(@PathVariable String name, Model model) {
        TouristAttraction attraction = touristService.getOneAttraction(name);
        model.addAttribute("touristAttraction", attraction);
        model.addAttribute("cities", touristRepository.getCities());
        model.addAttribute("availableTags", touristRepository.getTagsForAttraction(attraction.getId()));
        return "editAttraction";
    }*/

    @GetMapping("/{name}/tags")
    public String getAttractionTags(@PathVariable String name, Model model) {
        TouristAttraction attraction = touristService.getOneAttraction(name);
        model.addAttribute("touristAttraction", attraction);
        return "tags";
    }

    @PostMapping("/update")
    public String updateAttraction(@ModelAttribute TouristAttraction touristAttraction) {
        touristService.updateAttraction(touristAttraction);
        return "redirect:/attractions";
    }

    /*@PostMapping("/update")
    public String updateAttraction(@ModelAttribute TouristAttraction touristAttraction,
                                   @RequestParam String city_name) {
        int cityId = touristRepository.getCityId(city_name); // Få city_id ud fra city_name
        touristAttraction.setCity_id(cityId); // Sæt city_id på attraktionen

        touristService.updateAttraction(touristAttraction); // Opdater attraktionen
        return "redirect:/attractions";
    }*/

    /*// Metode til at opdatere en ændret attraktion
    @PostMapping("/update")
    public String updateAttraction(@ModelAttribute TouristAttraction touristAttraction) {
        touristService.updateAttraction(touristAttraction);
        return "redirect:/attractions";
    }*/

    // Metode til at tilføje en ny attraktion til databasen
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("touristAttraction", new TouristAttraction());
        model.addAttribute("cities", touristRepository.getCities());
        model.addAttribute("availableTags", touristRepository.getTags());
        return "addAttraction";
    }



}




