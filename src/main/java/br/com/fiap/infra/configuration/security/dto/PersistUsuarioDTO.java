package br.com.fiap.infra.configuration.security.dto;

import br.com.fiap.domain.dto.funcionario.PersistFuncionarioDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PersistUsuarioDTO(

        @NotBlank(message = "campo username deve ser preenchido")
        String username,

        @NotBlank(message = "campo password deve ser preenchido")
        String password,

        @NotNull(message = "campo administrador obrigatório")
        boolean administrador,
        @NotNull(message = "campo habilitado obrigatório")
        boolean habilitado,

        @Valid
        PersistFuncionarioDTO funcionario
) {
}
