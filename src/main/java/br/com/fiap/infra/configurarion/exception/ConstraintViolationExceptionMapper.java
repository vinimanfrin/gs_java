package br.com.fiap.infra.configurarion.exception;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Set;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Context
    UriInfo uriInfo;
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();

        final var jsonObject = Json.createObjectBuilder()
                .add( "HOST", uriInfo.getAbsolutePath().getHost() )
                .add( "RESOURCE", uriInfo.getAbsolutePath().getPath() )
                .add( "TYPE", exception.getClass().getSimpleName() )
                .add( "TITLE", "VALIDATION ERROR" );

        final var jsonArray = Json.createArrayBuilder();

        for (final var constraint : constraintViolations) {

            String className = constraint.getLeafBean().toString().split( "@" )[0];
            String message = constraint.getMessage();
            String propertyPath = constraint.getPropertyPath().toString().split( "\\." )[2];

            JsonObject jsonError = Json.createObjectBuilder()
                    .add( "CLASS", className )
                    .add( "FIELD", propertyPath )
                    .add( "MESSAGE", message )
                    .build();
            jsonArray.add( jsonError );
        }

        JsonObject errorJsonEntity = jsonObject.add( "ERRORS", jsonArray.build() ).build();

        return Response.status( Response.Status.BAD_REQUEST )
                .entity( errorJsonEntity )
                .type( MediaType.APPLICATION_JSON_TYPE )
                .build();
    }
}

