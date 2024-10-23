// Repository-pakken indeholder klasser, der håndterer dataadgang og lagring
   package kea.sofie.touristguide3.repository;

   import kea.sofie.touristguide3.model.City;
   import kea.sofie.touristguide3.model.TouristAttraction;
   import org.springframework.stereotype.Repository;

   import java.sql.*;
   import java.util.ArrayList;
   import java.util.List;

   // Betyder at denne klasse fungerer som et repository i Spring boot applikationen (Dataadgang og lagring)
   @Repository
   public class TouristRepository {

       private String url = System.getenv("DB_URL");
       private String user = System.getenv("DB_USER");
       private String password = System.getenv("DB_PASSWORD");


       public List<TouristAttraction> findAll() {
           List<TouristAttraction> attractions = new ArrayList<>();
           String sql = "SELECT a.a_name, a.description, c.c_name, a.fee " +
                   "FROM Attraction a " +
                   "JOIN City c ON a.city_id = c.city_id";

           try (Connection connection = DriverManager.getConnection(url, user, password)) {
               Statement statement = connection.createStatement();
               ResultSet resultSet = statement.executeQuery(sql);

               while (resultSet.next()) {
                   TouristAttraction attraction = new TouristAttraction(
                           resultSet.getString("a_name"),
                           resultSet.getString("description"),
                           resultSet.getString("c_name"),
                           resultSet.getInt("fee"));
                   attractions.add(attraction);
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }
           return attractions;
       }

       public TouristAttraction updateAttraction(TouristAttraction updatedAttraction) {
           String sql = "UPDATE Attraction SET a_name = ?, description = ?, city_id = ?, fee = ? WHERE a_name = ?";

           try (Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement statement = connection.prepareStatement(sql)) {

               statement.setString(1, updatedAttraction.getName());       // Sætter den opdaterede a_name
               statement.setString(2, updatedAttraction.getDescription()); // Sætter den opdaterede description
               statement.setString(3, updatedAttraction.getCity());        // Sætter den opdaterede city_id
               statement.setInt(4, updatedAttraction.getFee());           // Sætter den opdaterede fee
               statement.setString(5, updatedAttraction.getName());       // Bruger det gamle navn til WHERE-klausulen

               statement.executeUpdate();  // Udfør opdateringen
           } catch (SQLException e) {
               e.printStackTrace();
           }
           return updatedAttraction;
       }

       public TouristAttraction getOneAttraction(String name) {
           // SQL til at hente attraktionen baseret på navn
           String sqlAttraction = "SELECT a.id, a.a_name, a.description, c.c_name, a.fee " +
                   "FROM Attraction a " +
                   "JOIN City c ON a.city_id = c.city_id " +
                   "WHERE a.a_name = ?";
           TouristAttraction attraction = null;

           try (Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement preparedStatement = connection.prepareStatement(sqlAttraction)) {

               preparedStatement.setString(1, name);
               ResultSet resultSet = preparedStatement.executeQuery();

               // Hvis vi finder attraktionen, opretter vi et objekt
               if (resultSet.next()) {
                   attraction = new TouristAttraction();
                   attraction.setId(resultSet.getInt("id"));
                   attraction.setName(resultSet.getString("a_name"));
                   attraction.setDescription(resultSet.getString("description"));
                   attraction.setCity(resultSet.getString("c_name"));
                   attraction.setFee(resultSet.getInt("fee"));
                   attraction.setTags(getTagsForAttraction(attraction.getId()));
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }
           return attraction;
       }

       // Metode til at finde tags til den specifikke attraktion!
       private List<String> getTagsForAttraction(int attractionId) {
           List<String> tags = new ArrayList<>();
           String sqlTags = "SELECT t.tag_name FROM Tag t " +
                   "JOIN Attraction_tag at ON t.id = at.tag_id " +
                   "WHERE at.attraction_id = ?";

           try (Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement preparedStatement = connection.prepareStatement(sqlTags)) {

               preparedStatement.setInt(1, attractionId);
               ResultSet resultSet = preparedStatement.executeQuery();

               while (resultSet.next()) {
                   tags.add(resultSet.getString("tag_name"));
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }
           return tags;
       }

       // Hent tags fra databasen
       public List<String> getTags() {
           List<String> tags = new ArrayList<>();
           String sql = "SELECT tag_name FROM Tag";

           try (Connection connection = DriverManager.getConnection(url, user, password);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {

               while (resultSet.next()) {
                   tags.add(resultSet.getString("tag_name"));
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }
           return tags;
       }

       // Hent byer fra databasen
       public List<City> getCities() {
           List<City> cities = new ArrayList<>();
           String sql = "SELECT city_id, c_name FROM City";

           try (Connection connection = DriverManager.getConnection(url, user, password);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {

               while (resultSet.next()) {
                   int cityId = resultSet.getInt("city_id");
                   String cityName = resultSet.getString("c_name");
                   cities.add(new City(cityId, cityName)); // Tilføj by med både city_id og c_name
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }
           return cities;
       }
   }
       /*
       public int getCityId(String city_name) {
           List<City> cities = getCities();
           for (City city : cities) {
               if (city.getC_name().equals(city_name)) {
                   int city_id = city.getCity_id();
                   return city_id;
               }
           }
           return 0;
       }
   }
/*
       // Hent bys ID fra databasen baseret på city_name
       public int getCityId(City city_name) {
           int cityId = 0;
           String sql = "SELECT city_id FROM City WHERE c_name=?"; // Brug c_name i WHERE-klausulen

           try (Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

               // Sæt city_name som parameter i SQL-forespørgslen
               preparedStatement.setString(1, city_name);

               // Udfør forespørgslen
               try (ResultSet resultSet = preparedStatement.executeQuery()) {
                   if (resultSet.next()) { // Hvis der er resultater
                       cityId = resultSet.getInt("city_id"); // Hent city_id
                   }
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }
           return cityId;
       }

}
*/









