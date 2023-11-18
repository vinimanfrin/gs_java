package br.com.fiap.domain.dto.redeHospitalar;

import br.com.fiap.domain.dto.endereco.UpdateEnderecoDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateRedeHospitalarDTO(
        @NotNull(message = "Para atualizar os dados de uma Rede , seu id deve ser informado")
        Long id,

        @NotBlank(message = "Para a atualização , a Razão Social deve ser informado")
        String razaoSocial) {
}
