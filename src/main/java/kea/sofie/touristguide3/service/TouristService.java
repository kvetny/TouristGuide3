package kea.sofie.touristguide3.service;

import kea.sofie.touristguide3.model.TouristAttraction;
import kea.sofie.touristguide3.repository.TouristRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TouristService {

    private final TouristRepository touristRepository;

    public TouristService(TouristRepository touristRepository) {
        this.touristRepository = touristRepository;
    }


    // Metode der returnerer hele listen af turistattraktioner
    public List<TouristAttraction> getAttractions() {
        return touristRepository.findAll();
    }

    public TouristAttraction getOneAttraction(String name) {
        return touristRepository.getOneAttraction(name);
    }


    public void updateAttraction(TouristAttraction updatedAttraction) {
        touristRepository.updateAttraction(updatedAttraction);
    }




}


