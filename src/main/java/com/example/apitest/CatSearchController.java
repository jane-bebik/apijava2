package com.example.apitest;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CatSearchController {
    @FXML
    private TextField breedInput;
    @FXML
    private Button searchButton;
    @FXML
    private VBox resultBox;
    @FXML
    private ComboBox<String> suggestionsDropdown;
    @FXML
    private Button resetButton;
    @FXML
    private ImageView logo;


    private static final String API_URL = "https://api.api-ninjas.com/v1/cats?name=";
    private static final String API_KEY = "APIKEYHERE";

    @FXML
    public void initialize() {

        logo.setImage(new Image("file:src/main/resources/img/catapp.png"));

        suggestionsDropdown.getItems().addAll(
                "abyssinian", "siamese", "persian", "bengal", "maine Coon", "ragdol"
        );
        searchButton.setOnAction(event -> fetchCatData(breedInput.getText()));
        resetButton.setOnAction(event -> resetInterface());
        suggestionsDropdown.setOnAction(event -> breedInput.setText(suggestionsDropdown.getValue()));
    }

    private void fetchCatData(String breed) {
        if (breed == null || breed.trim().isEmpty()) {
            showAlert("Error", "Please enter a valid cat breed.");
            return;
        }
        try {
            URL url = new URL(API_URL + breed);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("accept", "application/json");
            connection.setRequestProperty("X-Api-Key", API_KEY);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();

            displayCatData(response.toString());
        } catch (Exception e) {
            showAlert("Error", "Unable to fetch data. Please check the breed name.");
            e.printStackTrace();
        }
    }

    private void displayCatData(String jsonResponse) {
        try {
            JSONParser parser = new JSONParser();
            JSONArray catArray = (JSONArray) parser.parse(jsonResponse);
            if (catArray.isEmpty()) {
                showAlert("No Results", "No data found for the specified breed.");
                return;
            }

            JSONObject cat = (JSONObject) catArray.get(0);

            String name = (String) cat.get("name");
            String origin = (String) cat.get("origin");
            String length = (String) cat.get("length");
            String imageLink = (String) cat.get("image_link");

            long familyFriendly = ((Number) cat.get("family_friendly")).longValue();
            long shedding = ((Number) cat.get("shedding")).longValue();
            long generalHealth = ((Number) cat.get("general_health")).longValue();
            long playfulness = ((Number) cat.get("playfulness")).longValue();
            long childrenFriendly = ((Number) cat.get("children_friendly")).longValue();
            long grooming = ((Number) cat.get("grooming")).longValue();
            long intelligence = ((Number) cat.get("intelligence")).longValue();
            long otherPetsFriendly = ((Number) cat.get("other_pets_friendly")).longValue();

            //new stats just test
           /* String familyFriendly= (String) cat.get("family_friendly");
            String shedding = (String) cat.get("shedding");
            String general_health = (String) cat.get("general_health");
            String playfulness= (String) cat.get("playfulness");
            String meowing= (String) cat.get("meowing");
            String children_friendly= (String) cat.get("children_friendly");
            String grooming= (String) cat.get("grooming");
            String intelligence= (String) cat.get("intelligence");
            String other_pets_friendly= (String) cat.get("other_pets_friendly");*/

            // so this is so the Weight and life  is display as weight: min - max and life: min- max
            long minWeight = ((Number) cat.get("min_weight")).longValue();
            long maxWeight = ((Number) cat.get("max_weight")).longValue();
            long minLife = ((Number) cat.get("min_life_expectancy")).longValue();
            long maxLife = ((Number) cat.get("max_life_expectancy")).longValue();

            ImageView imageView = new ImageView(new Image(imageLink));
            imageView.setFitWidth(300);
            imageView.setPreserveRatio(true);

            Label details = new Label(String.format(
                    "Name: %s\nOrigin: %s\nLength: %s\nWeight: %d - %d lbs\nLife Expectancy: %d - %d years\n\n" +
                    "Family Friendly: %d/5\nShedding: %d/5\nGeneral Health: %d/5\nPlayfulness: %d/5\n" +
                    "Children Friendly: %d/5\nGrooming: %d/5\nIntelligence: %d/5\nOther Pets Friendly: %d/5",

                    name, origin, length, minWeight, maxWeight, minLife, maxLife, familyFriendly, shedding,
                    generalHealth, playfulness, childrenFriendly, grooming, intelligence, otherPetsFriendly
            ));

            resultBox.getChildren().setAll(imageView, details, resetButton);
            resetButton.setVisible(true);
        } catch (Exception e) {
            showAlert("Error", "Failed to parse cat data. Please try again.");
            e.printStackTrace();
        }
    }

    private void resetInterface() {
        breedInput.clear();
        suggestionsDropdown.setValue(null);
        resultBox.getChildren().clear();
        resetButton.setVisible(false);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
