package br.com.fiap;

import br.com.fiap.infra.ConnectionFactory;
import br.com.fiap.infra.configuration.cors.CORSFilter;
import br.com.fiap.infra.configuration.exception.ConstraintViolationExceptionMapper;
import br.com.fiap.infra.configuration.exception.ExceptionHandling;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public class Main {
    private static final String BASE_URI = "http://localhost/api";

    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig()
                .register( CORSFilter.class )
                .register(ExceptionHandling.class)
                .register(ConstraintViolationExceptionMapper.class)
                .register( ConnectionFactory.build() )
                .packages( "br.com.fiap.domain.resource" )
                .packages("br.com.fiap.infra.configuration.security.resource");
        return GrizzlyHttpServerFactory.createHttpServer( URI.create( BASE_URI ), rc );
    }

    public static void main(String[] args) {
        var server = startServer();
        System.out.println( String.format(
                "Sprint 4 app started with endpoints available " +
                        "as %s%nHit Ctrl-C to stop it....", BASE_URI ) );

        try {
            System.in.read();
            server.stop();
        } catch (IOException e) {
            throw new RuntimeException( e );
        }
    }
}