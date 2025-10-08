package com.empresa.service;

import com.empresa.model.Endereco;
import com.empresa.model.Funcionario;
import com.empresa.util.CsvUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FuncionarioService {

    private final String ARQUIVO_FUNCIONARIOS = "funcionarios.csv";
    private final String ARQUIVO_ENDERECOS = "enderecos.csv";
    private List<Funcionario> funcionarios;

    public FuncionarioService() {
        try {
            // Passo 1: Carregar todos os endereços em um mapa para acesso rápido
            Map<String, Endereco> mapaEnderecos = CsvUtil.carregarEnderecos(ARQUIVO_ENDERECOS);
            // Passo 2: Carregar os funcionários e usar o mapa para linkar os endereços
            this.funcionarios = CsvUtil.carregarFuncionarios(ARQUIVO_FUNCIONARIOS, mapaEnderecos);
        } catch (IOException e) {
            System.err.println("Erro ao carregar os arquivos CSV: " + e.getMessage());
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
        List<Endereco> todosOsEnderecos = this.funcionarios.stream()
                .map(Funcionario::getEndereco)
                .distinct()
                .collect(Collectors.toList());

        CsvUtil.salvarEnderecos(ARQUIVO_ENDERECOS, todosOsEnderecos);
        CsvUtil.salvarFuncionarios(ARQUIVO_FUNCIONARIOS, this.funcionarios);
    }
}