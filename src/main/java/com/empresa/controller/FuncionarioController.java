package com.empresa.controller;

import com.empresa.model.Endereco;
import com.empresa.model.Funcionario;
import com.empresa.service.FuncionarioService;
import com.empresa.util.ValidacaoUtil;

import java.math.BigDecimal;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class FuncionarioController {

    @FXML private TextField matriculaField;
    @FXML private TextField nomeField;
    @FXML private TextField cpfField;
    @FXML private DatePicker dataNascimentoPicker;
    @FXML private TextField cargoField;
    @FXML private TextField salarioField;
    @FXML private DatePicker dataContratacaoPicker;
    @FXML private TextField logradouroField;
    @FXML private TextField numeroField;
    @FXML private TextField complementoField;
    @FXML private TextField bairroField;
    @FXML private TextField cidadeField;
    @FXML private TextField estadoField;
    @FXML private TextField cepField;
    @FXML private TextField consultaMatriculaField;

    @FXML private TableView<Funcionario> funcionarioTable;
    @FXML private TableColumn<Funcionario, String> matriculaColumn;
    @FXML private TableColumn<Funcionario, String> nomeColumn;
    @FXML private TableColumn<Funcionario, String> cargoColumn;

    private FuncionarioService funcionarioService;

    @FXML
    public void initialize() {
        funcionarioService = new FuncionarioService();

        matriculaColumn.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cargoColumn.setCellValueFactory(new PropertyValueFactory<>("cargo"));

        atualizarTabela();
    }

    private void atualizarTabela() {
        funcionarioTable.setItems(FXCollections.observableArrayList(funcionarioService.listarTodos()));
    }

    private void limparCampos() {
        matriculaField.clear();
        nomeField.clear();
        cpfField.clear();
        dataNascimentoPicker.setValue(null);
        cargoField.clear();
        salarioField.clear();
        dataContratacaoPicker.setValue(null);
        logradouroField.clear();
        numeroField.clear();
        complementoField.clear();
        bairroField.clear();
        cidadeField.clear();
        estadoField.clear();
        cepField.clear();
    }

    @FXML
    private void handleCadastrar() {
        if (!isInputValido()) {
            return;
        }

        try {
            Endereco endereco = new Endereco(logradouroField.getText(), numeroField.getText(), complementoField.getText(), bairroField.getText(), cidadeField.getText(), estadoField.getText(), cepField.getText());
            Funcionario funcionario = new Funcionario(
                    matriculaField.getText(), nomeField.getText(), cpfField.getText(), dataNascimentoPicker.getValue(),
                    cargoField.getText(), new BigDecimal(salarioField.getText()), dataContratacaoPicker.getValue(), endereco
            );

            funcionarioService.cadastrarFuncionario(funcionario);
            showAlert(AlertType.INFORMATION, "Sucesso", "Funcionário cadastrado com sucesso!");
            atualizarTabela();
            limparCampos();

        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erro no Cadastro", e.getMessage());
        }
    }

    @FXML
    private void handleExcluir() {
        Funcionario selecionado = funcionarioTable.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            try {
                funcionarioService.excluirFuncionario(selecionado.getMatricula());
                showAlert(AlertType.INFORMATION, "Sucesso", "Funcionário excluído com sucesso!");
                atualizarTabela();
            } catch (Exception e) {
                showAlert(AlertType.ERROR, "Erro ao Excluir", e.getMessage());
            }
        } else {
            showAlert(AlertType.WARNING, "Nenhuma Seleção", "Por favor, selecione um funcionário na tabela.");
        }
    }

    @FXML
    private void handleConsultar() {
        String matricula = consultaMatriculaField.getText();
        if (matricula == null || matricula.trim().isEmpty()) {
            showAlert(AlertType.WARNING, "Entrada Inválida", "Digite uma matrícula para consultar.");
            return;
        }

        funcionarioService.consultarPorMatricula(matricula).ifPresentOrElse(
                f -> {
                    String info = "Matrícula: " + f.getMatricula() + "\n" +
                            "Nome: " + f.getNome() + "\n" +
                            "CPF: " + f.getCpf() + "\n" +
                            "Cargo: " + f.getCargo() + "\n" +
                            "Salário: " + f.getSalario() + "\n" +
                            "Cidade: " + f.getEndereco().getCidade();
                    showAlert(AlertType.INFORMATION, "Funcionário Encontrado", info);
                },
                () -> showAlert(AlertType.INFORMATION, "Não Encontrado", "Nenhum funcionário encontrado com a matrícula " + matricula)
        );
    }

    private boolean isInputValido() {
        String errorMessage = "";

        if (!ValidacaoUtil.isMatriculaValida(matriculaField.getText())) errorMessage += "Matrícula inválida (deve ter 6 dígitos)\n";
        if (!ValidacaoUtil.isNomeValido(nomeField.getText())) errorMessage += "Nome inválido (mínimo 3 caracteres)\n";
        if (!ValidacaoUtil.isCpfValido(cpfField.getText())) errorMessage += "CPF inválido (deve ter 11 dígitos)\n";
        if (!ValidacaoUtil.isMaiorDeIdade(dataNascimentoPicker.getValue())) errorMessage += "Funcionário deve ser maior de 18 anos\n";
        if (!ValidacaoUtil.isSalarioValido(salarioField.getText())) errorMessage += "Salário inválido (deve ser um valor positivo)\n";
        if (!ValidacaoUtil.isCepValido(cepField.getText())) errorMessage += "CEP inválido (deve ter 8 dígitos)\n";

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showAlert(AlertType.ERROR, "Campos Inválidos", errorMessage);
            return false;
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}