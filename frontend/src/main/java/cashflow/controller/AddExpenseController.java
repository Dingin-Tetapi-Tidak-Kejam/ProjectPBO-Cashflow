package cashflow.controller;

import cashflow.service.api.ApiClientService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Expense;
import model.ExpenseCategory;
import model.ExpenseSubCategory;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class AddExpenseController implements Initializable {

    @FXML private TextField amountField;
    @FXML private TextField descriptionField;
    @FXML private ComboBox<ExpenseCategory> categoryComboBox;
    @FXML private ComboBox<ExpenseSubCategory> subCategoryComboBox;
    @FXML private DatePicker datePicker;

    private DashboardController dashboardController;
    private final ApiClientService apiClientService = new ApiClientService();

    private static final Map<ExpenseCategory, List<ExpenseSubCategory>> SUB_MAP = Map.of(
            ExpenseCategory.MAKANAN, List.of(
                    ExpenseSubCategory.SARAPAN,
                    ExpenseSubCategory.MAKAN_SIANG,
                    ExpenseSubCategory.MAKAN_MALAM,
                    ExpenseSubCategory.JAJAN
            ),
            ExpenseCategory.TRANSPORT, List.of(
                    ExpenseSubCategory.BENSIN,
                    ExpenseSubCategory.GRAB,
                    ExpenseSubCategory.PARKIR
            ),
            ExpenseCategory.BELANJA, List.of(
                    ExpenseSubCategory.PAKAIAN,
                    ExpenseSubCategory.SKINCARE,
                    ExpenseSubCategory.ELEKTRONIK
            ),
            ExpenseCategory.HIBURAN, List.of(
                    ExpenseSubCategory.BIOSKOP,
                    ExpenseSubCategory.GAME,
                    ExpenseSubCategory.KONSER
            ),
            ExpenseCategory.LAINNYA, List.of(
                    ExpenseSubCategory.LAINNYA
            )
    );

    public void setDashboardController(DashboardController dc) {
        this.dashboardController = dc;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categoryComboBox.setItems(
                FXCollections.observableArrayList(ExpenseCategory.values())
        );
        datePicker.setValue(LocalDate.now());
        subCategoryComboBox.setDisable(true);
    }

    @FXML
    public void handleCategoryChange() {
        ExpenseCategory selected = categoryComboBox.getValue();
        if (selected != null) {
            List<ExpenseSubCategory> subs = SUB_MAP.getOrDefault(
                    selected, List.of(ExpenseSubCategory.LAINNYA)
            );
            subCategoryComboBox.setItems(FXCollections.observableArrayList(subs));
            subCategoryComboBox.setValue(null);
            subCategoryComboBox.setDisable(false);
        }
    }

    @FXML
    public void handleSaveExpense() {
        String amountText = amountField.getText().trim();
        if (amountText.isEmpty()) {
            showAlert("Amount tidak boleh kosong!");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText.replace(",", ""));
        } catch (NumberFormatException e) {
            showAlert("Amount harus berupa angka!");
            return;
        }

        if (amount <= 0) {
            showAlert("Amount harus lebih dari 0!");
            return;
        }

        if (categoryComboBox.getValue() == null) {
            showAlert("Pilih Category!");
            return;
        }

        if (subCategoryComboBox.getValue() == null) {
            showAlert("Pilih Sub Category!");
            return;
        }

        if (datePicker.getValue() == null) {
            showAlert("Pilih tanggal!");
            return;
        }

        try {
            Expense expense = new Expense(
                    amount,
                    datePicker.getValue(),
                    descriptionField.getText().trim(),
                    categoryComboBox.getValue(),
                    subCategoryComboBox.getValue()
            );

            apiClientService.createTransaction(
                    expense.getTransactionType(),
                    expense.getAmount(),
                    expense.getDate().toString(),
                    expense.getDescription(),
                    expense.getDetail()
            );

            if (dashboardController != null) {
                dashboardController.refreshDashboard();
            }

            showInfo("Expense berhasil disimpan!");
            closeWindow();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Gagal menyimpan expense ke backend.");
        }
    }

    @FXML
    public void handleReset() {
        amountField.clear();
        descriptionField.clear();
        categoryComboBox.setValue(null);
        subCategoryComboBox.setValue(null);
        subCategoryComboBox.setDisable(true);
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
