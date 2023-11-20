package br.com.fiap.domain.resource;

import br.com.fiap.domain.dto.funcionario.DetalhamentoFuncionarioDTO;
import br.com.fiap.domain.dto.funcionario.PersistFuncionarioDTO;
import br.com.fiap.domain.dto.prognostico.DetalhamentoPrognosticoDTO;
import br.com.fiap.domain.dto.prognostico.PersistPrognosticoDTO;
import br.com.fiap.domain.entity.Funcionario;
import br.com.fiap.domain.entity.Prognostico;
import br.com.fiap.domain.service.FuncionarioService;
import br.com.fiap.domain.service.PrognosticoService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Path("/prognostico")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PrognosticoResource {
    @Context
    UriInfo uriInfo;
    private PrognosticoService service = new PrognosticoService();

    @GET
    public Response findAll(){
        List<Prognostico> all = service.findAll();
        List<DetalhamentoPrognosticoDTO> dtos = all.stream().map(DetalhamentoPrognosticoDTO::new).collect(Collectors.toList());
        return Response.ok(dtos).build();
    }
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id")Long id){
        Prognostico prognostico = service.findById(id);
        if (Objects.isNull(prognostico)) return Response.status(404).build();
        return Response.ok(new DetalhamentoPrognosticoDTO(prognostico)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id")Long id){
        boolean deleted = service.delete(id);
        if (deleted)return Response.noContent().build();
        return Response.status(404).build();
    }

    @POST
    public Response persist(@Valid final PersistPrognosticoDTO dto){

        Prognostico prognostico = new Prognostico(dto);
        Prognostico persisted = service.persist(prognostico);

        if (Objects.isNull(persisted)) throw new RuntimeException( "Não foi possível persistir o Prognostico" );

        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        URI uri = uriBuilder.path(String.valueOf(persisted.getId())).build();

        return Response.created(uri).entity(new DetalhamentoPrognosticoDTO(prognostico)).build();
    }
}
