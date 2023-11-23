package br.com.fiap.domain.dto.paciente;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record BuscaCpfPacienteDTO(

        @CPF
        @NotBlank(message = "por favor , para realizar a busca digite um cpf válido")
        String cpf
) {
}
