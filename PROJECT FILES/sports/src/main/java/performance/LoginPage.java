package  main.java.performance;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginPage extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sports Performance Analysis");

        // Left Side - Welcome Panel
        VBox welcomeBox = new VBox(20);
        welcomeBox.setPadding(new Insets(50));
        welcomeBox.setAlignment(Pos.CENTER);
        welcomeBox.setStyle("-fx-background-color: #2C3E50; -fx-pref-width: 300px;"); // Dark theme left side

        Label welcomeTitle = new Label("Welcome!");
        welcomeTitle.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        welcomeTitle.setTextFill(Color.WHITE);

        Label welcomeText = new Label("Analyze sports performance with detailed insights.\nLogin to continue.");
        welcomeText.setFont(Font.font("Arial", 14));
        welcomeText.setTextFill(Color.LIGHTGRAY);
        welcomeText.setWrapText(true);
        welcomeText.setMaxWidth(250);

        welcomeBox.getChildren().addAll(welcomeTitle, welcomeText);

        // Right Side - Login Form
        VBox loginBox = new VBox(10);
        loginBox.setPadding(new Insets(50));
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setStyle("-fx-background-color: #ECF0F1; -fx-pref-width: 400px;"); // Light theme right side

        Label loginLabel = new Label("Login");
        loginLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        loginLabel.setTextFill(Color.BLACK);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(250);

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #2980B9; -fx-text-fill: white;");
        loginButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label errorMessage = new Label();
        errorMessage.setTextFill(Color.RED);
        errorMessage.setVisible(false);

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (authenticate(username, password)) {
                new DashboardPage().start(primaryStage);
            } else {
                errorMessage.setText("Invalid credentials. Try again.");
                errorMessage.setVisible(true);
            }
        });

        loginBox.getChildren().addAll(loginLabel, usernameField, passwordField, loginButton, errorMessage);

        // Combine Both Panels
        HBox rootLayout = new HBox();
        rootLayout.getChildren().addAll(welcomeBox, loginBox);

        Scene scene = new Scene(rootLayout, 700, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Dummy authentication (Replace with real authentication logic)
    private boolean authenticate(String username, String password) {
        return username.equals("admin") && password.equals("password");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
