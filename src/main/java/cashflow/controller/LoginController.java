package cashflow.controller;

import cashflow.util.SessionManager;
import database.DatabaseInitializer;
import database.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.User;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private TextField     usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label         errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DatabaseInitializer.initialize();
        UserDAO.createTable();
        errorLabel.setVisible(false);
    }

    @FXML
    public void handleLogin() {
        String input    = usernameField.getText().trim();
        String password = passwordField.getText();

        if (input.isEmpty() || password.isEmpty()) {
            showError("Username/email dan password wajib diisi.");
            return;
        }

        User user = UserDAO.login(input, password);

        if (user == null) {
            showError("Username/email atau password salah.");
            return;
        }

        SessionManager.getInstance().setCurrentUser(user);
        openDashboard();
    }

    @FXML
    public void handleGoToSignUp() {
        openWindow("/cashflow/fxml/signup-view.fxml", "Daftar Akun");
        closeCurrentWindow();
    }

    private void openDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/cashflow/fxml/dashboard-view.fxml")
            );
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Cashflow Dashboard — " +
                    SessionManager.getInstance().getCurrentUser().getUserName());
            stage.setMinWidth(1000);
            stage.setMinHeight(650);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource("/cashflow/css/style.css").toExternalForm()
            );
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
            closeCurrentWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openWindow(String path, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource("/cashflow/css/style.css").toExternalForm()
            );
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeCurrentWindow() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
    }
}