// src/main/java/com.example.estoqdesktop/module-info.java

module com.example.estoqdesktop {
    requires javafx.controls;
    requires javafx.fxml;

    // Adicione estas linhas para permitir que o módulo acesse a API HTTP do Java e o Gson
    requires java.net.http;
    requires com.google.gson;

    // Abre o pacote principal para javafx.fxml (já existia)
    opens com.example.estoqdesktop to javafx.fxml;

    // Abre o pacote do controller para javafx.fxml (já existia)
    opens com.example.estoqdesktop.controller to javafx.fxml;

    // Abre o pacote do modelo para o Gson para serialização/desserialização
    opens com.example.estoqdesktop.model to com.google.gson;

    // Exporta o pacote principal (já existia)
    exports com.example.estoqdesktop;
    // Exporta o pacote do controller (já existia)
    exports com.example.estoqdesktop.controller;
    // Exporta o pacote do modelo para que outras partes do seu app possam usá-lo
    exports com.example.estoqdesktop.model;
    // Exporta seu pacote de serviço para que outras partes do seu app possam usá-lo
    exports com.example.estoqdesktop.service;
}