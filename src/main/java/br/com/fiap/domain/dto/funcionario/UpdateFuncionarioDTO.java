package br.com.fiap.domain.dto.funcionario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateFuncionarioDTO(
        @NotNull(message = "o id do funcionário a ser atualizado deve ser informado")
        Long id,
        @NotBlank(message = "O campo e-mail deve ser preenchido")
        @Email
        String email,
        @NotNull(message = "o id da rede hospitalar deve ser informado")
        Long idRedeHospitalar
) {
}
