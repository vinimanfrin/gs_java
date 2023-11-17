package br.com.fiap.domain.dto.endereco;

import br.com.fiap.domain.entity.Endereco;

public record DetalhamentoEnderecoDTO (Long id, String estado, String municipio, String logradouro, String numeroResidencial, String complemento, String cep){

    public DetalhamentoEnderecoDTO(Endereco endereco){
        this(endereco.getId(), endereco.getEstado(), endereco.getMunicipio(), endereco.getLogradouro(), endereco.getNumeroResidencial(), endereco.getComplemento(), endereco.getCep());
    }
}
