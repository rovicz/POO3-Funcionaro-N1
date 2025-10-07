package com.empresa.service;

import com.empresa.model.Funcionario;
import com.empresa.util.CsvUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FuncionarioService {

    private final String ARQUIVO_CSV = "funcionarios.csv";
    private List<Funcionario> funcionarios;

    public FuncionarioService() {
        try {
            this.funcionarios = CsvUtil.carregarFuncionarios(ARQUIVO_CSV);
        } catch (IOException e) {
            System.err.println("Erro ao carregar o arquivo CSV: " + e.getMessage());
            this.funcionarios = new ArrayList<>();
        }
    }

    public List<Funcionario> listarTodos() {
        return new ArrayList<>(funcionarios);
    }

    public void cadastrarFuncionario(Funcionario funcionario) throws Exception {
        if (funcionarios.stream().anyMatch(f -> f.getMatricula().equals(funcionario.getMatricula()))) {
            throw new Exception("Matrícula já cadastrada.");
        }
        funcionarios.add(funcionario);
        salvarDados();
    }

    public void excluirFuncionario(String matricula) throws Exception {
        boolean removido = funcionarios.removeIf(f -> f.getMatricula().equals(matricula));
        if (!removido) {
            throw new Exception("Funcionário com a matrícula informada não encontrado.");
        }
        salvarDados();
    }

    public Optional<Funcionario> consultarPorMatricula(String matricula) {
        return funcionarios.stream()
                .filter(f -> f.getMatricula().equals(matricula))
                .findFirst();
    }

    private void salvarDados() throws IOException {
        CsvUtil.salvarFuncionarios(ARQUIVO_CSV, funcionarios);
    }
}