package br.com.fiap.domain.dto.paciente;

import jakarta.validation.constraints.*;

public record UpdatePacienteDTO (
        @NotNull(message = "Para atualizar os dados de um Paciente , seu id deve ser informado")
        Long id,

        @NotBlank(message = "Para a atualização , o nome deve ser informado")
        String nome,

        @NotBlank(message = "Para a atualização , o email deve ser informado")
        @Email(message = "Por favor , digite um E-mail válido")
        String email,

        @NotNull(message = "Para o cadastro , o peso deve ser informado")
        @Min(value = 3,message = "Por favor , digite um valor válido para o peso")
        @Max(value = 300, message = "Por favor , digite um valor abaixo de 300kg")
        double peso,

        @NotNull(message = "Para o cadastro , é necessário informar a altura do paciente")
        double altura



){
}
