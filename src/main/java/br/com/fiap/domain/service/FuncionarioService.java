package br.com.fiap.domain.service;

import br.com.fiap.domain.entity.Funcionario;
import br.com.fiap.domain.entity.RedeHospitalar;
import br.com.fiap.domain.repository.FuncionarioRepository;
import br.com.fiap.domain.repository.RedeHospitalarRepository;


import java.util.List;
import java.util.Objects;

public class FuncionarioService {
    private FuncionarioRepository repository = FuncionarioRepository.build();
    private RedeHospitalarService redeHospitalarService = new RedeHospitalarService();

    public List<Funcionario> findAll() {
        return repository.findAll();
    }

    public Funcionario findById(Long id) {
        return repository.findById(id);
    }

    public Funcionario persist(Funcionario funcionario) {
        RedeHospitalar redeHospitalar = redeHospitalarService.findById(funcionario.getRedeHospitalar().getId());
        if (Objects.isNull(redeHospitalar)) return null;
        funcionario.setRedeHospitalar(redeHospitalar);
        return repository.persist(funcionario);
    }

    public boolean delete(Long id) {
        return repository.delete(id);
    }

    public Funcionario update(Funcionario funcionario)  {
        RedeHospitalar redeHospitalar = redeHospitalarService.findById(funcionario.getRedeHospitalar().getId());
        if (Objects.isNull(redeHospitalar)) return null;
        funcionario.setRedeHospitalar(redeHospitalar);
        return repository.update(funcionario);
    }
}
