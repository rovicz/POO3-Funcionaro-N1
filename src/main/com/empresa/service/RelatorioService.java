package com.empresa.service;

import com.empresa.model.Funcionario;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RelatorioService {

    private final List<Funcionario> funcionarios;

    public RelatorioService(List<Funcionario> funcionarios) {
        this.funcionarios = funcionarios;
    }

    public List<Funcionario> filtrarPorCargo(String cargo) {
        return funcionarios.stream()
                .filter(f -> f.getCargo().equalsIgnoreCase(cargo))
                .collect(Collectors.toList());
    }

    public List<Funcionario> filtrarPorFaixaSalarial(BigDecimal min, BigDecimal max) {
        return funcionarios.stream()
                .filter(f -> f.getSalario().compareTo(min) >= 0 && f.getSalario().compareTo(max) <= 0)
                .collect(Collectors.toList());
    }

    public Map<String, Double> calcularMediaSalarialPorCargo() {
        return funcionarios.stream()
                .collect(Collectors.groupingBy(
                        Funcionario::getCargo,
                        Collectors.averagingDouble(f -> f.getSalario().doubleValue())
                ));
    }

    public Map<String, List<Funcionario>> agruparPorCidade() {
        return funcionarios.stream()
                .collect(Collectors.groupingBy(f -> f.getEndereco().getCidade()));
    }
}