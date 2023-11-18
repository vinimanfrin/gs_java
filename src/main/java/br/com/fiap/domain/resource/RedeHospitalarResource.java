package br.com.fiap.domain.resource;

import br.com.fiap.domain.dto.paciente.DetalhamentoPacienteDTO;
import br.com.fiap.domain.dto.paciente.PersistPacienteDTO;
import br.com.fiap.domain.dto.paciente.UpdatePacienteDTO;
import br.com.fiap.domain.dto.redeHospitalar.DetalhamentoRedeHospitalarDTO;
import br.com.fiap.domain.dto.redeHospitalar.PersistRedeHospitalarDTO;
import br.com.fiap.domain.dto.redeHospitalar.UpdateRedeHospitalarDTO;
import br.com.fiap.domain.entity.Endereco;
import br.com.fiap.domain.entity.Paciente;
import br.com.fiap.domain.entity.RedeHospitalar;
import br.com.fiap.domain.service.EnderecoService;
import br.com.fiap.domain.service.PacienteService;
import br.com.fiap.domain.service.RedeHospitalarService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Path("/rede")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RedeHospitalarResource {

    @Context
    UriInfo uriInfo;
    private RedeHospitalarService service = new RedeHospitalarService();
    private EnderecoService enderecoService = new EnderecoService();


    @GET
    public Response findAll(){
        List<RedeHospitalar> all = service.findAll();
        List<DetalhamentoRedeHospitalarDTO> redeHospitalarDTOS = all.stream().map(DetalhamentoRedeHospitalarDTO::new).collect(Collectors.toList());
        return Response.ok(redeHospitalarDTOS).build();
    }
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id")Long id){
        RedeHospitalar redeHospitalar = service.findById(id);
        if (Objects.isNull(redeHospitalar)) return Response.status(404).build();
        return Response.ok(new DetalhamentoRedeHospitalarDTO(redeHospitalar)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id")Long id){
        boolean deleted = service.delete(id);
        if (deleted)return Response.noContent().build();
        return Response.status(404).build();
    }

    @POST
    public Response persist(@Valid final PersistRedeHospitalarDTO dto){
        Endereco endereco = enderecoService.persist(new Endereco(dto.endereco()));
        RedeHospitalar redeHospitalar = new RedeHospitalar(dto);
        redeHospitalar.setEndereco(endereco);
        RedeHospitalar persisted = service.persist(redeHospitalar);

        if (Objects.isNull(persisted)) throw new RuntimeException( "Não foi possível persistir a Rede" );

        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        URI uri = uriBuilder.path(String.valueOf(persisted.getId())).build();

        return Response.created(uri).entity(new DetalhamentoRedeHospitalarDTO(persisted)).build();
    }

    @PUT
    public Response update(@Valid UpdateRedeHospitalarDTO updateRedeHospitalarDTO){
        RedeHospitalar redeHospitalar = new RedeHospitalar(updateRedeHospitalarDTO);
        redeHospitalar.setId(updateRedeHospitalarDTO.id());
        RedeHospitalar redeHospitalarAtt = service.update(redeHospitalar);
        if (Objects.isNull(redeHospitalarAtt)) throw new RuntimeException( "Não foi possível atualizar a Rede, verifique se o id informado é válido: "+updateRedeHospitalarDTO.id());
        return Response.ok(new DetalhamentoRedeHospitalarDTO(redeHospitalarAtt)).build();
    }
}
