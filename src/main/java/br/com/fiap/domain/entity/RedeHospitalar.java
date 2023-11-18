package br.com.fiap.domain.entity;

import br.com.fiap.domain.dto.redeHospitalar.PersistRedeHospitalarDTO;
import br.com.fiap.domain.dto.redeHospitalar.UpdateRedeHospitalarDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RedeHospitalar {


    private Long id;
    private String razaoSocial;
    private String cnpj;
    private Endereco endereco;


    public RedeHospitalar(PersistRedeHospitalarDTO dto) {
        this.razaoSocial = dto.razaoSocial();
        this.cnpj = dto.cnpj();

    }

    public RedeHospitalar(UpdateRedeHospitalarDTO updateRedeHospitalarDTO) {
        this.id = updateRedeHospitalarDTO.id();
        this.razaoSocial = updateRedeHospitalarDTO.razaoSocial();
    }
}
