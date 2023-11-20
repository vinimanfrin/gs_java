package br.com.fiap.infra.configuration.criptografia;


import javax.enterprise.context.ApplicationScoped;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@ApplicationScoped
public class Password {

    /**
     * Criptografa
     *
     * @param pass
     * @return
     */
    public static String encoder(String pass) {

        MessageDigest algorithm = null;
        StringBuilder hexPass = new StringBuilder();
        try {
            algorithm = MessageDigest.getInstance( "SHA-256" );
            byte[] messageDigestPass = algorithm.digest( pass.getBytes( StandardCharsets.UTF_8 ) );

            //Transformando em hexadecimal
            for (byte b : messageDigestPass) {
                hexPass.append( String.format( "%02X", 0xFF & b ) );
            }

        } catch (NoSuchAlgorithmException e) {
            System.err.println( "Não foi possível utilizar o algoritmo" + algorithm.getAlgorithm() + ":\n" + e.getMessage() );
        }

        return hexPass.toString();
    }

    /**
     *
     *
     * @param pass
     * @param hash
     * @return
     */
    public static boolean check(String pass, String hash) {

        String v = encoder( pass );
        String h = encoder(hash);
        return v.equals( h );
    }
}
