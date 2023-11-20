package br.com.fiap.domain.dto.prognostico;

import br.com.fiap.domain.dto.funcionario.DetalhamentoFuncionarioDTO;
import br.com.fiap.domain.dto.paciente.DetalhamentoPacienteDTO;
import br.com.fiap.domain.entity.Prognostico;
import br.com.fiap.domain.entity.ResultadoPrognostico;

import java.time.LocalDateTime;

public record DetalhamentoPrognosticoDTO(
         Long id,

         LocalDateTime data,
         ResultadoPrognostico resultadoPrognostico,
         String observacoes,

         DetalhamentoPacienteDTO paciente,
         DetalhamentoFuncionarioDTO funcionario
) {
    public DetalhamentoPrognosticoDTO(Prognostico prognostico){
        this(prognostico.getId(), prognostico.getData(),prognostico.getResultadoPrognostico(), prognostico.getObservacoes(), new DetalhamentoPacienteDTO(prognostico.getPaciente()),new DetalhamentoFuncionarioDTO(prognostico.getFuncionario()));
    }
}
