package br.com.fiap.domain.service;

import br.com.fiap.domain.entity.Funcionario;
import br.com.fiap.domain.entity.Paciente;
import br.com.fiap.domain.entity.Prognostico;
import br.com.fiap.domain.repository.PrognosticoRepository;

import java.util.List;
import java.util.Objects;

public class PrognosticoService {
    private PrognosticoRepository repository = PrognosticoRepository.build();
    private FuncionarioService funcionarioService = new FuncionarioService();
    private PacienteService pacienteService = new PacienteService();

    public List<Prognostico> findAll() {
        return repository.findAll();
    }

    public Prognostico findById(Long id) {
        return repository.findById(id);
    }

    public Prognostico persist(Prognostico prognostico) {
        Paciente paciente = pacienteService.findByCpf(prognostico.getPaciente().getCpf());
        Funcionario funcionario = funcionarioService.findById(prognostico.getFuncionario().getId());
        if (Objects.isNull(paciente)) throw new RuntimeException("paciente não encontrado com o cpf"+prognostico.getPaciente().getCpf());
        if (Objects.isNull(funcionario)) return null;
        prognostico.setPaciente(paciente);
        prognostico.setFuncionario(funcionario);
        return repository.persist(prognostico);
    }

    public boolean delete(Long id) {
        return repository.delete(id);
    }

}
