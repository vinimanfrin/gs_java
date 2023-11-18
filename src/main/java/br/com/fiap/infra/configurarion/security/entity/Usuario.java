package br.com.fiap.infra.configurarion.security.entity;

import br.com.fiap.domain.entity.Funcionario;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    private Long id;
    private String username;
    private String password;

    private boolean admin;
    private boolean saude;

    private Funcionario funcionario;

}
