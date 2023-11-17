package br.com.fiap.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RedeHospitalar {

    @Setter(AccessLevel.NONE)
    private Long id;
    private String razaoSocial;
    private String cnpj;
    private Endereco endereco;

    public RedeHospitalar(String razaoSocial, String cnpj, Endereco endereco) {
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.endereco = endereco;
    }
}
