package kea.sofie.touristguide3.model;

import java.util.List;

public class TouristAttraction {

    private int id; //ID'et fra databasen (auto-incremented)
    private String name;
    private String description;
    private String city;
    private int city_id;
    private int fee;
    private List<String> tags;

    public TouristAttraction() {
    }

    public TouristAttraction(String name, String description, String city, int fee) {
        this.name = name;
        this.description = description;
        this.city = city;
        this.city_id = city_id;
        this.fee = fee;
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }
    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
