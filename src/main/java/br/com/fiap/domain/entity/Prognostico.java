package br.com.fiap.domain.entity;


import br.com.fiap.domain.dto.prognostico.PersistPrognosticoDTO;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prognostico {

    private Long id;

    private LocalDateTime data;
    private ResultadoPrognostico resultadoPrognostico;
    private String observacoes;

    private Paciente paciente;
    private Funcionario funcionario;


    public Prognostico(PersistPrognosticoDTO dto) {
        Paciente paciente = new Paciente();
        paciente.setCpf(dto.cpfPaciente());
        Funcionario funcionario = new Funcionario();
        funcionario.setId(dto.idFuncionario());
        this.data = dto.data();
        this.resultadoPrognostico = dto.resultado();
        this.observacoes = dto.observacoes();
        this.paciente = paciente;
        this.funcionario = funcionario;
    }
}
