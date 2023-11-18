package br.com.fiap.domain.repository;

import br.com.fiap.domain.entity.Endereco;
import br.com.fiap.domain.entity.Paciente;
import br.com.fiap.domain.entity.RedeHospitalar;
import br.com.fiap.domain.entity.Sexo;
import br.com.fiap.domain.service.EnderecoService;
import br.com.fiap.infra.ConnectionFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class RedeHospitalarRepository {
    private static final AtomicReference<RedeHospitalarRepository> instance = new AtomicReference<>();
    private ConnectionFactory factory;
    private EnderecoService enderecoService;
    private RedeHospitalarRepository(){
        this.factory = ConnectionFactory.build();
        this.enderecoService = new EnderecoService();
    }

    public static RedeHospitalarRepository build(){
        instance.compareAndSet(null, new RedeHospitalarRepository());
        return instance.get();
    }

    public List<RedeHospitalar> findAll() {
        List<RedeHospitalar> all = new ArrayList<>();
        Connection connection = factory.getConnection();
        Statement st = null;
        ResultSet rs = null;
        try {
            var sql = "SELECT * FROM T_IA_RED_HOSPITALAR";
            st = connection.createStatement();
            rs = st.executeQuery(sql);
            if (rs.isBeforeFirst()){
                while (rs.next()){
                    Long id = rs.getLong("ID_REDE_HOSPITALAR");
                    String razaoSocial = rs.getString("DS_RAZAO_SOCIAL");
                    String cnpj = rs.getString("NR_CNPJ");
                    Long idEndereco = rs.getLong("ID_ENDERECO");
                    Endereco endereco = enderecoService.findById(idEndereco);
                    all.add(new RedeHospitalar(id,razaoSocial,cnpj,endereco));
                }
            }
        }
        catch (SQLException e){
            System.err.println("Não foi possível realizar a busca por todas as Redes Hospitalares");
        }
        finally {
            fecharObjetos(rs,st,connection);
        }
        return all;
    }

    public RedeHospitalar findById(Long id) {
        RedeHospitalar redeHospitalar = null;
        Connection connection = factory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            var sql = "SELECT * FROM T_IA_RED_HOSPITALAR WHERE ID_REDE_HOSPITALAR=?";
            ps = connection.prepareStatement(sql);
            ps.setLong(1,id);
            rs = ps.executeQuery();
            if (rs.isBeforeFirst()){
                while (rs.next()){
                    String razaoSocial = rs.getString("DS_RAZAO_SOCIAL");
                    String cnpj = rs.getString("NR_CNPJ");
                    Long idEndereco = rs.getLong("ID_ENDERECO");
                    Endereco endereco = enderecoService.findById(idEndereco);
                    redeHospitalar = new RedeHospitalar(id,razaoSocial,cnpj,endereco);
                }
            }
            else {
                System.err.println("Rede não encontrada com o id:"+id);
            }
        }
        catch (SQLException e){
            System.err.println("Não foi possível realizar a consulta no banco de dados "+ e.getMessage());
        }
        finally {
            fecharObjetos(rs,ps,connection);
        }
        return redeHospitalar;
    }

    public RedeHospitalar persist(RedeHospitalar redeHospitalar) {
        var sql = "BEGIN INSERT INTO T_IA_RED_HOSPITALAR (DS_RAZAO_SOCIAL, NR_CNPJ, ID_ENDERECO) VALUES (?,?,?) returning ID_REDE_HOSPITALAR into ?; END;";
        Connection connection = factory.getConnection();
        CallableStatement cs = null;
        try{
            cs = connection.prepareCall(sql);
            cs.setString(1, redeHospitalar.getRazaoSocial());
            cs.setString(2, redeHospitalar.getCnpj());
            cs.setLong(3,redeHospitalar.getEndereco().getId());


            cs.registerOutParameter(4,Types.BIGINT);
            cs.executeUpdate();
            redeHospitalar.setId(cs.getLong(4));
        }
        catch (SQLException e){
            System.err.println("Não foi possível inserir a Rede" + e.getMessage());
        }
        finally {
            fecharObjetos(null,cs,connection);
        }
        if (Objects.isNull(redeHospitalar.getId())){
            return null;
        }
        return redeHospitalar;
    }
    public boolean delete(Long id) {
        var sql = "DELETE FROM T_IA_RED_HOSPITALAR WHERE ID_REDE_HOSPITALAR=?";
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
    public RedeHospitalar update(RedeHospitalar redeHospitalar)   {
        var sql = "UPDATE T_IA_RED_HOSPITALAR SET DS_RAZAO_SOCIAL=? WHERE ID_REDE_HOSPITALAR=?";
        Connection connection = factory.getConnection();
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, redeHospitalar.getRazaoSocial());
            ps.setLong(2,redeHospitalar.getId());
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                return findById(redeHospitalar.getId());
            }
        } catch (SQLException e) {
            System.err.println("Não foi possível atualizar a Rede: " + e.getMessage() + e.getCause() + e.getErrorCode() );
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
