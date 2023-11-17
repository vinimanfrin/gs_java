package br.com.fiap.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
@NoArgsConstructor
public class Paciente {
    @Setter(AccessLevel.NONE)
    private Long id;

    private Long nome;
    private String cpf;

    @Setter(AccessLevel.NONE)
    private Integer idade;

    private String email;
    private LocalDate dataNascimento;
    private Sexo sexo;
    private double peso;
    private double altura;

    public Paciente( Long nome, String cpf, String email, LocalDate dataNascimento, Sexo sexo, double peso, double altura) {
        LocalDate dataAtual = LocalDate.now();
        Period period = Period.between(dataNascimento,dataAtual);

        this.id = null;
        this.nome = nome;
        this.cpf = cpf;
        this.idade = period.getYears();
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.peso = peso;
        this.altura = altura;
    }
}
