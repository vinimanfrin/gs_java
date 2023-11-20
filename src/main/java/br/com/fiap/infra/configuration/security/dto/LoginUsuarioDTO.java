package br.com.fiap.infra.configuration.security.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginUsuarioDTO(
        @NotBlank(message = "campo username deve ser preenchido")
        String username,

        @NotBlank(message = "campo password deve ser preenchido")
        String password
) {
}
