package br.com.fiap.domain.resource;

import br.com.fiap.domain.dto.endereco.DetalhamentoEnderecoDTO;
import br.com.fiap.domain.dto.endereco.PersistEnderecoDTO;
import br.com.fiap.domain.dto.endereco.UpdateEnderecoDTO;
import br.com.fiap.domain.entity.Endereco;
import br.com.fiap.domain.service.EnderecoService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Path("/endereco")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EnderecoResource {

    @Context
    UriInfo uriInfo;
    private EnderecoService service = new EnderecoService();


    @GET
    public Response findAll(){
        List<Endereco> all = service.findAll();
        List<DetalhamentoEnderecoDTO> detalhamentoEnderecoDTOS = all.stream().map(DetalhamentoEnderecoDTO::new).collect(Collectors.toList());
        return Response.ok(detalhamentoEnderecoDTOS).build();
    }
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id")Long id){
        Endereco endereco = service.findById(id);
        if (Objects.isNull(endereco)) throw new NotFoundException( "Não temos Endereço cadastrado com o id: " + id );
        return Response.ok(new DetalhamentoEnderecoDTO(endereco)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id")Long id){
        boolean deleted = service.delete(id);
        if (deleted)return Response.noContent().build();
        return Response.status(404).build();
    }

    @POST
    public Response persist(@Valid final PersistEnderecoDTO dto){
        Endereco persisted = service.persist(new Endereco(dto));
        if (Objects.isNull(persisted)) throw new RuntimeException( "Não foi possível persistir o Endereço" );

        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        URI uri = uriBuilder.path(String.valueOf(persisted.getId())).build();

        return Response.created(uri).entity(new DetalhamentoEnderecoDTO(persisted)).build();
    }

    @PUT
    public Response update(@Valid UpdateEnderecoDTO updateEnderecoDTO){
        Endereco endereco = new Endereco(updateEnderecoDTO);
        endereco.setId(updateEnderecoDTO.id());
        Endereco enderecoAtt = service.update(endereco);
        if (Objects.isNull(enderecoAtt)) throw new RuntimeException( "Não foi possível atualizar o Endereço, verifique se o id informado é válido: "+updateEnderecoDTO.id());
        return Response.ok(new DetalhamentoEnderecoDTO(enderecoAtt)).build();
    }
}
