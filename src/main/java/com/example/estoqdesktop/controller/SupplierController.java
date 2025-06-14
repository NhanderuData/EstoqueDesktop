package com.example.estoqdesktop.controller;

import com.example.estoqdesktop.model.Supplier;
import com.example.estoqdesktop.service.SupplierService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleLongProperty;
import java.util.regex.Pattern;

public class SupplierController {

    @FXML private TableView<Supplier> tabelaFornecedores;
    @FXML private TableColumn<Supplier, Long> colunaId;
    @FXML private TableColumn<Supplier, String> colunaNome;
    @FXML private TableColumn<Supplier, String> colunaContato;
    @FXML private TableColumn<Supplier, String> colunaTelefone;
    @FXML private TableColumn<Supplier, String> colunaEmail;
    @FXML private TableColumn<Supplier, String> colunaEndereco;
    @FXML private TableColumn<Supplier, String> colunaDataCadastro;

    @FXML private TextField nomeField;
    @FXML private TextField contatoField;
    @FXML private TextField telefoneField;
    @FXML private TextField emailField;
    @FXML private TextArea enderecoField;

    @FXML private Button salvarBtn;
    @FXML private Button editarBtn;
    @FXML private Button excluirBtn;
    @FXML private Button limparBtn;
    @FXML private Button atualizarBtn;

    private SupplierService service = new SupplierService();
    private ObservableList<Supplier> fornecedores = FXCollections.observableArrayList();
    private Supplier fornecedorSelecionado = null;

