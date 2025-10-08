package com.empresa.controller;

import com.empresa.model.Endereco;
import com.empresa.model.Funcionario;
import com.empresa.service.FuncionarioService;
import com.empresa.service.RelatorioService; // Import necessário
import com.empresa.util.ValidacaoUtil;

import java.math.BigDecimal;
import java.util.List; // Import necessário
import java.util.Map; // Import necessário

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

    // Campos do formulário
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

    // Campos de consulta e filtro
    @FXML private TextField consultaMatriculaField;
    @FXML private TextField filtroCargoField;
    @FXML private TextField filtroSalarioMinField;
    @FXML private TextField filtroSalarioMaxField;

    // Tabela
    @FXML private TableView<Funcionario> funcionarioTable;
    @FXML private TableColumn<Funcionario, String> matriculaColumn;
    @FXML private TableColumn<Funcionario, String> nomeColumn;
    @FXML private TableColumn<Funcionario, String> cargoColumn;

    // Serviços
    private FuncionarioService funcionarioService;
    private RelatorioService relatorioService;

    @FXML
    public void initialize() {
        funcionarioService = new FuncionarioService();
        relatorioService = new RelatorioService(funcionarioService.listarTodos());

        configurarTabela();
        configurarListeners();
        atualizarTabela();
    }

    private void configurarTabela() {
        matriculaColumn.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cargoColumn.setCellValueFactory(new PropertyValueFactory<>("cargo"));
    }

    private void configurarListeners() {
        funcionarioTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        preencherFormulario(newSelection);
                    }
                }
        );
    }

    @FXML
    private void handleCadastrar() {
        if (!isInputValido()) return;
        try {
            Funcionario funcionario = criarFuncionarioDoFormulario();
            funcionarioService.cadastrarFuncionario(funcionario);
            showAlert(AlertType.INFORMATION, "Sucesso", "Funcionário cadastrado com sucesso!");
            atualizarTabela();
            limparFormulario();
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erro no Cadastro", e.getMessage());
        }
    }

    @FXML
    private void handleExcluir() {
        Funcionario selecionado = funcionarioTable.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            showAlert(AlertType.WARNING, "Nenhuma Seleção", "Por favor, selecione um funcionário na tabela.");
            return;
        }
        try {
            funcionarioService.excluirFuncionario(selecionado.getMatricula());
            showAlert(AlertType.INFORMATION, "Sucesso", "Funcionário excluído com sucesso!");
            atualizarTabela();
            limparFormulario();
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erro ao Excluir", e.getMessage());
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
                    funcionarioTable.getSelectionModel().select(f);
                    funcionarioTable.scrollTo(f);
                },
                () -> showAlert(AlertType.INFORMATION, "Não Encontrado", "Nenhum funcionário com a matrícula " + matricula)
        );
    }

    @FXML
    private void handleFiltrarPorCargo() {
        String cargo = filtroCargoField.getText();
        if (cargo == null || cargo.trim().isEmpty()) {
            showAlert(AlertType.WARNING, "Campo Vazio", "Por favor, digite um cargo para filtrar.");
            return;
        }
        List<Funcionario> resultado = relatorioService.filtrarPorCargo(cargo);
        funcionarioTable.setItems(FXCollections.observableArrayList(resultado));
    }

    @FXML
    private void handleFiltrarPorSalario() {
        try {
            BigDecimal min = new BigDecimal(filtroSalarioMinField.getText());
            BigDecimal max = new BigDecimal(filtroSalarioMaxField.getText());
            if (min.compareTo(max) > 0) {
                showAlert(AlertType.ERROR, "Erro de Lógica", "O salário mínimo não pode ser maior que o máximo.");
                return;
            }
            List<Funcionario> resultado = relatorioService.filtrarPorFaixaSalarial(min, max);
            funcionarioTable.setItems(FXCollections.observableArrayList(resultado));
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Erro de Formato", "Por favor, insira valores numéricos válidos para os salários.");
        }
    }

    @FXML
    private void handleMediaSalarialPorCargo() {
        Map<String, Double> mediaSalarial = relatorioService.calcularMediaSalarialPorCargo();
        StringBuilder relatorio = new StringBuilder("Média Salarial por Cargo:\n\n");
        mediaSalarial.forEach((cargo, media) ->
                relatorio.append(String.format("%s: R$ %.2f\n", cargo, media))
        );
        showAlert(AlertType.INFORMATION, "Relatório de Média Salarial", relatorio.toString());
    }

    @FXML
    private void handleAgruparPorCidade() {
        Map<String, List<Funcionario>> agrupado = relatorioService.agruparPorCidade();
        StringBuilder relatorio = new StringBuilder("Funcionários por Cidade:\n\n");
        agrupado.forEach((cidade, funcionarios) -> {
            relatorio.append("--- ").append(cidade).append(" ---\n");
            funcionarios.forEach(f -> relatorio.append("  - ").append(f.getNome()).append("\n"));
            relatorio.append("\n");
        });
        showAlert(AlertType.INFORMATION, "Relatório de Funcionários por Cidade", relatorio.toString());
    }

    @FXML
    private void handleLimparFiltro() {
        atualizarTabela();
        filtroCargoField.clear();
        filtroSalarioMinField.clear();
        filtroSalarioMaxField.clear();
    }

    private void limparFormulario() {
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
        funcionarioTable.getSelectionModel().clearSelection();
    }

    private void preencherFormulario(Funcionario funcionario) {
        matriculaField.setText(funcionario.getMatricula());
        nomeField.setText(funcionario.getNome());
        cpfField.setText(funcionario.getCpf());
        dataNascimentoPicker.setValue(funcionario.getDataNascimento());
        cargoField.setText(funcionario.getCargo());
        salarioField.setText(funcionario.getSalario().toString());
        dataContratacaoPicker.setValue(funcionario.getDataContratacao());
        Endereco endereco = funcionario.getEndereco();
        logradouroField.setText(endereco.getLogradouro());
        numeroField.setText(endereco.getNumero());
        complementoField.setText(endereco.getComplemento());
        bairroField.setText(endereco.getBairro());
        cidadeField.setText(endereco.getCidade());
        estadoField.setText(endereco.getEstado());
        cepField.setText(endereco.getCep());
    }

    private Funcionario criarFuncionarioDoFormulario() {
        Endereco endereco = new Endereco(logradouroField.getText(), numeroField.getText(), complementoField.getText(), bairroField.getText(), cidadeField.getText(), estadoField.getText(), cepField.getText());
        return new Funcionario(
                matriculaField.getText(), nomeField.getText(), cpfField.getText(), dataNascimentoPicker.getValue(),
                cargoField.getText(), new BigDecimal(salarioField.getText()), dataContratacaoPicker.getValue(), endereco
        );
    }

    private void atualizarTabela() {
        List<Funcionario> funcionarios = funcionarioService.listarTodos();
        funcionarioTable.setItems(FXCollections.observableArrayList(funcionarios));
        relatorioService = new RelatorioService(funcionarios);
    }

    private boolean isInputValido() {
        String errorMessage = "";
        if (!ValidacaoUtil.isMatriculaValida(matriculaField.getText())) errorMessage += "Matrícula inválida (deve ter 6 dígitos)\n";
        if (!ValidacaoUtil.isNomeValido(nomeField.getText())) errorMessage += "Nome inválido (mínimo 3 caracteres)\n";
        if (!ValidacaoUtil.isCpfValido(cpfField.getText())) errorMessage += "CPF inválido\n";
        if (!ValidacaoUtil.isMaiorDeIdade(dataNascimentoPicker.getValue())) errorMessage += "Funcionário deve ser maior de 18 anos\n";
        if (!ValidacaoUtil.isSalarioValido(salarioField.getText())) errorMessage += "Salário inválido (deve ser um valor positivo)\n";
        if (!ValidacaoUtil.isCepValido(cepField.getText())) errorMessage += "CEP inválido\n";

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