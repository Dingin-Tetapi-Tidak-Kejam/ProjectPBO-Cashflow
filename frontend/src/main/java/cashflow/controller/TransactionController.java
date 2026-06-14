package cashflow.controller;

import cashflow.dto.TransactionResponse;
import cashflow.service.api.ApiClientService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {

    @FXML private TextField searchField;
    @FXML private TableView<TransactionResponse> transactionTable;
    @FXML private TableColumn<TransactionResponse, Integer> idColumn;
    @FXML private TableColumn<TransactionResponse, String> typeColumn;
    @FXML private TableColumn<TransactionResponse, String> amountColumn;
    @FXML private TableColumn<TransactionResponse, String> dateColumn;
    @FXML private TableColumn<TransactionResponse, String> descriptionColumn;
    @FXML private TableColumn<TransactionResponse, String> detailColumn;

    private DashboardController dashboardController;
    private final ApiClientService apiClientService = new ApiClientService();

    public void setDashboardController(DashboardController dc) {
        this.dashboardController = dc;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTable();
        loadAll();
    }

    private void setupTable() {
        idColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(transactionTable.getItems().indexOf(cellData.getValue()) + 1)
        );

        typeColumn.setCellValueFactory(c ->
                new SimpleStringProperty(safe(c.getValue().getTransactionType())));
        amountColumn.setCellValueFactory(c ->
                new SimpleStringProperty(formatRupiah(
                        c.getValue().getAmount() != null ? c.getValue().getAmount() : 0.0)));
        dateColumn.setCellValueFactory(c ->
                new SimpleStringProperty(safe(c.getValue().getDate())));
        descriptionColumn.setCellValueFactory(c ->
                new SimpleStringProperty(safe(c.getValue().getDescription())));
        detailColumn.setCellValueFactory(c ->
                new SimpleStringProperty(safe(c.getValue().getDetail())));
    }

    private void loadAll() {
        try {
            List<TransactionResponse> rows = apiClientService.getAllTransactions();
            transactionTable.setItems(FXCollections.observableArrayList(rows));
        } catch (Exception e) {
            e.printStackTrace();
            showError("Gagal mengambil data transaksi dari backend.");
        }
    }

    @FXML
    public void handleSearch() {
        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {
            loadAll();
            return;
        }

        try {
            List<TransactionResponse> rows = apiClientService.searchTransactions(keyword);
            transactionTable.setItems(FXCollections.observableArrayList(rows));
        } catch (Exception e) {
            e.printStackTrace();
            showError("Gagal mencari transaksi dari backend.");
        }
    }

    @FXML
    public void handleRefresh() {
        searchField.clear();
        loadAll();
    }

    @FXML
    public void handleDeleteTransaction() {
        TransactionResponse selected = transactionTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Pilih transaksi yang ingin dihapus!", ButtonType.OK).showAndWait();
            return;
        }

        Optional<ButtonType> result = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Hapus transaksi ini?",
                ButtonType.YES, ButtonType.NO
        ).showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean deleted = apiClientService.deleteTransaction(selected.getId());

                if (deleted) {
                    loadAll();

                    if (dashboardController != null) {
                        dashboardController.refreshDashboard();
                    }

                    new Alert(Alert.AlertType.INFORMATION,
                            "Transaksi berhasil dihapus!", ButtonType.OK).showAndWait();
                } else {
                    showError("Transaksi gagal dihapus.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showError("Gagal menghapus transaksi dari backend.");
            }
        }
    }

    private String safe(String value) {
        return value != null ? value : "";
    }

    private String formatRupiah(double amount) {
        NumberFormat fmt = NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID"));
        return "Rp " + fmt.format(amount);
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }
}
