package br.com.fiap.infra.configuration.security.resource;

import br.com.fiap.infra.configuration.security.dto.DetalhamentoUsuarioDTO;
import br.com.fiap.infra.configuration.security.dto.LoginUsuarioDTO;
import br.com.fiap.infra.configuration.security.dto.PersistUsuarioDTO;
import br.com.fiap.infra.configuration.security.dto.UpdateUsuarioDTO;
import br.com.fiap.infra.configuration.security.entity.Usuario;
import br.com.fiap.infra.configuration.security.service.UsuarioService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Path("/usuario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {
    @Context
    UriInfo uriInfo;
    private UsuarioService service = new UsuarioService();

    @GET
    public Response findAll(){
        List<Usuario> all = service.findAll();
        List<DetalhamentoUsuarioDTO> dtos = all.stream().map(DetalhamentoUsuarioDTO::new).collect(Collectors.toList());
        return Response.ok(dtos).build();
    }
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id")Long id){
        Usuario usuario = service.findById(id);
        if (Objects.isNull(usuario)) return Response.status(404).build();
        return Response.ok(new DetalhamentoUsuarioDTO(usuario)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id")Long id){
        boolean deleted = service.delete(id);
        if (deleted)return Response.noContent().build();
        return Response.status(404).build();
    }

    @POST
    public Response persist(@Valid final PersistUsuarioDTO dto){

        Usuario usuario = new Usuario(dto);
        Usuario persisted = service.persist(usuario);

        if (Objects.isNull(persisted)) throw new RuntimeException( "Não foi possível persistir o Usuário" );

        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        URI uri = uriBuilder.path(String.valueOf(persisted.getId())).build();

        return Response.created(uri).entity(new DetalhamentoUsuarioDTO(usuario)).build();
    }

    @PUT
    public Response update(@Valid UpdateUsuarioDTO dto){
        Usuario usuario = new Usuario(dto);
        Usuario usuarioAtt = service.update(usuario);
        if (Objects.isNull(usuarioAtt)) throw new RuntimeException( "Não foi possível atualizar o Usuario, verifique se o id informado é válido: "+dto.id());
        return Response.ok(new DetalhamentoUsuarioDTO(usuarioAtt)).build();
    }

    @Path("/login")
    @POST
    public Response login(@Valid LoginUsuarioDTO dto) {
        Usuario usuarioAutenticado = service.autenticar(dto);
        if (Objects.isNull(usuarioAutenticado))
            return Response.status(401).build();
        return Response.ok(new DetalhamentoUsuarioDTO(usuarioAutenticado)).build();
    }

}
