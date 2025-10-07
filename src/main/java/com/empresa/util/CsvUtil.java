package com.empresa.util;

import com.empresa.model.Endereco;
import com.empresa.model.Funcionario;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void salvarFuncionarios(String caminhoArquivo, List<Funcionario> funcionarios) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(caminhoArquivo),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        )) {
            for (Funcionario f : funcionarios) {
                writer.write(f.toString());
                writer.newLine();
            }
        }
    }

    public static List<Funcionario> carregarFuncionarios(String caminhoArquivo) throws IOException {
        List<Funcionario> funcionarios = new ArrayList<>();

        if (Files.notExists(Paths.get(caminhoArquivo))) {
            return funcionarios;
        }

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(caminhoArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue; // ignora linhas vazias

                String[] dados = linha.split(";", -1);
                if (dados.length < 14) {
                    System.err.println("Linha inválida (colunas insuficientes): " + linha);
                    continue;
                }

                LocalDate dataNascimento = parseData(dados[3]);
                LocalDate dataAdmissao = parseData(dados[6]);
                BigDecimal salario = parseBigDecimal(dados[5]);

                Endereco endereco = new Endereco(
                        dados[7], dados[8], dados[9], dados[10],
                        dados[11], dados[12], dados[13]
                );

                Funcionario funcionario = new Funcionario(
                        dados[0],
                        dados[1],
                        dados[2],
                        dataNascimento,
                        dados[4],
                        salario,
                        dataAdmissao,
                        endereco
                );

                funcionarios.add(funcionario);
            }
        }

        return funcionarios;
    }


    private static LocalDate parseData(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(valor.trim(), DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            System.err.println("Data inválida: '" + valor + "' — formato esperado: yyyy-MM-dd");
            return null;
        }
    }

    private static BigDecimal parseBigDecimal(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(valor.trim());
        } catch (NumberFormatException e) {
            System.err.println("Valor numérico inválido: '" + valor + "'");
            return BigDecimal.ZERO;
        }
    }
}
