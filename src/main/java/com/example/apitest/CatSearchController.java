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

    private static final String API_URL = "https://api.api-ninjas.com/v1/cats?name=";
    private static final String API_KEY = "MAHAAHHA";

    @FXML
    public void initialize() {
        suggestionsDropdown.getItems().addAll(
                "Abyssinian", "Siamese", "Persian", "Bengal", "Maine Coon"
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
            long minWeight = (long) cat.get("min_weight");
            long maxWeight = (long) cat.get("max_weight");
            long minLife = (long) cat.get("min_life_expectancy");
            long maxLife = (long) cat.get("max_life_expectancy");

            ImageView imageView = new ImageView(new Image(imageLink));
            imageView.setFitWidth(300);
            imageView.setPreserveRatio(true);

            Label details = new Label(String.format(
                    "Name: %s\nOrigin: %s\nLength: %s\nWeight: %d - %d lbs\nLife Expectancy: %d - %d years",
                    name, origin, length, minWeight, maxWeight, minLife, maxLife
            ));

            resultBox.getChildren().setAll(imageView, details, resetButton);
            resetButton.setVisible(true);
        } catch (Exception e) {
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
