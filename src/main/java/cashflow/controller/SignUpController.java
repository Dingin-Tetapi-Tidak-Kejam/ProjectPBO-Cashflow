package cashflow.controller;

import database.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    @FXML private TextField     usernameField;
    @FXML private TextField     emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label         errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorLabel.setVisible(false);
    }

    @FXML
    public void handleSignUp() {
        String username  = usernameField.getText().trim();
        String email     = emailField.getText().trim();
        String password  = passwordField.getText();
        String confirm   = confirmPasswordField.getText();

        // ── Validasi ──────────────────────────────────────────────────────────
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            showError("Semua field wajib diisi.");
            return;
        }

        if (username.length() < 3) {
            showError("Username minimal 3 karakter.");
            return;
        }

        if (!email.matches("^[\\w.+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$")) {
            showError("Format email tidak valid.");
            return;
        }

        if (password.length() < 6) {
            showError("Password minimal 6 karakter.");
            return;
        }

        if (!password.equals(confirm)) {
            showError("Password dan konfirmasi tidak cocok.");
            return;
        }

        if (UserDAO.usernameExists(username)) {
            showError("Username sudah digunakan.");
            return;
        }

        if (UserDAO.emailExists(email)) {
            showError("Email sudah terdaftar.");
            return;
        }

        // ── Simpan ────────────────────────────────────────────────────────────
        boolean success = UserDAO.register(username, email, password);

        if (success) {
            new Alert(Alert.AlertType.INFORMATION,
                    "Akun berhasil dibuat! Silakan login.", ButtonType.OK).showAndWait();
            goToLogin();
        } else {
            showError("Registrasi gagal. Coba lagi.");
        }
    }

    @FXML
    public void handleGoToLogin() {
        goToLogin();
    }

    private void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/cashflow/fxml/login-view.fxml")
            );
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Login");
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource("/cashflow/css/style.css").toExternalForm()
            );
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();

            // Tutup window sign up
            Stage current = (Stage) usernameField.getScene().getWindow();
            current.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
    }
}