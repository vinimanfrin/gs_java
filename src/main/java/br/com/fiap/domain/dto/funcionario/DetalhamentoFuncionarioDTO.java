package br.com.fiap.domain.dto.funcionario;
import br.com.fiap.domain.dto.redeHospitalar.DetalhamentoRedeHospitalarDTO;
import br.com.fiap.domain.entity.Funcionario;


public record DetalhamentoFuncionarioDTO (
         Long id,
         String nome,
         String cpf,
         String email,
         DetalhamentoRedeHospitalarDTO redeHospitalar
) {
    public DetalhamentoFuncionarioDTO(Funcionario funcionario){
        this(funcionario.getId(), funcionario.getNome(), funcionario.getCpf(), funcionario.getEmail(), new DetalhamentoRedeHospitalarDTO(funcionario.getRedeHospitalar()));
    }

}
