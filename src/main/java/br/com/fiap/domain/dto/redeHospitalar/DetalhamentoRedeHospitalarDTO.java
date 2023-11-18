package br.com.fiap.domain.dto.redeHospitalar;

import br.com.fiap.domain.dto.endereco.DetalhamentoEnderecoDTO;
import br.com.fiap.domain.entity.RedeHospitalar;

public record DetalhamentoRedeHospitalarDTO (Long id, String razaoSocial, String cnpj, DetalhamentoEnderecoDTO endereco){

    public DetalhamentoRedeHospitalarDTO(RedeHospitalar redeHospitalar){
        this(redeHospitalar.getId(), redeHospitalar.getRazaoSocial(), redeHospitalar.getCnpj(), new DetalhamentoEnderecoDTO(redeHospitalar.getEndereco()));
    }
}
