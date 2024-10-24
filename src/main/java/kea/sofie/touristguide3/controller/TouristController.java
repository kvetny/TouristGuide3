// Controller-pakken indeholder klasser, der håndterer HTTP-forespørgelser fra klienter (en browser)
package kea.sofie.touristguide3.controller;

import kea.sofie.touristguide3.model.TouristAttraction;
import kea.sofie.touristguide3.repository.TouristRepository;
import kea.sofie.touristguide3.service.TouristService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

//hej med dig ...

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
        model.addAttribute("availableTags", touristRepository.getTags()); // Tilgængelige tags til checkbokse
        return "editAttraction";
    }


    @GetMapping("/{name}/tags")
    public String getAttractionTags(@PathVariable String name, Model model) {
        TouristAttraction attraction = touristService.getOneAttraction(name);
        model.addAttribute("touristAttraction", attraction);
        return "tags";
    }

    // Metode der viser formularen til at tilføje en ny attraktion
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("touristAttraction", new TouristAttraction());
        model.addAttribute("cities", touristRepository.getCities());
        model.addAttribute("availableTags", touristRepository.getTags());
        return "addAttraction";
    }

    // En ny instans af TouristAttraction bliver gemt i databasen!
    @PostMapping("/save")
    public String saveAttraction(@ModelAttribute TouristAttraction touristAttraction,
                                 @RequestParam("city") int cityId,
                                 @RequestParam("tags") List<String> selectedTags) {
        // Indstil city_id og tags for attraktionen
        touristAttraction.setCity_id(cityId);
        touristAttraction.setTags(selectedTags);

        // Gem attraktionen gennem service laget
        touristService.saveAttraction(touristAttraction);

        // Omdirigér til oversigten efter gemning
        return "redirect:/attractions";
    }

    // En eksisterende attraktion bliver opdateret!
    @PostMapping("/update")
    public String updateAttraction(@ModelAttribute TouristAttraction touristAttraction,
                                   @RequestParam("city") int cityId,
                                   @RequestParam("tags") List<String> selectedTags) {
        // Sæt city_id og tags på den opdaterede attraktion
        touristAttraction.setCity_id(cityId);
        touristAttraction.setTags(selectedTags);

        // Opdater attraktionen gennem service laget
        touristService.updateAttraction(touristAttraction);

        // Omdirigér til oversigten efter opdatering
        return "redirect:/attractions";
    }

    // Metode til at slette en attraktion
    @PostMapping("/delete")
    public String deleteAttraction(@RequestParam String name) {
        boolean deleted = touristService.deleteAttraction(name);
        if (deleted) {
            return "redirect:/attractions";
        } else {
            return "redirect:/attractions"; // Viser listen med en fejlbesked
        }
    }
}




