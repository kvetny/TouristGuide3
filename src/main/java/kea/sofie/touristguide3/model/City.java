package kea.sofie.touristguide3.model;

public class City {

        private int city_id;
        private String c_name;

        // Constructor
        public City(int city_id, String c_name) {
            this.city_id = city_id;
            this.c_name = c_name;
        }

        // Getters and setters
        public int getCity_id() {
            return city_id;
        }

        public void setCity_id(int city_id) {
            this.city_id = city_id;
        }

        public String getC_name() {
            return c_name;
        }

        public void setC_name(String c_name) {
            this.c_name = c_name;
        }
}
