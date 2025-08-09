package  main.java.performance;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class DashboardPage extends Application {

    private Stage primaryStage;
    private Scene inputScene, graphScene;

    private ComboBox<String> sportComboBox, timeFrameBox, playerDurationBox;
    private TextField matchesField, runsField, assistsField, wicketsField, playerNameField, customTimeFrameField;
    private Button nextButton, backButton, showResultsButton;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        setupInputScene();
        primaryStage.setTitle("Sports Performance Analysis");
        primaryStage.setScene(inputScene);
        primaryStage.show();
    }

    private void setupInputScene() {
        // Sport selection dropdown
        sportComboBox = new ComboBox<>();
        sportComboBox.getItems().addAll("Football", "Basketball", "Tennis", "Cricket");
        sportComboBox.setPromptText("Select a Sport");

        // Timeframe dropdown with an option for custom input
        timeFrameBox = new ComboBox<>();
        timeFrameBox.getItems().addAll("1 Year", "5 Years", "10 Years", "Career Stats", "Custom");
        timeFrameBox.setPromptText("Select Time Frame");
        customTimeFrameField = new TextField();
        customTimeFrameField.setPromptText("Enter Custom Time Frame (in months or years)");
        customTimeFrameField.setVisible(false); // Initially hidden

        // Show custom input field when "Custom" is selected
        timeFrameBox.setOnAction(e -> {
            if (timeFrameBox.getValue().equals("Custom")) {
                customTimeFrameField.setVisible(true);
            } else {
                customTimeFrameField.setVisible(false);
            }
        });

        // Player Duration dropdown (in months or years)
        playerDurationBox = new ComboBox<>();
        playerDurationBox.getItems().addAll("Months", "Years");
        playerDurationBox.setPromptText("Select Player's Duration");

        // Input fields
        playerNameField = new TextField();
        playerNameField.setPromptText("Player Name");

        matchesField = new TextField();
        matchesField.setPromptText("Total Matches Played");

        runsField = new TextField();
        runsField.setPromptText("Total Runs Scored (if applicable)");

        assistsField = new TextField();
        assistsField.setPromptText("Total Assists (if applicable)");

        wicketsField = new TextField();
        wicketsField.setPromptText("Total Wickets Taken (if applicable)");

        // Navigation buttons
        nextButton = new Button("Next");
        nextButton.setOnAction(e -> switchToGraphScene());

        backButton = new Button("Back");
        backButton.setOnAction(e -> primaryStage.setScene(inputScene));

        // Layout with headings for input fields
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        // Adding headers for each input field
        layout.getChildren().addAll(
                createHeading("Select Sport"),
                sportComboBox,
                createHeading("Select Time Frame"),
                timeFrameBox,
                customTimeFrameField,
                createHeading("Enter Player's Duration"),
                playerDurationBox,
                createHeading("Enter Player's Name"),
                playerNameField,
                createHeading("Total Matches Played"),
                matchesField,
                createHeading("Total Runs Scored (if applicable)"),
                runsField,
                createHeading("Total Assists (if applicable)"),
                assistsField,
                createHeading("Total Wickets Taken (if applicable)"),
                wicketsField
        );

        // Adding the Back and Next buttons on opposite sides
        HBox buttonLayout = new HBox(20);
        buttonLayout.setPadding(new Insets(20));
        buttonLayout.getChildren().addAll(backButton, nextButton);
        layout.getChildren().add(buttonLayout);

        inputScene = new Scene(layout, 500, 600);
    }

    // Method to switch to the graph scene
    private void switchToGraphScene() {
        setupGraphScene();
        primaryStage.setScene(graphScene);
    }

    private void setupGraphScene() {
        // Show results button
        showResultsButton = new Button("Show Performance Analysis");
        showResultsButton.setOnAction(e -> displayGraphs());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().add(showResultsButton);

        // Back button for graph scene
        Button backToInputButton = new Button("Back");
        backToInputButton.setOnAction(e -> primaryStage.setScene(inputScene));
        layout.getChildren().add(backToInputButton);

        graphScene = new Scene(layout, 500, 400);
    }

    private void displayGraphs() {
        Stage graphStage = new Stage();
        graphStage.setTitle("Performance Analysis");

        TabPane tabPane = new TabPane();

        // Generate 5 different graphs
        tabPane.getTabs().add(new Tab("Bar Chart", createBarChart()));
        tabPane.getTabs().add(new Tab("Pie Chart", createPieChart()));
        tabPane.getTabs().add(new Tab("Line Chart", createLineChart()));
        tabPane.getTabs().add(new Tab("Area Chart", createAreaChart()));
        tabPane.getTabs().add(new Tab("Scatter Chart", createScatterChart()));

        Scene scene = new Scene(tabPane, 700, 500);
        graphStage.setScene(scene);
        graphStage.show();
    }

    private BarChart<String, Number> createBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(sportComboBox.getValue());
        series.getData().add(new XYChart.Data<>("Performance", 75));  // Example value
        barChart.getData().add(series);
        return barChart;
    }

    private PieChart createPieChart() {
        PieChart pieChart = new PieChart();
        pieChart.getData().add(new PieChart.Data("Matches", Integer.parseInt(matchesField.getText())));
        pieChart.getData().add(new PieChart.Data("Runs", Integer.parseInt(runsField.getText())));
        return pieChart;
    }

    private LineChart<String, Number> createLineChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Start", 0));
        series.getData().add(new XYChart.Data<>("Mid", 50));
        series.getData().add(new XYChart.Data<>("End", 100));
        lineChart.getData().add(series);
        return lineChart;
    }

    private AreaChart<String, Number> createAreaChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        AreaChart<String, Number> areaChart = new AreaChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Start", 10));
        series.getData().add(new XYChart.Data<>("Mid", 60));
        series.getData().add(new XYChart.Data<>("End", 100));
        areaChart.getData().add(series);
        return areaChart;
    }

    private ScatterChart<String, Number> createScatterChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        ScatterChart<String, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Point 1", 50));
        series.getData().add(new XYChart.Data<>("Point 2", 75));
        scatterChart.getData().add(series);
        return scatterChart;
    }

    private Label createHeading(String text) {
        Label heading = new Label(text);
        heading.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        return heading;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
