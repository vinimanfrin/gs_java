package br.com.fiap.domain.dto.prognostico;

import br.com.fiap.domain.entity.ResultadoPrognostico;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDateTime;

public record PersistPrognosticoDTO(

        LocalDateTime data,

        @NotNull(message = "o resultado deve ser informado: PNEUMONIA, TUBERCULOSE ou NORMAL")
        ResultadoPrognostico resultado,

        String observacoes,

        @CPF(message = "Por favor , insira um cpf valido")
        String cpfPaciente,

        @NotNull(message = "o id do funcionario é obrigatorio")
        Long idFuncionario
) {
}
