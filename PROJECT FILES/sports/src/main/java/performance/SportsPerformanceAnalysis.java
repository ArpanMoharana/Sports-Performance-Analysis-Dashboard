package  main.java.performance;

import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Random;

public class SportsPerformanceAnalysis {

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sports Performance Analysis");

        // Create performance chart (Bar chart for simplicity)
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Player Performance");

        // Random data for example purposes
        series.getData().add(new XYChart.Data<>("Match 1", new Random().nextInt(100)));
        series.getData().add(new XYChart.Data<>("Match 2", new Random().nextInt(100)));
        series.getData().add(new XYChart.Data<>("Match 3", new Random().nextInt(100)));
        barChart.getData().add(series);

        // Create a back button
        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> {
            // Go back to DashboardPage
            DashboardPage dashboardPage = new DashboardPage();
            dashboardPage.start(primaryStage);
        });

        // Create layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(barChart, backButton);
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
