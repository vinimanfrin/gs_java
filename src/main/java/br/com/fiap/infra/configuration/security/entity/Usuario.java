package br.com.fiap.infra.configuration.security.entity;

import br.com.fiap.domain.entity.Funcionario;
import br.com.fiap.infra.configuration.security.dto.PersistUsuarioDTO;
import br.com.fiap.infra.configuration.security.dto.UpdateUsuarioDTO;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    private Long id;
    private String username;
    private String password;

    private boolean administrador;
    private boolean habilitado;

    private Funcionario funcionario;

    public Usuario(PersistUsuarioDTO dto) {
        this.username = dto.username();
        this.password = dto.password();
        this.administrador = dto.administrador();
        this.habilitado = dto.habilitado();
        this.funcionario = new Funcionario(dto.funcionario());
    }

    public Usuario(UpdateUsuarioDTO dto) {
        this.id = dto.id();
        this.username = dto.username();
        this.password = dto.password();
        this.administrador = dto.administrador();
        this.habilitado = dto.habilitado();
    }
}
