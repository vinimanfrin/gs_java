package br.com.fiap.domain.entity;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prognostico {

    private Long id;

    private LocalDateTime data;
    private ResultadoProagnostico resultadoProagnostico;
    private String observacoes;

    private Paciente paciente;
    private Funcionario funcionario;


}
