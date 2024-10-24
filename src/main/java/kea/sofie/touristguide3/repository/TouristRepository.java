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

       //Nye update metode !! med en masse comments.....
       public TouristAttraction updateAttraction(TouristAttraction updatedAttraction) {
           // SQL til at opdatere attraktionen i Attraction_tabellen
           String updateAttractionSQL = "UPDATE Attraction SET a_name = ?, description = ?, city_id = ?, fee = ? WHERE id = ?";

           try (Connection connection = DriverManager.getConnection(url, user, password)) {
               // Opretter et PreparedStatement for at udføre opdateringen
               try (PreparedStatement updateAttractionStatement = connection.prepareStatement(updateAttractionSQL)) {
                   // Sætter værdierne fra updatedAttraction objektet ind i SQL-forespørgslen
                   updateAttractionStatement.setString(1, updatedAttraction.getName());        // Opdaterer attraktionen navn
                   updateAttractionStatement.setString(2, updatedAttraction.getDescription()); // Opdaterer attraktionens beskrivelse
                   updateAttractionStatement.setInt(3, updatedAttraction.getCity_id());        // Opdaterer by-id for attraktionen
                   updateAttractionStatement.setInt(4, updatedAttraction.getFee());            // Opdaterer gebyret for attraktionen
                   updateAttractionStatement.setInt(5, updatedAttraction.getId());             // Identificerer hvilken attraktion der skal opdateres
                   updateAttractionStatement.executeUpdate(); // Udfører opdateringen
               }

               // Hent eksisterende tags for at undgå at tilføje dubletter
               List<String> existingTags = getTagsForAttraction(updatedAttraction.getId());

               // Gennemgår de tags, der skal tilknyttes den opdaterede attraktion
               for (String tagName : updatedAttraction.getTags()) {
                   if (!existingTags.contains(tagName)) { // Tjekker om tagget allerede findes for attraktionen
                       int tagId = findTagIdByName(tagName); // Finder tag_id for tagget baseret på tag_name
                       if (tagId != -1) { // Hvis tagget findes i Tag-tabellen
                           // SQL til at tilføje det nye tag til Attraction_tag-tabellen
                           String insertTagSQL = "INSERT INTO Attraction_tag (attraction_id, tag_id) VALUES (?, ?)";
                           try (PreparedStatement insertTagStatement = connection.prepareStatement(insertTagSQL)) {
                               insertTagStatement.setInt(1, updatedAttraction.getId()); // Angiver attraction_id
                               insertTagStatement.setInt(2, tagId);                    // Angiver tag_id
                               insertTagStatement.executeUpdate(); // Udfører indsættelsen af tagget
                           }
                       }
                   }
               }

           } catch (SQLException e) {
               e.printStackTrace(); // Håndterer eventuelle SQL-fejl
           }
           return updatedAttraction; // Returnerer det opdaterede TouristAttraction objekt
       }

       private int findTagIdByName(String tagName) {
           // SQL til at finde id for et givent tag_name
           String sql = "SELECT id FROM Tag WHERE tag_name = ?";
           int tagId = -1;  // Standard værdi, hvis tag ikke findes

           try (Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

               preparedStatement.setString(1, tagName); // Angiver tag_name i forespørgslen
               ResultSet resultSet = preparedStatement.executeQuery(); // Udfører forespørgslen

               // Tjekker om der er et resultat og henter tag_id
               if (resultSet.next()) {
                   tagId = resultSet.getInt("id"); // Henter id fra resultatet
               }
           } catch (SQLException e) {
               e.printStackTrace(); // Håndterer eventuelle SQL-fejl
           }
           return tagId; // Returnerer tag_id, eller -1 hvis tagget ikke blev fundet
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

       public void saveAttraction(TouristAttraction touristAttraction) {
           String sql = "INSERT INTO Attraction (a_name, description, city_id, fee) VALUES (?, ?, ?, ?)";

           try (Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

               statement.setString(1, touristAttraction.getName());
               statement.setString(2, touristAttraction.getDescription());
               statement.setInt(3, touristAttraction.getCity_id()); // Brug city_id
               statement.setInt(4, touristAttraction.getFee());

               statement.executeUpdate();

               // Hent den genererede attraction_id for at gemme tags
               try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                   if (generatedKeys.next()) {
                       int attractionId = generatedKeys.getInt(1);
                       saveAttractionTags(attractionId, touristAttraction.getTags());
                   }
               }

           } catch (SQLException e) {
               e.printStackTrace();
           }
       }

       // Gem tags for attraktionen
       private void saveAttractionTags(int attractionId, List<String> tags) {
           String sql = "INSERT INTO Attraction_tag (attraction_id, tag_id) VALUES (?, ?)";

           try (Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement statement = connection.prepareStatement(sql)) {

               for (String tag : tags) {
                   int tagId = findTagIdByName(tag); // Få tag_id for det specifikke tag
                   statement.setInt(1, attractionId);
                   statement.setInt(2, tagId);
                   statement.addBatch(); // Tilføj til batch
               }
               statement.executeBatch(); // Udfør batch-indsættelse

           } catch (SQLException e) {
               e.printStackTrace();
           }
       }

       public boolean deleteAttraction(String name) {
           // Først slet relaterede rækker fra Attraction_tag
           String deleteTagsSQL = "DELETE FROM Attraction_tag WHERE attraction_id IN " +
                   "(SELECT id FROM Attraction WHERE a_name = ?)";

           // Derefter slet selve attraktionerne fra Attraction-tabellen
           String deleteAttractionSQL = "DELETE FROM Attraction WHERE a_name = ?";

           try (Connection connection = DriverManager.getConnection(url, user, password)) {
               // Start transaktionen
               connection.setAutoCommit(false);

               // Slet tags relateret til attraktionerne
               try (PreparedStatement deleteTagsStatement = connection.prepareStatement(deleteTagsSQL)) {
                   deleteTagsStatement.setString(1, name);
                   deleteTagsStatement.executeUpdate();
               }

               // Slet attraktionerne
               try (PreparedStatement deleteAttractionStatement = connection.prepareStatement(deleteAttractionSQL)) {
                   deleteAttractionStatement.setString(1, name);
                   int rowsAffected = deleteAttractionStatement.executeUpdate();

                   // Hvis rækker blev slettet, commit transaktionen
                   if (rowsAffected > 0) {
                       connection.commit();
                       return true;
                   } else {
                       connection.rollback(); // Hvis ingen rækker blev slettet, rollback
                       return false;
                   }
               } catch (SQLException e) {
                   connection.rollback(); // Rollback i tilfælde af fejl
                   e.printStackTrace();
                   return false;
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }
           return false;
       }
   }








