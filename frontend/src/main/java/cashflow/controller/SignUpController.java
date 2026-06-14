package cashflow.controller;

import cashflow.dto.AuthResponse;
import cashflow.service.api.ApiClientService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;

    private final ApiClientService apiClientService = new ApiClientService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorLabel.setVisible(false);
    }

    @FXML
    public void handleSignUp() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
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

        if (!password.equals(confirmPassword)) {
            showError("Password dan konfirmasi password tidak sama.");
            return;
        }

        try {
            AuthResponse response = apiClientService.register(username, email, password);

            if (response == null) {
                showError("Registrasi gagal.");
                return;
            }

            if (!response.isSuccess()) {
                showError(response.getMessage() != null ? response.getMessage() : "Registrasi gagal.");
                return;
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registrasi Berhasil");
            alert.setHeaderText(null);
            alert.setContentText("Akun berhasil dibuat! Silakan login.");
            alert.showAndWait();

            goToLogin();

        } catch (IOException e) {
            e.printStackTrace();
            showError("Tidak bisa terhubung ke backend. Pastikan backend berjalan.");
        } catch (InterruptedException e) {
            e.printStackTrace();
            showError("Proses registrasi terganggu.");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Terjadi kesalahan saat registrasi.");
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
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();

            Stage current = (Stage) usernameField.getScene().getWindow();
            current.close();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Gagal membuka halaman login.");
        }
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
    }
}