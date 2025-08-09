package com.mycompany.sportsperformanceanalysis;

import com.datastax.oss.driver.api.core.cql.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginPage extends Application {

    private Stage primaryStage;
    private Scene loginScene;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        SportsPerformanceAnalysis.connect(); // Connect to Cassandra
        showLoginPage();
    }

    private void showLoginPage() {
        primaryStage.setTitle("Sports Performance Analysis");

        // Left Panel (Welcome Section)
        VBox welcomeBox = new VBox(20);
        welcomeBox.setPadding(new Insets(50));
        welcomeBox.setAlignment(Pos.CENTER);
        welcomeBox.setStyle("-fx-background-color: #2C3E50; -fx-pref-width: 300px;");

        Label welcomeTitle = new Label("Welcome!");
        welcomeTitle.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        welcomeTitle.setTextFill(Color.WHITE);

        Label welcomeText = new Label("Analyze sports performance with detailed insights.\nLogin to continue.");
        welcomeText.setFont(Font.font("Arial", 14));
        welcomeText.setTextFill(Color.LIGHTGRAY);
        welcomeText.setWrapText(true);
        welcomeText.setMaxWidth(250);

        welcomeBox.getChildren().addAll(welcomeTitle, welcomeText);

        // Right Panel (Login Form)
        VBox loginBox = new VBox(10);
        loginBox.setPadding(new Insets(50));
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setStyle("-fx-background-color: #ECF0F1; -fx-pref-width: 400px;");

        Label loginLabel = new Label("Login");
        loginLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(250); // Set width to 250px

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(250); // Now same width as username field

        // Toggle Show Password
        CheckBox showPasswordCheckBox = new CheckBox("Show Password");
        TextField passwordTextField = new TextField();
        passwordTextField.setMaxWidth(250); // Same width as password field
        passwordTextField.setManaged(false);
        passwordTextField.setVisible(false);

        showPasswordCheckBox.setOnAction(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordTextField.setText(passwordField.getText());
                passwordTextField.setVisible(true);
                passwordTextField.setManaged(true);
                passwordField.setVisible(false);
                passwordField.setManaged(false);
            } else {
                passwordField.setText(passwordTextField.getText());
                passwordField.setVisible(true);
                passwordField.setManaged(true);
                passwordTextField.setVisible(false);
                passwordTextField.setManaged(false);
            }
        });

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #2980B9; -fx-text-fill: white;");
        loginButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Button registerButton = new Button("Register");
        registerButton.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white;");
        registerButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label messageLabel = new Label();
        messageLabel.setTextFill(Color.RED);
        messageLabel.setVisible(false);

        // Login Button Action
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = showPasswordCheckBox.isSelected() ? passwordTextField.getText().trim() : passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("❌ Username and password required!");
                messageLabel.setVisible(true);
                return;
            }

            if (authenticate(username, password)) {
                messageLabel.setText("✅ Login successful!");
                messageLabel.setTextFill(Color.GREEN);
                messageLabel.setVisible(true);
                new Dashboard().start(new Stage());  // Open Dashboard
                primaryStage.close(); // Close login window
            } else {
                messageLabel.setText("❌ Invalid credentials. Try again.");
                messageLabel.setVisible(true);
            }
        });

        registerButton.setOnAction(e -> showRegistrationPage());

        loginBox.getChildren().addAll(loginLabel, usernameField, passwordField, passwordTextField, showPasswordCheckBox, loginButton, registerButton, messageLabel);

        // Combine Both Panels
        HBox rootLayout = new HBox();
        rootLayout.getChildren().addAll(welcomeBox, loginBox);

        loginScene = new Scene(rootLayout, 700, 400);
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    // Show Registration Page
    private void showRegistrationPage() {
        Stage registerStage = new Stage();
        registerStage.setTitle("Register New User");

        VBox registerBox = new VBox(10);
        registerBox.setPadding(new Insets(50));
        registerBox.setAlignment(Pos.CENTER);
        registerBox.setStyle("-fx-background-color: #ECF0F1; -fx-pref-width: 400px;");

        Label registerLabel = new Label("Register New User");
        registerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        TextField newUsernameField = new TextField();
        newUsernameField.setPromptText("New Username");
        newUsernameField.setMaxWidth(250);

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("New Password");
        newPasswordField.setMaxWidth(250);

        Button registerSubmitButton = new Button("Register");
        registerSubmitButton.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white;");
        registerSubmitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label registerMessage = new Label();
        registerMessage.setTextFill(Color.RED);
        registerMessage.setVisible(false);

        registerSubmitButton.setOnAction(e -> {
            String newUsername = newUsernameField.getText().trim();
            String newPassword = newPasswordField.getText().trim();

            if (newUsername.isEmpty() || newPassword.isEmpty()) {
                registerMessage.setText("❌ Username and password required!");
                registerMessage.setVisible(true);
            } else if (registerUser(newUsername, newPassword)) {
                registerMessage.setText("✅ Registration successful! Returning to login.");
                registerMessage.setTextFill(Color.GREEN);
                registerMessage.setVisible(true);
                registerStage.close();
                showLoginPage();
            } else {
                registerMessage.setText("❌ Registration failed! Try a different username.");
                registerMessage.setVisible(true);
            }
        });

        registerBox.getChildren().addAll(registerLabel, newUsernameField, newPasswordField, registerSubmitButton, registerMessage);

        Scene registerScene = new Scene(registerBox, 400, 300);
        registerStage.setScene(registerScene);
        registerStage.show();
    }

    // Authenticate User
    private boolean authenticate(String username, String password) {
        try {
            String query = "SELECT password FROM users WHERE username = ?";
            PreparedStatement statement = SportsPerformanceAnalysis.getSession().prepare(query);
            BoundStatement boundStatement = statement.bind(username);
            ResultSet resultSet = SportsPerformanceAnalysis.getSession().execute(boundStatement);

            Row row = resultSet.one();
            return row != null && password.equals(row.getString("password"));
        } catch (Exception e) {
            System.err.println("❌ Authentication error: " + e.getMessage());
        }
        return false;
    }

    // Register User
    private boolean registerUser(String username, String password) {
        try {
            String checkQuery = "SELECT username FROM users WHERE username = ?";
            PreparedStatement checkStatement = SportsPerformanceAnalysis.getSession().prepare(checkQuery);
            BoundStatement checkBoundStatement = checkStatement.bind(username);
            ResultSet resultSet = SportsPerformanceAnalysis.getSession().execute(checkBoundStatement);

            if (resultSet.one() != null) return false;

            String insertQuery = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement insertStatement = SportsPerformanceAnalysis.getSession().prepare(insertQuery);
            BoundStatement insertBoundStatement = insertStatement.bind(username, password);
            SportsPerformanceAnalysis.getSession().execute(insertBoundStatement);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Registration error: " + e.getMessage());
        }
        return false;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
