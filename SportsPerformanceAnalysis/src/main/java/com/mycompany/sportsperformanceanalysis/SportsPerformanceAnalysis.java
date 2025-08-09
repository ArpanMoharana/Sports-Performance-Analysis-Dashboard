package com.mycompany.sportsperformanceanalysis;

import com.datastax.oss.driver.api.core.CqlSession;
import java.net.InetSocketAddress;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SportsPerformanceAnalysis extends Application {

    private static CqlSession session;

    public static void connect() {
        try {
            session = CqlSession.builder()
                    .addContactPoint(new InetSocketAddress("127.0.0.1", 9042))
                    .withLocalDatacenter("datacenter1")
                    .withKeyspace("sports_analysis")
                    .build();
            System.out.println("✅ Connected to Cassandra inside Docker!");
        } catch (Exception e) {
            System.err.println("❌ Failed to connect to Cassandra: " + e.getMessage());
        }
    }

    public static CqlSession getSession() {
        return session;
    }

    public static void close() {
        if (session != null) {
            session.close();
            System.out.println("🔄 Cassandra connection closed.");
        }
    }

    @Override
    public void start(Stage primaryStage) {
        connect(); // Initialize Cassandra
        Label label = new Label("Sports Performance Analysis App");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 600, 400);

        primaryStage.setTitle("Sports Performance Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Launch JavaFX
    }
}
