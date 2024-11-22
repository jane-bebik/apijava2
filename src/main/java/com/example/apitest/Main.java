package com.example.apitest;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main extends Application {

    private static final String API_URL = "https://api.api-ninjas.com/v1/cats?name=ragdoll";
    private static final String API_KEY = "XvQCGEJFNvHaIZHkeR2tTw==42Pdzj6DtJ1RY4cN";

    @Override
    public void start(Stage primaryStage) {
        // Button to trigger API request
        Button fetchButton = new Button("Fetch Cat Data");
        fetchButton.setOnAction(event -> fetchCatData());

        VBox root = new VBox(10, fetchButton);
        Scene scene = new Scene(root, 300, 200);

        primaryStage.setTitle("Cats API Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void fetchCatData() {
        try {
            // Set up the connection
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("accept", "application/json");
            connection.setRequestProperty("X-Api-Key", API_KEY);

            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            // Parse and print JSON response
            parseAndPrintResponse(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseAndPrintResponse(String jsonResponse) {
        try {
            JSONParser parser = new JSONParser();
            JSONArray catArray = (JSONArray) parser.parse(jsonResponse);

            for (Object obj : catArray) {
                JSONObject cat = (JSONObject) obj;
                System.out.println("Name: " + cat.get("name"));
                System.out.println("Origin: " + cat.get("origin"));
                System.out.println("Length: " + cat.get("length"));
                System.out.println("Image: " + cat.get("image_link"));
                System.out.println("---------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
