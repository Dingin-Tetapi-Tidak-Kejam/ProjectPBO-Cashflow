package cashflow.controller;

import cashflow.dto.SummaryResponse;
import cashflow.dto.TransactionResponse;
import cashflow.service.api.ApiClientService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.SessionManager;

import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private Label welcomeLabel;
    @FXML private Label incomeLabel;
    @FXML private Label expenseLabel;
    @FXML private Label balanceLabel;

    @FXML private TableView<TransactionResponse> transactionTable;
    @FXML private TableColumn<TransactionResponse, Integer> idColumn;
    @FXML private TableColumn<TransactionResponse, String> typeColumn;
    @FXML private TableColumn<TransactionResponse, String> amountColumn;
    @FXML private TableColumn<TransactionResponse, String> dateColumn;
    @FXML private TableColumn<TransactionResponse, String> descriptionColumn;
    @FXML private TableColumn<TransactionResponse, String> detailColumn;

    private final ApiClientService apiClientService = new ApiClientService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTable();

        if (SessionManager.getInstance().isLoggedIn()
                && SessionManager.getInstance().getCurrentUser() != null) {
            welcomeLabel.setText("Halo, " +
                    SessionManager.getInstance().getCurrentUser().getUserName() + "!");
        }

        refreshDashboard();
    }

    private void setupTable() {
        idColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(transactionTable.getItems().indexOf(cellData.getValue()) + 1)
        );

        typeColumn.setCellValueFactory(c ->
                new SimpleStringProperty(safe(c.getValue().getTransactionType()))
        );

        amountColumn.setCellValueFactory(c ->
                new SimpleStringProperty(formatRupiah(
                        c.getValue().getAmount() != null ? c.getValue().getAmount() : 0.0
                ))
        );

        dateColumn.setCellValueFactory(c ->
                new SimpleStringProperty(safe(c.getValue().getDate()))
        );

        descriptionColumn.setCellValueFactory(c ->
                new SimpleStringProperty(safe(c.getValue().getDescription()))
        );

        detailColumn.setCellValueFactory(c ->
                new SimpleStringProperty(safe(c.getValue().getDetail()))
        );
    }

    public void refreshDashboard() {
        try {
            SummaryResponse summary = apiClientService.getSummary();
            List<TransactionResponse> rows = apiClientService.getAllTransactions();

            System.out.println("ROWS TO TABLE: " + rows.size());

            incomeLabel.setText(formatRupiah(
                    summary != null && summary.getTotalIncome() != null ? summary.getTotalIncome() : 0.0
            ));
            expenseLabel.setText(formatRupiah(
                    summary != null && summary.getTotalExpense() != null ? summary.getTotalExpense() : 0.0
            ));
            balanceLabel.setText(formatRupiah(
                    summary != null && summary.getBalance() != null ? summary.getBalance() : 0.0
            ));

            transactionTable.setItems(FXCollections.observableArrayList(rows));
            transactionTable.refresh();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Gagal mengambil data dashboard dari backend.");
        }
    }

    @FXML
    public void handleOpenIncome() {
        openModal("/cashflow/fxml/add-income-view.fxml", "Tambah Income");
    }

    @FXML
    public void handleOpenExpense() {
        openModal("/cashflow/fxml/add-expense-view.fxml", "Tambah Expense");
    }

    @FXML
    public void handleOpenTransaction() {
        openModal("/cashflow/fxml/transaction-view.fxml", "Daftar Transaksi");
    }

    @FXML
    public void handleLogout() {
        try {
            apiClientService.logout();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SessionManager.getInstance().logout();

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/cashflow/fxml/login-view.fxml")
            );
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("CashFlow — Login");

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource("/cashflow/css/style.css").toExternalForm()
            );

            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();

            Stage current = (Stage) balanceLabel.getScene().getWindow();
            current.close();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Gagal logout.");
        }
    }

    private void openModal(String path, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent root = loader.load();

            Object controller = loader.getController();

            if (controller instanceof AddIncomeController incomeController) {
                incomeController.setDashboardController(this);
            } else if (controller instanceof AddExpenseController expenseController) {
                expenseController.setDashboardController(this);
            } else if (controller instanceof TransactionController transactionController) {
                transactionController.setDashboardController(this);
            }

            Stage stage = new Stage();
            stage.setTitle(title);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource("/cashflow/css/style.css").toExternalForm()
            );

            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Gagal membuka halaman.");
        }
    }

    private String formatRupiah(double amount) {
        NumberFormat fmt = NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID"));
        return "Rp " + fmt.format(amount);
    }

    private String safe(String value) {
        return value != null ? value : "";
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }
}