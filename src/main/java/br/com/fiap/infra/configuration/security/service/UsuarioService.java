package br.com.fiap.infra.configuration.security.service;

import br.com.fiap.domain.entity.Funcionario;
import br.com.fiap.domain.service.FuncionarioService;
import br.com.fiap.infra.configuration.criptografia.Password;
import br.com.fiap.infra.configuration.security.dto.LoginUsuarioDTO;
import br.com.fiap.infra.configuration.security.entity.Usuario;
import br.com.fiap.infra.configuration.security.repository.UsuarioRepository;

import java.util.List;
import java.util.Objects;

public class UsuarioService {
    private UsuarioRepository repository = UsuarioRepository.build();
    private FuncionarioService funcionarioService = new FuncionarioService();

    public List<Usuario> findAll() {
        return repository.findAll();
    }

    public Usuario findById(Long id) {
        return repository.findById(id);
    }

    public Usuario persist(Usuario usuario) {
        usuario.setPassword(Password.encoder(usuario.getPassword()));
        Funcionario funcionario = funcionarioService.persist(usuario.getFuncionario());
        if (Objects.isNull(funcionario)) return null;
        usuario.setFuncionario(funcionario);
        return repository.persist(usuario);
    }

    public boolean delete(Long id) {
        return repository.delete(id);
    }

    public Usuario update(Usuario usuario)  {
        return repository.update(usuario);
    }
    public Usuario autenticar(LoginUsuarioDTO dto){
        Usuario usuario = repository.findByUsername(dto.username());
        if (Objects.isNull(usuario)) return null;
        var password = dto.password();
        boolean autenticado = Password.check(password, usuario.getPassword());
        if (autenticado) {
            return usuario;
        }
        return null;
    }
}
