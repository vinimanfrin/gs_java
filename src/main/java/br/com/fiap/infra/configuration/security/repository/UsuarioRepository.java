package br.com.fiap.infra.configuration.security.repository;

import br.com.fiap.domain.entity.Funcionario;
import br.com.fiap.domain.service.FuncionarioService;
import br.com.fiap.infra.ConnectionFactory;
import br.com.fiap.infra.configuration.security.entity.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class UsuarioRepository {

    private static final AtomicReference<UsuarioRepository> instance = new AtomicReference<>();
    private ConnectionFactory factory;
    private FuncionarioService serviceFuncionario;
    private UsuarioRepository(){
        this.factory = ConnectionFactory.build();
        this.serviceFuncionario = new FuncionarioService();
    }

    public static UsuarioRepository build(){
        instance.compareAndSet(null, new UsuarioRepository());
        return instance.get();
    }

    public List<Usuario> findAll() {
        List<Usuario> all = new ArrayList<>();
        Connection connection = factory.getConnection();
        Statement st = null;
        ResultSet rs = null;
        try {
            var sql = "SELECT * FROM T_IA_USUARIO";
            st = connection.createStatement();
            rs = st.executeQuery(sql);
            if (rs.isBeforeFirst()){
                while (rs.next()){
                    Long id = rs.getLong("ID_USUARIO");
                    String username = rs.getString("NM_USUARIO");
                    String password  = rs.getString("CD_SENHA");
                    boolean administrador = rs.getBoolean("administrador");
                    boolean habilitado = rs.getBoolean("habilitado");
                    Long idFuncionario = rs.getLong("ID_FUNCIONARIO");
                    Funcionario funcionario = serviceFuncionario.findById(idFuncionario);
                    all.add(new Usuario(id,username,password,administrador,habilitado,funcionario));
                }
            }
        }
        catch (SQLException e){
            System.err.println("Não foi possível realizar a busca por todos os Usuarios");
        }
        finally {
            fecharObjetos(rs,st,connection);
        }
        return all;
    }

    public Usuario findById(Long id) {
        Usuario usuario = null;
        Connection connection = factory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            var sql = "SELECT * FROM T_IA_USUARIO WHERE ID_USUARIO=?";
            ps = connection.prepareStatement(sql);
            ps.setLong(1,id);
            rs = ps.executeQuery();
            if (rs.isBeforeFirst()){
                while (rs.next()){
                    String username = rs.getString("NM_USUARIO");
                    String password  = rs.getString("CD_SENHA");
                    boolean administrador = rs.getBoolean("administrador");
                    boolean habilitado = rs.getBoolean("habilitado");
                    Long idFuncionario = rs.getLong("ID_FUNCIONARIO");
                    Funcionario funcionario = serviceFuncionario.findById(idFuncionario);
                    usuario = new Usuario(id,username,password,administrador,habilitado,funcionario);
                }
            }
            else {
                System.err.println("Usuario não encontrado com o id:"+id);
            }
        }
        catch (SQLException e){
            System.err.println("Não foi possível realizar a consulta no banco de dados "+ e.getMessage());
        }
        finally {
            fecharObjetos(rs,ps,connection);
        }
        return usuario;
    }

    public Usuario findByUsername(String username) {
        Usuario usuario = null;
        Connection connection = factory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            var sql = "SELECT * FROM T_IA_USUARIO WHERE NM_USUARIO = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, username);

            rs = ps.executeQuery();

            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    Long id = rs.getLong("ID_USUARIO");
                    String password = rs.getString("CD_SENHA");
                    boolean administrador = rs.getBoolean("administrador");
                    boolean habilitado = rs.getBoolean("habilitado");
                    Long idFuncionario = rs.getLong("ID_FUNCIONARIO");
                    Funcionario funcionario = serviceFuncionario.findById(idFuncionario);

                    usuario = new Usuario(id, username, password, administrador, habilitado, funcionario);
                }
            } else {
                System.err.println("Usuário não encontrado com o username: " + username);
            }
        } catch (SQLException e) {
            System.err.println("Não foi possível realizar a consulta no banco de dados " + e.getMessage());
        } finally {
            fecharObjetos(rs, ps, connection);
        }

        return usuario;
    }

    public Usuario persist(Usuario usuario) {
        var sql = "BEGIN INSERT INTO T_IA_USUARIO (NM_USUARIO, CD_SENHA, administrador, habilitado,ID_FUNCIONARIO) VALUES (?,?,?,?,?) returning ID_USUARIO into ?; END;";
        Connection connection = factory.getConnection();
        CallableStatement cs = null;
        try{
            cs = connection.prepareCall(sql);
            cs.setString(1,usuario.getUsername());
            cs.setString(2, usuario.getPassword());
            cs.setBoolean(3,usuario.isAdministrador());
            cs.setBoolean(4, usuario.isHabilitado());
            cs.setLong(5,usuario.getFuncionario().getId());

            cs.registerOutParameter(6,Types.BIGINT);
            cs.executeUpdate();
            usuario.setId(cs.getLong(6));
        }
        catch (SQLException e){
            System.err.println("Não foi possível inserir o usuario" + e.getMessage());
        }
        finally {
            fecharObjetos(null,cs,connection);
        }
        if (Objects.isNull(usuario.getId())){
            return null;
        }
        return usuario;
    }
    public boolean delete(Long id) {
        var sql = "DELETE FROM T_IA_USUARIO WHERE ID_USUARIO=?";
        Connection connection = factory.getConnection();
        PreparedStatement ps = null;
        try{
            ps = connection.prepareStatement(sql);
            ps.setLong(1,id);

            var itensRemovidos = ps.executeUpdate();
            if (itensRemovidos > 0) return true;
        }
        catch (SQLException e){
            System.err.println( "Não foi possível executar o delete: \n" + e.getMessage() );
        }
        finally {
            fecharObjetos(null,ps,connection);
        }
        return false;
    }
    public Usuario update(Usuario usuario)   {
        var sql = "UPDATE T_IA_USUARIO SET NM_USUARIO=?, CD_SENHA=?, administrador=?, habilitado=? WHERE ID_USUARIO=?";
        Connection connection = factory.getConnection();
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getPassword());
            ps.setBoolean(3,usuario.isAdministrador());
            ps.setBoolean(4, usuario.isHabilitado());
            ps.setLong(5,usuario.getId());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                return findById(usuario.getId());
            }
        } catch (SQLException e) {
            System.err.println("Não foi possível atualizar o Usuario: " + e.getMessage() + e.getCause() + e.getErrorCode() );
        } finally {
            fecharObjetos(null,ps,connection);
        }
        return null;
    }

    private static void fecharObjetos(ResultSet rs, Statement st, Connection connection) {
        try{
            if (Objects.nonNull(rs) && !rs.isClosed()){
                rs.close();
            }
            st.close();
            connection.close();
        }catch (SQLException e){
            System.err.println("Erro ao encerrar o ResultSet , a Connection e o Statment\n"+e.getMessage());
        }
    }
}
