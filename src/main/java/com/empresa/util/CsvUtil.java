package com.empresa.util;

import com.empresa.model.Endereco;
import com.empresa.model.Funcionario;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CsvUtil {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public static Map<String, Endereco> carregarEnderecos(String arquivoEnderecos) throws IOException {
        List<Endereco> enderecos = new ArrayList<>();
        File file = new File(arquivoEnderecos);
        if (!file.exists()) {
            return Map.of();
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";", -1);
                if (values.length == 8) {
                    enderecos.add(new Endereco(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7]));
                }
            }
        }
        return enderecos.stream().collect(Collectors.toMap(Endereco::getId, e -> e));
    }

    public static List<Funcionario> carregarFuncionarios(String arquivoFuncionarios, Map<String, Endereco> mapaEnderecos) throws IOException {
        List<Funcionario> funcionarios = new ArrayList<>();
        File file = new File(arquivoFuncionarios);
        if (!file.exists()) {
            return funcionarios;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";", -1);
                if (values.length == 8) {
                    String enderecoId = values[7];
                    Endereco enderecoDoFuncionario = mapaEnderecos.get(enderecoId);

                    if (enderecoDoFuncionario != null) {
                        String dataNascStr = values[3];
                        String dataContratacaoStr = values[6];

                        LocalDate dataNascimento = dataNascStr.isEmpty() ? null : LocalDate.parse(dataNascStr, DATE_FORMATTER);
                        LocalDate dataContratacao = dataContratacaoStr.isEmpty() ? null : LocalDate.parse(dataContratacaoStr, DATE_FORMATTER);

                        Funcionario f = new Funcionario(
                                values[0], values[1], values[2], dataNascimento,
                                values[4], new BigDecimal(values[5]), dataContratacao,
                                enderecoDoFuncionario
                        );
                        funcionarios.add(f);
                    }
                }
            }
        }
        return funcionarios;
    }

    public static void salvarEnderecos(String arquivoEnderecos, List<Endereco> enderecos) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoEnderecos))) {
            bw.write("endereco_id;logradouro;numero;complemento;bairro;cidade;estado;cep");
            bw.newLine();
            for (Endereco e : enderecos) {
                String line = String.join(";",
                        e.getId(), e.getLogradouro(), e.getNumero(), e.getComplemento(),
                        e.getBairro(), e.getCidade(), e.getEstado(), e.getCep()
                );
                bw.write(line);
                bw.newLine();
            }
        }
    }

    public static void salvarFuncionarios(String arquivoFuncionarios, List<Funcionario> funcionarios) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoFuncionarios))) {
            bw.write("matricula;nome;cpf;dataNascimento;cargo;salario;dataContratacao;fk_endereco_id");
            bw.newLine();
            for (Funcionario f : funcionarios) {
                String dataNascimentoStr = f.getDataNascimento() != null ? f.getDataNascimento().format(DATE_FORMATTER) : "";
                String dataContratacaoStr = f.getDataContratacao() != null ? f.getDataContratacao().format(DATE_FORMATTER) : "";

                String line = String.join(";",
                        f.getMatricula(), f.getNome(), f.getCpf(),
                        dataNascimentoStr, f.getCargo(),
                        f.getSalario().toString(), dataContratacaoStr,
                        f.getEndereco().getId()
                );
                bw.write(line);
                bw.newLine();
            }
        }
    }
}