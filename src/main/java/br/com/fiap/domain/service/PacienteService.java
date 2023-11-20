package br.com.fiap.domain.service;


import br.com.fiap.domain.entity.Paciente;
import br.com.fiap.domain.repository.PacienteRepository;

import java.util.List;
import java.util.Objects;

public class PacienteService {
    private PacienteRepository repository = PacienteRepository.build();

    public List<Paciente> findAll() {
        return repository.findAll();
    }

    public Paciente findById(Long id) {
        return repository.findById(id);
    }

    public Paciente persist(Paciente paciente) {
        Paciente pacienteComCadastro = repository.findByCpf(paciente.getCpf());
        if (Objects.isNull(pacienteComCadastro)) {
            return repository.persist(paciente);
        }
        throw new RuntimeException("Paciente ja cadastrado");
    }

    public boolean delete(Long id) {
        return repository.delete(id);
    }

    public Paciente update(Paciente paciente)  {
        return repository.update(paciente);
    }

    public Paciente findByCpf(String cpf) {
        return repository.findByCpf(cpf);
    }
}
