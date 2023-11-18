package br.com.fiap.domain.service;

import br.com.fiap.domain.entity.Paciente;
import br.com.fiap.domain.entity.RedeHospitalar;
import br.com.fiap.domain.repository.PacienteRepository;
import br.com.fiap.domain.repository.RedeHospitalarRepository;

import java.util.List;

public class RedeHospitalarService {
    private RedeHospitalarRepository repository = RedeHospitalarRepository.build();

    public List<RedeHospitalar> findAll() {
        return repository.findAll();
    }

    public RedeHospitalar findById(Long id) {
        return repository.findById(id);
    }

    public RedeHospitalar persist(RedeHospitalar redeHospitalar) {
        return repository.persist(redeHospitalar);
    }

    public boolean delete(Long id) {
        return repository.delete(id);
    }

    public RedeHospitalar update(RedeHospitalar redeHospitalar)  {
        return repository.update(redeHospitalar);
    }
}
