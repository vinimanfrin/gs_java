package br.com.fiap.domain.dto.paciente;

import br.com.fiap.domain.entity.Sexo;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record PersistPacienteDTO (

        @NotBlank(message = "Para o cadastro , o nome deve ser informado")
        String nome,

        @CPF(message = "Por favor , digite um CPF válido")
        @NotBlank(message = "Para o cadastro , o cpf deve ser informado")
        String cpf,

        @NotBlank(message = "Para o cadastro , o email deve ser informado")
        @Email(message = "Por favor , digite um E-mail válido")
        String email,

        @NotNull(message = "Para o cadastro , a data não pode ser nula")
        @Past(message = "A data deve ser anterior à data atual")
        LocalDate dataNascimento,

        @NotNull(message = "Para o cadastro, o sexo do paciente deve ser informado")
        Sexo sexo,

        @NotNull(message = "Para o cadastro , o peso deve ser informado")
        @Min(value = 3,message = "Por favor , digite um valor válido para o peso")
        @Max(value = 300, message = "Por favor , digite um valor abaixo de 300kg")
        double peso,

        @NotNull(message = "Para o cadastro , é necessário informar a altura do paciente")
        double altura
) {
}
