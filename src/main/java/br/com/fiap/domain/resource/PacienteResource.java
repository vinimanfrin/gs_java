package br.com.fiap.domain.resource;

import br.com.fiap.domain.dto.endereco.DetalhamentoEnderecoDTO;
import br.com.fiap.domain.dto.endereco.PersistEnderecoDTO;
import br.com.fiap.domain.dto.endereco.UpdateEnderecoDTO;
import br.com.fiap.domain.dto.paciente.DetalhamentoPacienteDTO;
import br.com.fiap.domain.dto.paciente.PersistPacienteDTO;
import br.com.fiap.domain.dto.paciente.UpdatePacienteDTO;
import br.com.fiap.domain.entity.Endereco;
import br.com.fiap.domain.entity.Paciente;
import br.com.fiap.domain.service.EnderecoService;
import br.com.fiap.domain.service.PacienteService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Path("/paciente")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PacienteResource {
    @Context
    UriInfo uriInfo;
    private PacienteService service = new PacienteService();


    @GET
    public Response findAll(){
        List<Paciente> all = service.findAll();
        List<DetalhamentoPacienteDTO> detalhamentoPacienteDTOs = all.stream().map(DetalhamentoPacienteDTO::new).collect(Collectors.toList());
        return Response.ok(detalhamentoPacienteDTOs).build();
    }
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id")Long id){
        Paciente paciente = service.findById(id);
        if (Objects.isNull(paciente)) return Response.status(404).build();
        return Response.ok(new DetalhamentoPacienteDTO(paciente)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id")Long id){
        boolean deleted = service.delete(id);
        if (deleted)return Response.noContent().build();
        return Response.status(404).build();
    }

    @POST
    public Response persist(@Valid final PersistPacienteDTO dto){
        Paciente persisted = service.persist(new Paciente(dto));
        if (Objects.isNull(persisted)) throw new RuntimeException( "Não foi possível persistir o Paciente" );

        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        URI uri = uriBuilder.path(String.valueOf(persisted.getId())).build();

        return Response.created(uri).entity(new DetalhamentoPacienteDTO(persisted)).build();
    }

    @PUT
    public Response update(@Valid UpdatePacienteDTO updatePacienteDTO){
        Paciente paciente = new Paciente(updatePacienteDTO);
        paciente.setId(updatePacienteDTO.id());
        Paciente pacienteAtt = service.update(paciente);
        if (Objects.isNull(pacienteAtt)) throw new RuntimeException( "Não foi possível atualizar o Paciente, verifique se o id informado é válido: "+updatePacienteDTO.id());
        return Response.ok(new DetalhamentoPacienteDTO(pacienteAtt)).build();
    }
}
