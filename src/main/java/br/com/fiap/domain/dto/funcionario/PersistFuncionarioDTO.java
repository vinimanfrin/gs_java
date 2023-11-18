package br.com.fiap.domain.dto.funcionario;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

public record PersistFuncionarioDTO(
        @NotBlank(message = "para o cadastro , o campo nome deve ser preenchido")
        String nome,
        @NotNull(message = "o campo cpf deve ser preenchido")
        @CPF(message = "por favor , digite um CPF válido")
        String cpf,
        @NotBlank(message = "O campo e-mail deve ser preenchido")
        @Email
        String email,
        @NotNull(message = "o id da rede hospitalar deve ser informado")
        Long idRedeHospitalar
) {
}
