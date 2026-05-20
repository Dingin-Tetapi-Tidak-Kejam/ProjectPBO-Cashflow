package cashflow.controller;

import cashflow.util.SessionManager;
import database.DatabaseInitializer;
import database.TransactionDAO;
import database.TransactionDAO.TransactionRow;
import javafx.beans.property.SimpleIntegerProperty;
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

    @FXML private TableView<TransactionRow>            transactionTable;
    @FXML private TableColumn<TransactionRow, Integer> idColumn;
    @FXML private TableColumn<TransactionRow, String>  typeColumn;
    @FXML private TableColumn<TransactionRow, String>  amountColumn;
    @FXML private TableColumn<TransactionRow, String>  dateColumn;
    @FXML private TableColumn<TransactionRow, String>  descriptionColumn;
    @FXML private TableColumn<TransactionRow, String>  detailColumn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DatabaseInitializer.initialize();
        setupTable();
        refreshDashboard();

        // Show logged-in username
        if (SessionManager.getInstance().isLoggedIn()) {
            welcomeLabel.setText("Halo, " +
                    SessionManager.getInstance().getCurrentUser().getUserName() + "!");
        }
    }

    private void setupTable() {
        idColumn.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getId()).asObject());
        typeColumn.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getType()));
        amountColumn.setCellValueFactory(c ->
                new SimpleStringProperty(formatRupiah(c.getValue().getAmount())));
        dateColumn.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDate().toString()));
        descriptionColumn.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDescription()));
        detailColumn.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDetail()));
    }

    public void refreshDashboard() {
        double totalIncome  = TransactionDAO.getTotalIncome();
        double totalExpense = TransactionDAO.getTotalExpense();
        double balance      = totalIncome - totalExpense;

        incomeLabel.setText(formatRupiah(totalIncome));
        expenseLabel.setText(formatRupiah(totalExpense));
        balanceLabel.setText(formatRupiah(balance));

        List<TransactionRow> rows = TransactionDAO.getAll();
        transactionTable.setItems(FXCollections.observableArrayList(rows));
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

            // Close dashboard
            Stage current = (Stage) incomeLabel.getScene().getWindow();
            current.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openModal(String path, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent root = loader.load();

            Object ctrl = loader.getController();
            if (ctrl instanceof AddIncomeController aic)   aic.setDashboardController(this);
            else if (ctrl instanceof AddExpenseController aec) aec.setDashboardController(this);
            else if (ctrl instanceof TransactionController tc)  tc.setDashboardController(this);

            Stage stage = new Stage();
            stage.setTitle(title);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource("/cashflow/css/style.css").toExternalForm()
            );
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formatRupiah(double amount) {
        NumberFormat fmt = NumberFormat.getNumberInstance(new Locale("id", "ID"));
        return "Rp " + fmt.format(amount);
    }
}