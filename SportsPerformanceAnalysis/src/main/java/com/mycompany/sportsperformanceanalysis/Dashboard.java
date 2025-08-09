package com.mycompany.sportsperformanceanalysis;

import com.datastax.oss.driver.api.core.cql.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.Node;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import java.io.File;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.HashSet;
import java.util.Set;

public class Dashboard extends Application {

    private Stage primaryStage;
    private Scene inputScene, graphScene, historyScene;
    private ComboBox<String> sportComboBox, durationComboBox, comparePlayerComboBox;
    private TextField matchesField, runsField, assistsField, wicketsField, playerNameField;
    private Button nextButton, viewHistoryButton, showResultsButton, exportButton;
    private BarChart<String, Number> barChart;
    private PieChart pieChart;
    private CategoryAxis xAxis;
    private NumberAxis yAxis;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        SportsPerformanceAnalysis.connect();
        setupInputScene();
        primaryStage.setTitle("🏆 Sports Performance Analysis");
        primaryStage.setScene(inputScene);
        primaryStage.show();
    }

    private void setupInputScene() {
        Label titleLabel = new Label("🏅 Enter Player Performance Data");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        sportComboBox = new ComboBox<>();
        sportComboBox.getItems().addAll("Football", "Basketball", "Tennis", "Cricket");
        sportComboBox.setPromptText("Select a Sport");
        sportComboBox.setOnAction(e -> updatePlaceholders());

        durationComboBox = new ComboBox<>();
        durationComboBox.getItems().addAll("Last 1 Year", "Last 5 Years", "Last 10 Years", "Career Stats");
        durationComboBox.setPromptText("Select Duration");

        playerNameField = createStyledTextField("Player Name");
        matchesField = createStyledTextField("Total Matches Played");
        runsField = createStyledTextField("Total Runs Scored");
        assistsField = createStyledTextField("Total Assists");
        wicketsField = createStyledTextField("Total Wickets Taken");

        nextButton = createStyledButton("Save & Analyze", "#3498db");
        nextButton.setOnAction(e -> {
            savePlayerData();
            switchToGraphScene();
        });

        viewHistoryButton = createStyledButton("View History", "#f1c40f");
        viewHistoryButton.setOnAction(e -> displayHistory());

        VBox formLayout = new VBox(12, sportComboBox, durationComboBox, playerNameField, matchesField, runsField, assistsField, wicketsField);
        formLayout.setAlignment(Pos.CENTER);
        formLayout.setPadding(new Insets(15));
        formLayout.setStyle("-fx-background-color: white; -fx-padding: 20px; -fx-border-radius: 10px;");
        formLayout.setEffect(new DropShadow(10, Color.GRAY));

        HBox buttonLayout = new HBox(20, nextButton, viewHistoryButton);
        buttonLayout.setAlignment(Pos.CENTER);

        VBox mainLayout = new VBox(15, titleLabel, formLayout, buttonLayout);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(30));

        inputScene = new Scene(mainLayout, 700, 500);
    }

    private void updatePlaceholders() {
        String selectedSport = sportComboBox.getValue();
        if (selectedSport == null) return;

        switch (selectedSport) {
            case "Football":
                runsField.setPromptText("Goals Scored");
                assistsField.setPromptText("Assists");
                wicketsField.setPromptText("Red Cards");
                break;
            case "Basketball":
                runsField.setPromptText("Points Scored");
                assistsField.setPromptText("Assists");
                wicketsField.setPromptText("Rebounds");
                break;
            case "Tennis":
                runsField.setPromptText("Matches Won");
                assistsField.setPromptText("Aces");
                wicketsField.setPromptText("Double Faults");
                break;
            case "Cricket":
                runsField.setPromptText("Total Runs");
                assistsField.setPromptText("Catches Taken");
                wicketsField.setPromptText("Wickets Taken");
                break;
        }
    }

    private void savePlayerData() {
        try {
            String playerName = playerNameField.getText();
            String sport = sportComboBox.getValue();
            int matches = Integer.parseInt(matchesField.getText());
            int runs = Integer.parseInt(runsField.getText());
            int assists = Integer.parseInt(assistsField.getText());
            int wickets = Integer.parseInt(wicketsField.getText());

            String query = "INSERT INTO sports_analysis.player_performance (player_id, player_name, sport, matches_played, runs_scored, assists, wickets) VALUES (uuid(), ?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = SportsPerformanceAnalysis.getSession().prepare(query);
            BoundStatement boundStatement = statement.bind(playerName, sport, matches, runs, assists, wickets);
            SportsPerformanceAnalysis.getSession().execute(boundStatement);
            System.out.println("✅ Player data inserted!");
        } catch (Exception e) {
            showAlert("Error", "Failed to save player data: " + e.getMessage());
        }
    }

    private void switchToGraphScene() {
        setupGraphScene();
        primaryStage.setScene(graphScene);
    }

    private void setupGraphScene() {
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        xAxis.setLabel("Performance Category");
        yAxis.setLabel("Value");
        
        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Player Performance Metrics");
        barChart.setAnimated(true);
        barChart.setLegendVisible(true);
        barChart.setLegendSide(Side.RIGHT);
        barChart.setCategoryGap(20);
        
        pieChart = new PieChart();
        pieChart.setTitle("Performance Distribution (Primary Player)");
        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(true);
        pieChart.setClockwise(true);
        pieChart.setLabelLineLength(10);
        
        exportButton = createStyledButton("Export as PNG", "#9b59b6");
        exportButton.setOnAction(e -> exportChartAsImage());
        
        showResultsButton = createStyledButton("Show Performance Analysis", "#2ecc71");
        showResultsButton.setOnAction(e -> displayGraphs());

        // Add comparison ComboBox
        comparePlayerComboBox = new ComboBox<>();
        comparePlayerComboBox.setPromptText("Select Player to Compare");
        updateComparePlayerList(); // Populate with existing players
        
        VBox chartContainer = new VBox(20);
        chartContainer.setAlignment(Pos.CENTER);
        chartContainer.getChildren().addAll(barChart, pieChart);
        
        HBox buttonBox = new HBox(20, showResultsButton, exportButton, comparePlayerComboBox);
        buttonBox.setAlignment(Pos.CENTER);
        
        VBox layout = new VBox(20, buttonBox, chartContainer);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f9f9f9;");
        
        graphScene = new Scene(layout, 800, 600);
    }

    private void updateComparePlayerList() {
        try {
            // Check if session is available
            if (SportsPerformanceAnalysis.getSession() == null || SportsPerformanceAnalysis.getSession().isClosed()) {
                showAlert("Error", "Database connection is not available");
                return;
            }

            // Fetch all player names and handle distinct in Java
            ResultSet resultSet = SportsPerformanceAnalysis.getSession().execute(
                "SELECT player_name FROM sports_analysis.player_performance");
            
            comparePlayerComboBox.getItems().clear();
            Set<String> uniquePlayers = new HashSet<>(); // Use Set to ensure uniqueness
            String primaryPlayer = playerNameField.getText();

            for (Row row : resultSet) {
                String playerName = row.getString("player_name");
                if (playerName != null && !playerName.isEmpty() && !playerName.equals(primaryPlayer)) {
                    uniquePlayers.add(playerName);
                }
            }

            if (uniquePlayers.isEmpty()) {
                showAlert("Info", "No other players found in the database to compare");
            } else {
                comparePlayerComboBox.getItems().addAll(uniquePlayers);
                System.out.println("Players loaded for comparison: " + uniquePlayers);
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load player list: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void displayGraphs() {
        try {
            String primaryPlayer = playerNameField.getText();
            String comparePlayer = comparePlayerComboBox.getValue();
            
            barChart.getData().clear();
            pieChart.getData().clear();
            
            String[] barColors = {"#3498db", "#2ecc71", "#e74c3c", "#f39c12"};
            String[] pieColors = {"#3498db", "#2ecc71", "#e74c3c", "#f39c12", "#9b59b6"};
            
            // Display primary player data
            displayPlayerData(primaryPlayer, barColors, 0, true);
            
            // Display comparison player data if selected
            if (comparePlayer != null && !comparePlayer.isEmpty()) {
                displayPlayerData(comparePlayer, barColors, 2, false);
            }
            
        } catch (Exception e) {
            showAlert("Error", "Failed to load performance data: " + e.getMessage());
        }
    }

    private void displayPlayerData(String playerName, String[] barColors, int colorOffset, boolean updatePieChart) {
        String query = "SELECT * FROM sports_analysis.player_performance WHERE player_name = ? ALLOW FILTERING";
        ResultSet resultSet = SportsPerformanceAnalysis.getSession().execute(
            SportsPerformanceAnalysis.getSession().prepare(query).bind(playerName));
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(playerName);
        
        int colorIndex = colorOffset;
        for (Row row : resultSet) {
            String[] categories = {"Matches", "Runs", "Assists", "Wickets"};
            int[] values = {
                row.getInt("matches_played"),
                row.getInt("runs_scored"),
                row.getInt("assists"),
                row.getInt("wickets")
            };
            
            for (int i = 0; i < categories.length; i++) {
                final int finalIndex = colorIndex;
                final String finalCategory = categories[i];
                final int finalValue = values[i];
                
                XYChart.Data<String, Number> data = new XYChart.Data<>(finalCategory, finalValue);
                series.getData().add(data);
                
                data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                    if (newNode != null) {
                        newNode.setStyle("-fx-bar-fill: " + barColors[finalIndex % barColors.length] + ";");
                        Tooltip.install(newNode, new Tooltip(finalCategory + ": " + finalValue));
                    }
                });
                colorIndex++;
            }
            
            if (updatePieChart) {
                pieChart.getData().addAll(
                    new PieChart.Data("Matches (" + values[0] + ")", values[0]),
                    new PieChart.Data("Runs (" + values[1] + ")", values[1]),
                    new PieChart.Data("Assists (" + values[2] + ")", values[2]),
                    new PieChart.Data("Wickets (" + values[3] + ")", values[3])
                );
                
                colorIndex = 0;
                for (PieChart.Data data : pieChart.getData()) {
                    data.getNode().setStyle("-fx-pie-color: " + barColors[colorIndex % barColors.length] + ";");
                    colorIndex++;
                }
            }
        }
        
        barChart.getData().add(series);
    }
    
    private void exportChartAsImage() {
        try {
            WritableImage image = new WritableImage(800, 600);
            graphScene.snapshot(image);
            
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Chart Image");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"));
            File file = fileChooser.showSaveDialog(primaryStage);
            
            if (file != null) {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                showAlert("Success", "Chart exported successfully to:\n" + file.getAbsolutePath());
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to export chart: " + e.getMessage());
        }
    }

    private void displayHistory() {
        Stage historyStage = new Stage();
        historyStage.setTitle("📜 Player History");

        ListView<String> historyList = new ListView<>();
        ResultSet resultSet = SportsPerformanceAnalysis.getSession().execute(
            "SELECT player_name, sport, matches_played, runs_scored FROM sports_analysis.player_performance");

        for (Row row : resultSet) {
            historyList.getItems().add(row.getString("player_name") + " - " + 
                row.getString("sport") + " | Matches: " + 
                row.getInt("matches_played") + " | Runs: " + 
                row.getInt("runs_scored"));
        }

        Scene scene = new Scene(historyList, 400, 300);
        historyStage.setScene(scene);
        historyStage.show();
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; " +
                       "-fx-text-fill: white; " +
                       "-fx-font-weight: bold; " +
                       "-fx-padding: 8 15;");
        return button;
    }

    private TextField createStyledTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setStyle("-fx-padding: 8px; -fx-font-size: 14px;");
        return textField;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}