package cashflow;

import database.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // App now starts at Login screen
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/cashflow/fxml/login-view.fxml")
        );
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                getClass().getResource("/cashflow/css/style.css").toExternalForm()
        );

        stage.setTitle("CashFlow — Login");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setOnCloseRequest(e -> DatabaseConnection.closeConnection());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}