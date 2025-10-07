package com.empresa.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/empresa/MainView.fxml"));
        Scene scene = new Scene(root);

        scene.getStylesheets().add(getClass().getResource("/css/dark-theme.css").toExternalForm());

        stage.setTitle("Gerenciador de Funcionários");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}