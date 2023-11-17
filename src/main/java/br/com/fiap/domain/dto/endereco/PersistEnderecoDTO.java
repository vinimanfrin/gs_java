package br.com.fiap.domain.dto.endereco;

import jakarta.validation.constraints.NotBlank;

public record PersistEnderecoDTO(
        @NotBlank(message = "O campo Estado deve ser informado")
        String estado,

        @NotBlank(message = "O campo Município deve ser informado")
        String municipio,

        @NotBlank(message = "O campo Logradouro deve ser informado")
        String logradouro,

        @NotBlank(message = "O campo Numero Residencial deve ser informado")
        String numeroResidencial,

        @NotBlank(message = "O campo Complemento deve ser informado")
        String complemento,

        @NotBlank(message = "O campo Cep deve ser informado")
        String cep)
    {
}
