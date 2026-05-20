package cashflow.controller;

import database.TransactionDAO;
import database.TransactionDAO.TransactionRow;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {

    @FXML private TextField searchField;
    @FXML private TableView<TransactionRow> transactionTable;
    @FXML private TableColumn<TransactionRow, Integer> idColumn;
    @FXML private TableColumn<TransactionRow, String>  typeColumn;
    @FXML private TableColumn<TransactionRow, String>  amountColumn;
    @FXML private TableColumn<TransactionRow, String>  dateColumn;
    @FXML private TableColumn<TransactionRow, String>  descriptionColumn;
    @FXML private TableColumn<TransactionRow, String>  detailColumn;

    private DashboardController dashboardController;

    public void setDashboardController(DashboardController dc) {
        this.dashboardController = dc;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTable();
        loadAll();
    }

    private void setupTable() {
        idColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()).asObject());
        typeColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getType()));
        amountColumn.setCellValueFactory(c -> new SimpleStringProperty(formatRupiah(c.getValue().getAmount())));
        dateColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDate().toString()));
        descriptionColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDescription()));
        detailColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDetail()));
    }

    private void loadAll() {
        List<TransactionRow> rows = TransactionDAO.getAll();
        transactionTable.setItems(FXCollections.observableArrayList(rows));
    }

    @FXML
    public void handleSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadAll();
            return;
        }
        List<TransactionRow> rows = TransactionDAO.search(keyword);
        transactionTable.setItems(FXCollections.observableArrayList(rows));
    }

    @FXML
    public void handleRefresh() {
        searchField.clear();
        loadAll();
    }

    @FXML
    public void handleDeleteTransaction() {
        TransactionRow selected = transactionTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Pilih transaksi yang ingin dihapus!", ButtonType.OK).showAndWait();
            return;
        }

        Optional<ButtonType> result = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Hapus transaksi ID " + selected.getId() + "?",
                ButtonType.YES, ButtonType.NO
        ).showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            TransactionDAO.delete(selected.getId());
            loadAll();

            // Refresh summary di dashboard
            if (dashboardController != null) {
                dashboardController.refreshDashboard();
            }
        }
    }

    private String formatRupiah(double amount) {
        NumberFormat fmt = NumberFormat.getNumberInstance(new Locale("id", "ID"));
        return "Rp " + fmt.format(amount);
    }
}