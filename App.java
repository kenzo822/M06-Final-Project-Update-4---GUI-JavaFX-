import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class App extends Application {

    private boolean loggedIn = false;
    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private Map<String, String> registeredUsers = new HashMap<>();
    private ComboBox<String> gameComboBox = new ComboBox<>();
    private ComboBox<String> seatComboBox = new ComboBox<>();
    private ComboBox<Integer> ticketComboBox = new ComboBox<>();
    private static final String USER_FILE_PATH = "registered_users.txt";

    @Override
    public void start(Stage primaryStage) {
        loadRegisteredUsers();
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        Scene scene = new Scene(root, 400, 300);

        displayLogin(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Baseball Ticket App");
        primaryStage.show();
    }

    private void displayLogin(VBox root) {
        Label titleLabel = new Label("Welcome to Baseball Ticket App");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");

        loginButton.setOnAction(event -> login(root));
        registerButton.setOnAction(event -> register());

        root.getChildren().addAll(titleLabel, usernameLabel, usernameField, passwordLabel, passwordField,
                loginButton, registerButton);
        root.setAlignment(Pos.CENTER);
    }

    private void login(VBox root) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (registeredUsers.containsKey(username) && registeredUsers.get(username).equals(password)) {
            loggedIn = true;
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome back, " + username + "!");
            usernameField.clear();
            passwordField.clear();
            root.getChildren().clear();
            displayTicketMenu(root);
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    private void displayTicketMenu(VBox root) {
        Label titleLabel = new Label("Ticket Menu");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        gameComboBox.getItems().addAll("Atlanta Braves vs Chicago Cubs - May 9, 2024, 3:30 PM",
                "Atlanta Braves vs Chicago Cubs - May 10, 2024, 3:30 PM");
        gameComboBox.setPromptText("Select a game");

        seatComboBox.getItems().addAll("Outfield", "First Base Side", "Third Base Side");
        seatComboBox.setPromptText("Select seat area");

        ticketComboBox.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8);
        ticketComboBox.setPromptText("Select number of tickets");

        Button purchaseButton = new Button("Purchase Tickets");
        purchaseButton.setOnAction(event -> purchaseTickets());

        VBox ticketMenu = new VBox(10);
        ticketMenu.getChildren().addAll(titleLabel, gameComboBox, seatComboBox, ticketComboBox, purchaseButton);
        ticketMenu.setAlignment(Pos.CENTER);

        root.getChildren().add(ticketMenu);
    }

    private void purchaseTickets() {
        // Implement purchase logic here
        showAlert(Alert.AlertType.INFORMATION, "Ticket Purchase", "Tickets successfully purchased!");
    }

    private void register() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (!isValidUsername(username)) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Invalid username.");
        } else if (password.length() < 5) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Password must be at least 5 characters.");
        } else if (!registeredUsers.containsKey(username)) {
            registeredUsers.put(username, password);
            saveRegisteredUsers();
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "You have successfully registered!");
            usernameField.clear();
            passwordField.clear();
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Username already exists.");
        }
    }

    private boolean isValidUsername(String username) {
        return username.length() >= 4;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadRegisteredUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                registeredUsers.put(parts[0], parts[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveRegisteredUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE_PATH))) {
            for (Map.Entry<String, String> entry : registeredUsers.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}