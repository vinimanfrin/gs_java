package br.com.fiap.domain.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Prognostico {
    @Setter(AccessLevel.NONE)
    private Long id;

    private LocalDateTime data;
    private ResultadoProagnostico resultadoProagnostico;
    private String observacoes;

    private Paciente paciente;
    private Funcionario funcionario;

    public Prognostico(LocalDateTime data, ResultadoProagnostico resultadoProagnostico, String observacoes, Paciente paciente, Funcionario funcionario) {
        this.data = data;
        this.resultadoProagnostico = resultadoProagnostico;
        this.observacoes = observacoes;
        this.paciente = paciente;
        this.funcionario = funcionario;
    }
}
