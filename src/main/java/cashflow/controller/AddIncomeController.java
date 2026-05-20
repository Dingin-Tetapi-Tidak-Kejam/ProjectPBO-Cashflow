package cashflow.controller;

import database.TransactionDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Income;
import model.IncomeSource;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddIncomeController implements Initializable {

    @FXML private TextField  amountField;
    @FXML private TextField  descriptionField;
    @FXML private ComboBox<IncomeSource> sourceComboBox;
    @FXML private DatePicker datePicker;

    private DashboardController dashboardController;

    public void setDashboardController(DashboardController dc) {
        this.dashboardController = dc;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sourceComboBox.setItems(
                FXCollections.observableArrayList(IncomeSource.values())
        );
        datePicker.setValue(LocalDate.now());
    }

    @FXML
    public void handleSaveIncome() {
        // --- Validasi ---
        String amountText = amountField.getText().trim();
        if (amountText.isEmpty()) {
            showAlert("Amount tidak boleh kosong!");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText.replace(",", "").replace(".", ""));
        } catch (NumberFormatException e) {
            showAlert("Amount harus berupa angka!");
            return;
        }

        if (amount <= 0) {
            showAlert("Amount harus lebih dari 0!");
            return;
        }

        if (sourceComboBox.getValue() == null) {
            showAlert("Pilih Income Source!");
            return;
        }

        if (datePicker.getValue() == null) {
            showAlert("Pilih tanggal!");
            return;
        }

        // --- Simpan ---
        Income income = new Income(
                amount,
                datePicker.getValue(),
                descriptionField.getText().trim(),
                sourceComboBox.getValue()
        );

        TransactionDAO.save(income);

        // Refresh dashboard
        if (dashboardController != null) {
            dashboardController.refreshDashboard();
        }

        showInfo("Income berhasil disimpan!");
        closeWindow();
    }

    @FXML
    public void handleReset() {
        amountField.clear();
        descriptionField.clear();
        sourceComboBox.setValue(null);
        datePicker.setValue(LocalDate.now());
    }

    private void closeWindow() {
        Stage stage = (Stage) amountField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String msg) {
        new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK).showAndWait();
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}