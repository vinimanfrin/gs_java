package br.com.fiap.domain.dto.redeHospitalar;

import br.com.fiap.domain.dto.endereco.DetalhamentoEnderecoDTO;
import br.com.fiap.domain.dto.endereco.PersistEnderecoDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CNPJ;

public record PersistRedeHospitalarDTO(
        @NotBlank(message = "O campo Razão Social deve ser informado")
        String razaoSocial,
        @NotBlank(message = "Para o cadastro , o cnpj deve ser informado")
        @CNPJ(message = "Por favor , digite um cnpj válido")
        String cnpj,
        @Valid
        PersistEnderecoDTO endereco) {
}
