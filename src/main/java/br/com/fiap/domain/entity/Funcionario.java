package br.com.fiap.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Funcionario {

    @Setter(AccessLevel.NONE)
    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private RedeHospitalar redeHospitalar;

    public Funcionario(String nome, String cpf, String email,RedeHospitalar redeHospitalar) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.redeHospitalar = redeHospitalar;
    }
}
