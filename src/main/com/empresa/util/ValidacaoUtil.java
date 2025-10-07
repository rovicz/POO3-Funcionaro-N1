package com.empresa.util;

import java.time.LocalDate;
import java.time.Period;

public class ValidacaoUtil {

    public static boolean isMatriculaValida(String matricula) {
        return matricula != null && matricula.matches("\\d{6}");
    }

    public static boolean isNomeValido(String nome) {
        return nome != null && !nome.trim().isEmpty() && nome.length() >= 3;
    }

    public static boolean isCpfValido(String cpf) {
        // Implementação simplificada de validação de CPF (apenas formato)
        return cpf != null && cpf.matches("\\d{11}");
    }

    public static boolean isMaiorDeIdade(LocalDate dataNascimento) {
        if (dataNascimento == null) return false;
        return Period.between(dataNascimento, LocalDate.now()).getYears() >= 18;
    }

    public static boolean isSalarioValido(String salario) {
        try {
            double valor = Double.parseDouble(salario);
            return valor > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isCepValido(String cep) {
        return cep != null && cep.matches("\\d{8}");
    }
}