package br.com.fiap.infra.configuration.security.dto;

import br.com.fiap.domain.dto.funcionario.DetalhamentoFuncionarioDTO;
import br.com.fiap.infra.configuration.security.entity.Usuario;

public record DetalhamentoUsuarioDTO(
        Long id,
        String username,
        String password,

        boolean administrador,
        boolean habilitado,
        DetalhamentoFuncionarioDTO funcionario
) {
    public DetalhamentoUsuarioDTO (Usuario usuario){
        this(usuario.getId(), usuario.getUsername(), usuario.getPassword(), usuario.isAdministrador(), usuario.isHabilitado(), new DetalhamentoFuncionarioDTO(usuario.getFuncionario()));
    }
}
