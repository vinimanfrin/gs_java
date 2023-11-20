package br.com.fiap.infra.configuration.exception;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ExceptionHandling implements ExceptionMapper<Exception> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(Exception exception) {
        // Customize your error response here
        String errorMessage = "INTERNAL SERVER ERROR: " + exception.getMessage();

        final var jsonObject = Json.createObjectBuilder()
                .add( "HOST", uriInfo.getAbsolutePath().getHost() )
                .add( "RESOURCE", uriInfo.getAbsolutePath().getPath() )
                .add( "TYPE", exception.getClass().getSimpleName() )
                .add( "TITLE", "INTERNAL SERVER ERROR" );

        final var jsonArray = Json.createArrayBuilder();

        JsonObject jsonError = Json.createObjectBuilder()
                .add( "MESSAGE", errorMessage )
                .build();
        jsonArray.add( jsonError );

        JsonObject errorJsonEntity = jsonObject.add( "ERRORS", jsonArray.build() ).build();

        return Response
                .status( Response.Status.INTERNAL_SERVER_ERROR )
                .entity( errorJsonEntity )
                .type( MediaType.APPLICATION_JSON_TYPE )
                .build();
    }
}
