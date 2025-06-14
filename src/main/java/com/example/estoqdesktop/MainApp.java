package com.example.estoqdesktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/com/example/estoqdesktop/supplier-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);

        // Configurações da janela
        stage.setTitle("Sistema de Gestão de Fornecedores");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(700);

        // Opcional: Adicionar ícone da aplicação
        try {
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        } catch (Exception e) {
            // Ícone não encontrado, continua sem ícone
        }

        // Centralizar na tela
        stage.centerOnScreen();

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}