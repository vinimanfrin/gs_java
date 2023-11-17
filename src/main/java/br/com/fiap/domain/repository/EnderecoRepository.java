package br.com.fiap.domain.repository;

import br.com.fiap.domain.entity.Endereco;
import br.com.fiap.infra.ConnectionFactory;
import javassist.NotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class EnderecoRepository {
    private static final AtomicReference<EnderecoRepository> instance = new AtomicReference<>();
    private ConnectionFactory factory;
    private EnderecoRepository(){
        this.factory = ConnectionFactory.build();
    }

    public static EnderecoRepository build(){
        instance.compareAndSet(null, new EnderecoRepository());
        return instance.get();
    }

    public List<Endereco> findAll() {
        List<Endereco> all = new ArrayList<>();
        Connection connection = factory.getConnection();
        Statement st = null;
        ResultSet rs = null;
        try {
            var sql = "SELECT * FROM T_IA_ENDERECO";
            st = connection.createStatement();
            rs = st.executeQuery(sql);
            if (rs.isBeforeFirst()){
                while (rs.next()){
                    Long id = rs.getLong("ID_ENDERECO");
                    String estado = rs.getString("NM_ESTADO");
                    String municipio = rs.getString("NM_MUNICIPIO");
                    String logradouro = rs.getString("NM_LOGRADOURO");
                    String numeroResidencial = rs.getString("NR_NUMERO_RESIDENCIAL");
                    String complemento = rs.getString("DS_COMPLEMENTO");
                    String cep = rs.getString("NR_CEP");
                    all.add(new Endereco(id,estado,municipio,logradouro,numeroResidencial,complemento,cep));
                }
            }
        }
        catch (SQLException e){
            System.err.println("Não foi possível realizar a busca por todos os endereços");
        }
        finally {
            fecharObjetos(rs,st,connection);
        }
        return all;
    }

    public Endereco findById(Long id) {
        Endereco endereco = null;
        Connection connection = factory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            var sql = "SELECT * FROM T_IA_ENDERECO WHERE ID_ENDERECO=?";
            ps = connection.prepareStatement(sql);
            ps.setLong(1,id);
            rs = ps.executeQuery();
            if (rs.isBeforeFirst()){
                while (rs.next()){
                    String estado = rs.getString("NM_ESTADO");
                    String municipio = rs.getString("NM_MUNICIPIO");
                    String logradouro = rs.getString("NM_LOGRADOURO");
                    String numeroResidencial = rs.getString("NR_NUMERO_RESIDENCIAL");
                    String complemento = rs.getString("DS_COMPLEMENTO");
                    String cep = rs.getString("NR_CEP");
                    endereco = new Endereco(id,estado,municipio,logradouro,numeroResidencial,complemento,cep);
                }
            }
            else {
                System.err.println("Endereço não encontrado com o id:"+id);
            }
        }
        catch (SQLException e){
            System.err.println("Não foi possível realizar a consulta no banco de dados "+ e.getMessage());
        }
        finally {
            fecharObjetos(rs,ps,connection);
        }
        return endereco;
    }

    public Endereco persist(Endereco endereco) {
        var sql = "BEGIN INSERT INTO T_IA_ENDERECO (NM_ESTADO, NM_MUNICIPIO, NM_LOGRADOURO, NR_NUMERO_RESIDENCIAL,DS_COMPLEMENTO,NR_CEP) VALUES (?,?,?,?,?,?) returning ID_ENDERECO into ?; END;";
        Connection connection = factory.getConnection();
        CallableStatement cs = null;
        try{
            cs = connection.prepareCall(sql);
            cs.setString(1, endereco.getEstado());
            cs.setString(2, endereco.getMunicipio());
            cs.setString(3, endereco.getLogradouro());
            cs.setString(4, endereco.getNumeroResidencial());
            cs.setString(5, endereco.getComplemento());
            cs.setString(6, endereco.getCep());
            cs.registerOutParameter(7,Types.BIGINT);
            cs.executeUpdate();
            endereco.setId(cs.getLong(7));
        }
        catch (SQLException e){
            System.err.println("Não foi possível inserir o endereço" + e.getMessage());
        }
        finally {
            fecharObjetos(null,cs,connection);
        }
        if (Objects.isNull(endereco.getId())){
            return null;
        }
        return endereco;
    }
    public boolean delete(Long id) {
        var sql = "DELETE FROM T_IA_ENDERECO WHERE ID_ENDERECO=?";
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
    public Endereco update(Endereco endereco)   {
        var sql = "UPDATE T_IA_ENDERECO SET NM_ESTADO=?, NM_MUNICIPIO=?, NM_LOGRADOURO=?, NR_NUMERO_RESIDENCIAL=?, DS_COMPLEMENTO=?, NR_CEP=? WHERE ID_ENDERECO=?";
        Connection connection = factory.getConnection();
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, endereco.getEstado());
            ps.setString(2, endereco.getMunicipio());
            ps.setString(3, endereco.getLogradouro());
            ps.setString(4, endereco.getNumeroResidencial());
            ps.setString(5, endereco.getComplemento());
            ps.setString(6, endereco.getCep());
            ps.setLong(7,endereco.getId());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                return findById(endereco.getId());
            }
        } catch (SQLException e) {
            System.err.println("Não foi possível atualizar o endereco: " + e.getMessage() + e.getCause() + e.getErrorCode() );
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
