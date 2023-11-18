package br.com.fiap.domain.dto.paciente;

import br.com.fiap.domain.entity.Paciente;
import br.com.fiap.domain.entity.Sexo;

import java.time.LocalDate;

public record DetalhamentoPacienteDTO(Long id,
                                      String nome,
                                      String cpf,
                                      Integer idade,
                                      String email,
                                      LocalDate dataNascimento,
                                      Sexo sexo,
                                      double peso,
                                      double altura) {


    public DetalhamentoPacienteDTO(Paciente paciente){
        this(paciente.getId(), paciente.getNome(), paciente.getCpf(), paciente.getIdade(),
                paciente.getEmail(), paciente.getDataNascimento(),paciente.getSexo(), paciente.getPeso(), paciente.getAltura());
    }
}

