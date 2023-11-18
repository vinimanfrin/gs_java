package br.com.fiap.domain.resource;

import br.com.fiap.domain.dto.funcionario.DetalhamentoFuncionarioDTO;
import br.com.fiap.domain.dto.funcionario.PersistFuncionarioDTO;
import br.com.fiap.domain.dto.funcionario.UpdateFuncionarioDTO;
import br.com.fiap.domain.dto.redeHospitalar.DetalhamentoRedeHospitalarDTO;
import br.com.fiap.domain.dto.redeHospitalar.PersistRedeHospitalarDTO;
import br.com.fiap.domain.dto.redeHospitalar.UpdateRedeHospitalarDTO;
import br.com.fiap.domain.entity.Endereco;
import br.com.fiap.domain.entity.Funcionario;
import br.com.fiap.domain.entity.Paciente;
import br.com.fiap.domain.entity.RedeHospitalar;
import br.com.fiap.domain.service.EnderecoService;
import br.com.fiap.domain.service.FuncionarioService;
import br.com.fiap.domain.service.RedeHospitalarService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Path("/funcionario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FuncionarioResource {

    @Context
    UriInfo uriInfo;
    private FuncionarioService service = new FuncionarioService();

    @GET
    public Response findAll(){
        List<Funcionario> all = service.findAll();
        List<DetalhamentoFuncionarioDTO> dtos = all.stream().map(DetalhamentoFuncionarioDTO::new).collect(Collectors.toList());
        return Response.ok(dtos).build();
    }
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id")Long id){
        Funcionario funcionario = service.findById(id);
        if (Objects.isNull(funcionario)) return Response.status(404).build();
        return Response.ok(new DetalhamentoFuncionarioDTO(funcionario)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id")Long id){
        boolean deleted = service.delete(id);
        if (deleted)return Response.noContent().build();
        return Response.status(404).build();
    }

    @POST
    public Response persist(@Valid final PersistFuncionarioDTO dto){

        Funcionario funcionario = new Funcionario(dto);
        Funcionario persisted = service.persist(funcionario);

        if (Objects.isNull(persisted)) throw new RuntimeException( "Não foi possível persistir o Funcionário" );

        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        URI uri = uriBuilder.path(String.valueOf(persisted.getId())).build();

        return Response.created(uri).entity(new DetalhamentoFuncionarioDTO(funcionario)).build();
    }

    @PUT
    public Response update(@Valid UpdateFuncionarioDTO dto){
        Funcionario funcionario = new Funcionario(dto);
        Funcionario funcionarioAtt = service.update(funcionario);
        if (Objects.isNull(funcionarioAtt)) throw new RuntimeException( "Não foi possível atualizar o Funcionário, verifique se o id informado é válido: "+dto.id());
        return Response.ok(new DetalhamentoFuncionarioDTO(funcionarioAtt)).build();
    }
}
