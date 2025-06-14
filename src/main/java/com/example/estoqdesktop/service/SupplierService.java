package com.example.estoqdesktop.service;

import com.example.estoqdesktop.model.Supplier;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.net.URI;
import java.net.http.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SupplierService {
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson;
    private final String BASE_URL = "http://localhost:8080/api/suppliers";

    public SupplierService() {
        // Configurar Gson para lidar com LocalDateTime
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                        context.serialize(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) ->
                        LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .create();
    }

    public List<Supplier> listarTodos() throws Exception {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new Exception("Erro na API: " + response.statusCode() + " - " + response.body());
            }

            return gson.fromJson(response.body(), new TypeToken<List<Supplier>>(){}.getType());
        } catch (Exception e) {
            throw new Exception("Erro ao buscar fornecedores: " + e.getMessage());
        }
    }

    public Supplier salvar(Supplier fornecedor) throws Exception {
        try {
            String json = gson.toJson(fornecedor);
            System.out.println("JSON enviado: " + json); // Para debug

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200 && response.statusCode() != 201) {
                throw new Exception("Erro ao salvar: " + response.statusCode() + " - " + response.body());
            }

            return gson.fromJson(response.body(), Supplier.class);
        } catch (Exception e) {
            throw new Exception("Erro ao salvar fornecedor: " + e.getMessage());
        }
    }

    public Supplier atualizar(Supplier fornecedor) throws Exception {
        try {
            if (fornecedor.getId() == null) {
                throw new Exception("ID do fornecedor é obrigatório para atualização");
            }

            String json = gson.toJson(fornecedor);
            System.out.println("JSON enviado para atualização: " + json); // Para debug

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + fornecedor.getId()))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new Exception("Erro ao atualizar: " + response.statusCode() + " - " + response.body());
            }

            return gson.fromJson(response.body(), Supplier.class);
        } catch (Exception e) {
            throw new Exception("Erro ao atualizar fornecedor: " + e.getMessage());
        }
    }

    public void excluir(Long id) throws Exception {
        try {
            if (id == null) {
                throw new Exception("ID do fornecedor é obrigatório para exclusão");
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + id))
                    .header("Accept", "application/json")
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200 && response.statusCode() != 204) {
                throw new Exception("Erro ao excluir: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            throw new Exception("Erro ao excluir fornecedor: " + e.getMessage());
        }
    }

    public Supplier buscarPorId(Long id) throws Exception {
        try {
            if (id == null) {
                throw new Exception("ID do fornecedor é obrigatório");
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + id))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 404) {
                throw new Exception("Fornecedor não encontrado");
            }

            if (response.statusCode() != 200) {
                throw new Exception("Erro ao buscar fornecedor: " + response.statusCode() + " - " + response.body());
            }

            return gson.fromJson(response.body(), Supplier.class);
        } catch (Exception e) {
            throw new Exception("Erro ao buscar fornecedor: " + e.getMessage());
        }
    }

    // Método para buscar fornecedores por nome (se sua API suportar)
    public List<Supplier> buscarPorNome(String nome) throws Exception {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "?name=" + nome))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new Exception("Erro na busca: " + response.statusCode() + " - " + response.body());
            }

            return gson.fromJson(response.body(), new TypeToken<List<Supplier>>(){}.getType());
        } catch (Exception e) {
            throw new Exception("Erro ao buscar fornecedores por nome: " + e.getMessage());
        }
    }
}