    // Padrões de validação
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\([0-9]{2}\\)\\s[0-9]{4,5}-[0-9]{4}$"
    );

    @FXML
    public void initialize() {
        configurarColunas();
        configurarSelecaoTabela();
        carregarFornecedores();
        configurarBotoes();
        configurarValidacaoTelefone();
    }

    private void configurarColunas() {
        colunaId.setCellValueFactory(cellData ->
                new SimpleLongProperty(cellData.getValue().getId()).asObject());
        colunaNome.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));
        colunaContato.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getContactPerson()));
        colunaTelefone.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPhone()));
        colunaEmail.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEmail()));
        colunaEndereco.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAddress()));
        colunaDataCadastro.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFormattedRegistrationDate()));
    }

    private void configurarSelecaoTabela() {
        tabelaFornecedores.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    fornecedorSelecionado = newValue;
                    if (newValue != null) {
                        preencherCampos(newValue);
                        editarBtn.setDisable(false);
                        excluirBtn.setDisable(false);
                    } else {
                        limparCampos();
                        editarBtn.setDisable(true);
                        excluirBtn.setDisable(true);
                    }
                }
        );
    }

    private void configurarBotoes() {
        editarBtn.setDisable(true);
        excluirBtn.setDisable(true);
    }

    private void configurarValidacaoTelefone() {
        telefoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                String formatted = formatarTelefone(newValue);
                if (!formatted.equals(newValue)) {
                    telefoneField.setText(formatted);
                }
            }
        });
    }

    private String formatarTelefone(String telefone) {
        // Remove todos os caracteres não numéricos
        String numeros = telefone.replaceAll("\\D", "");

        // Limita a 11 dígitos
        if (numeros.length() > 11) {
            numeros = numeros.substring(0, 11);
        }

        // Formata o telefone
        if (numeros.length() >= 2) {
            StringBuilder sb = new StringBuilder();
            sb.append("(").append(numeros.substring(0, 2)).append(")");

            if (numeros.length() > 2) {
                sb.append(" ");
                if (numeros.length() <= 6) {
                    sb.append(numeros.substring(2));
                } else if (numeros.length() <= 10) {
                    sb.append(numeros.substring(2, 6)).append("-").append(numeros.substring(6));
                } else {
                    sb.append(numeros.substring(2, 7)).append("-").append(numeros.substring(7));
                }
            }
            return sb.toString();
        }
        return numeros;
    }

    public void carregarFornecedores() {
        try {
            fornecedores.setAll(service.listarTodos());
            tabelaFornecedores.setItems(fornecedores);
            mostrarSucesso("Fornecedores carregados com sucesso!");
        } catch (Exception e) {
            mostrarErro("Erro ao carregar fornecedores: " + e.getMessage());
        }
    }

    @FXML
    public void salvarFornecedor() {
        if (!validarCampos()) {
            return;
        }

        try {
            Supplier fornecedor = new Supplier();
            fornecedor.setName(getTextSafe(nomeField));
            fornecedor.setContactPerson(getTextSafe(contatoField));
            fornecedor.setPhone(getTextSafe(telefoneField));
            fornecedor.setEmail(getTextSafe(emailField).toLowerCase());
            fornecedor.setAddress(getTextSafe(enderecoField));

            service.salvar(fornecedor);
            carregarFornecedores();
            limparCampos();
            mostrarSucesso("Fornecedor salvo com sucesso!");
        } catch (Exception e) {
            mostrarErro("Erro ao salvar fornecedor: " + e.getMessage());
        }
    }

    @FXML
    public void editarFornecedor() {
        if (fornecedorSelecionado == null) {
            mostrarErro("Selecione um fornecedor para editar!");
            return;
        }

        if (!validarCampos()) {
            return;
        }

        try {
            fornecedorSelecionado.setName(getTextSafe(nomeField));
            fornecedorSelecionado.setContactPerson(getTextSafe(contatoField));
            fornecedorSelecionado.setPhone(getTextSafe(telefoneField));
            fornecedorSelecionado.setEmail(getTextSafe(emailField).toLowerCase());
            fornecedorSelecionado.setAddress(getTextSafe(enderecoField));

            service.atualizar(fornecedorSelecionado);
            carregarFornecedores();
            limparCampos();
            mostrarSucesso("Fornecedor atualizado com sucesso!");
        } catch (Exception e) {
            mostrarErro("Erro ao atualizar fornecedor: " + e.getMessage());
        }
    }

    @FXML
    public void excluirFornecedor() {
        Supplier selecionado = tabelaFornecedores.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarErro("Selecione um fornecedor para excluir!");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Tem certeza que deseja excluir este fornecedor?");
        confirmacao.setContentText("Fornecedor: " + selecionado.getName());

        if (confirmacao.showAndWait().get() == ButtonType.OK) {
            try {
                service.excluir(selecionado.getId());
                carregarFornecedores();
                limparCampos();
                mostrarSucesso("Fornecedor excluído com sucesso!");
            } catch (Exception e) {
                mostrarErro("Erro ao excluir fornecedor: " + e.getMessage());
            }
        }
    }

    @FXML
    public void limparCampos() {
        if (nomeField != null) nomeField.clear();
        if (contatoField != null) contatoField.clear();
        if (telefoneField != null) telefoneField.clear();
        if (emailField != null) emailField.clear();
        if (enderecoField != null) enderecoField.clear();
        if (tabelaFornecedores != null) tabelaFornecedores.getSelectionModel().clearSelection();
        fornecedorSelecionado = null;
    }

    @FXML
    public void atualizarTabela() {
        carregarFornecedores();
    }

    private void preencherCampos(Supplier fornecedor) {
        if (nomeField != null) nomeField.setText(fornecedor.getName() != null ? fornecedor.getName() : "");
        if (contatoField != null) contatoField.setText(fornecedor.getContactPerson() != null ? fornecedor.getContactPerson() : "");
        if (telefoneField != null) telefoneField.setText(fornecedor.getPhone() != null ? fornecedor.getPhone() : "");
        if (emailField != null) emailField.setText(fornecedor.getEmail() != null ? fornecedor.getEmail() : "");
        if (enderecoField != null) enderecoField.setText(fornecedor.getAddress() != null ? fornecedor.getAddress() : "");
    }

    // Método auxiliar para obter texto de forma segura
    private String getTextSafe(TextField field) {
        if (field == null || field.getText() == null) {
            return "";
        }
        return field.getText().trim();
    }

    // Método auxiliar para obter texto de TextArea de forma segura
    private String getTextSafe(TextArea area) {
        if (area == null || area.getText() == null) {
            return "";
        }
        return area.getText().trim();
    }

    private boolean validarCampos() {
        // Verificar se os campos foram inicializados
        if (nomeField == null || contatoField == null || telefoneField == null ||
                emailField == null || enderecoField == null) {
            mostrarErro("Erro interno: Campos não foram inicializados corretamente!");
            return false;
        }

        String nome = getTextSafe(nomeField);
        if (nome.isEmpty()) {
            mostrarErro("Nome do fornecedor é obrigatório!");
            nomeField.requestFocus();
            return false;
        }

        String contato = getTextSafe(contatoField);
        if (contato.isEmpty()) {
            mostrarErro("Nome da pessoa de contato é obrigatório!");
            contatoField.requestFocus();
            return false;
        }

        String telefone = getTextSafe(telefoneField);
        if (telefone.isEmpty()) {
            mostrarErro("Telefone é obrigatório!");
            telefoneField.requestFocus();
            return false;
        }

        if (!PHONE_PATTERN.matcher(telefone).matches()) {
            mostrarErro("Formato de telefone inválido! Use o formato: (XX) XXXXX-XXXX");
            telefoneField.requestFocus();
            return false;
        }

        String email = getTextSafe(emailField);
        if (email.isEmpty()) {
            mostrarErro("Email é obrigatório!");
            emailField.requestFocus();
            return false;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            mostrarErro("Formato de email inválido!");
            emailField.requestFocus();
            return false;
        }

        String endereco = getTextSafe(enderecoField);
        if (endereco.isEmpty()) {
            mostrarErro("Endereço é obrigatório!");
            enderecoField.requestFocus();
            return false;
        }

        return true;
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText("Ocorreu um erro");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText("Operação realizada com sucesso");
        alert.setContentText(mensagem);
        alert.show();
    }
}
