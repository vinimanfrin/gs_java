package br.com.fiap.domain.entity;


import br.com.fiap.domain.dto.endereco.PersistEnderecoDTO;
import br.com.fiap.domain.dto.endereco.UpdateEnderecoDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {

    private Long id;
    private String estado;
    private String municipio;
    private String logradouro;
    private String numeroResidencial;
    private String complemento;
    private String cep;

    public Endereco(UpdateEnderecoDTO dto) {
        this.id = dto.id();
        this.estado = dto.estado();
        this.municipio = dto.municipio();
        this.logradouro = dto.logradouro();
        this.numeroResidencial = dto.numeroResidencial();
        this.complemento = dto.complemento();
        this.cep = dto.cep();
    }

    public Endereco(PersistEnderecoDTO dto) {
        this.estado = dto.estado();
        this.municipio = dto.municipio();
        this.logradouro = dto.logradouro();
        this.numeroResidencial = dto.numeroResidencial();
        this.complemento = dto.complemento();
        this.cep = dto.cep();
    }
}
