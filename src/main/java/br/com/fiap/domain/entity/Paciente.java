package br.com.fiap.domain.entity;

import br.com.fiap.domain.dto.paciente.PersistPacienteDTO;
import br.com.fiap.domain.dto.paciente.UpdatePacienteDTO;
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

    private Long id;

    private String nome;
    private String cpf;

    @Setter(AccessLevel.NONE)
    private Integer idade;

    private String email;
    private LocalDate dataNascimento;
    private Sexo sexo;
    private double peso;
    private double altura;

    public Paciente( Long id,String nome, String cpf, String email, LocalDate dataNascimento, Sexo sexo, double peso, double altura) {
        LocalDate dataAtual = LocalDate.now();
        Period period = Period.between(dataNascimento,dataAtual);

        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.idade = period.getYears();
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.peso = peso;
        this.altura = altura;
    }

    public Paciente(PersistPacienteDTO dto) {
        LocalDate dataAtual = LocalDate.now();
        Period period = Period.between(dto.dataNascimento(),dataAtual);

        this.nome = dto.nome();
        this.cpf = dto.cpf();
        this.idade = period.getYears();
        this.email = dto.email();
        this.dataNascimento = dto.dataNascimento();
        this.sexo = dto.sexo();
        this.peso = dto.peso();
        this.altura = dto.altura();
    }

    public Paciente(UpdatePacienteDTO dto) {
        this.id = dto.id();
        this.nome = dto.nome();
        this.cpf = null;
        this.idade = null;
        this.email = dto.email();
        this.dataNascimento = null;
        this.sexo = null;
        this.peso = dto.peso();
        this.altura = dto.altura();
    }
}
