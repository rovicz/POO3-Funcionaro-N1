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
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {

    public static void salvarFuncionarios(String caminhoArquivo, List<Funcionario> funcionarios) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(caminhoArquivo), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
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
                String[] dados = linha.split(";", -1);
                Endereco endereco = new Endereco(dados[7], dados[8], dados[9], dados[10], dados[11], dados[12], dados[13]);
                Funcionario funcionario = new Funcionario(
                        dados[0], dados[1], dados[2], LocalDate.parse(dados[3]),
                        dados[4], new BigDecimal(dados[5]), LocalDate.parse(dados[6]), endereco
                );
                funcionarios.add(funcionario);
            }
        }
        return funcionarios;
    }
}