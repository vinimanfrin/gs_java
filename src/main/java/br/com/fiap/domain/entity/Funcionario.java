package br.com.fiap.domain.entity;

import br.com.fiap.domain.dto.funcionario.PersistFuncionarioDTO;
import br.com.fiap.domain.dto.funcionario.UpdateFuncionarioDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario {


    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private RedeHospitalar redeHospitalar;


    public Funcionario(PersistFuncionarioDTO dto) {
        this.nome  = dto.nome();
        this.cpf = dto.cpf();
        this.email = dto.email();
        this.redeHospitalar = new RedeHospitalar();
        redeHospitalar.setId(dto.idRedeHospitalar());
    }

    public Funcionario(UpdateFuncionarioDTO dto) {
        this.id = dto.id();
        this.email = dto.email();
        this.redeHospitalar = new RedeHospitalar();
        redeHospitalar.setId(dto.idRedeHospitalar());
    }


}
