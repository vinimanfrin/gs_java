package br.com.fiap.infra.configuration.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUsuarioDTO(
        @NotNull(message = "campo id usuario é obrigatório")
        Long id,
        @NotBlank(message = "campo username deve ser preenchido")
        String username,

        @NotBlank(message = "campo password deve ser preenchido")
        String password,

        @NotNull(message = "campo administrador obrigatório")
        boolean administrador,
        @NotNull(message = "campo habilitado obrigatório")
        boolean habilitado
) {
}
