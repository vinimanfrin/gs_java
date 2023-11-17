package br.com.fiap.domain.service;

import br.com.fiap.domain.entity.Endereco;
import br.com.fiap.domain.repository.EnderecoRepository;
import javassist.NotFoundException;

import java.util.List;

public class EnderecoService {
    private EnderecoRepository repository = EnderecoRepository.build();

    public List<Endereco> findAll() {
        return repository.findAll();
    }

    public Endereco findById(Long id) {
        return repository.findById(id);
    }

    public Endereco persist(Endereco endereco) {
        return repository.persist(endereco);
    }

    public boolean delete(Long id) {
        return repository.delete(id);
    }

    public Endereco update(Endereco endereco)  {
        return repository.update(endereco);
    }
}
