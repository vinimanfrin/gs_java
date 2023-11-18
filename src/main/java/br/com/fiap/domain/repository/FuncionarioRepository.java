package br.com.fiap.domain.repository;

import br.com.fiap.domain.entity.Endereco;
import br.com.fiap.domain.entity.Funcionario;
import br.com.fiap.domain.entity.Paciente;
import br.com.fiap.domain.entity.RedeHospitalar;
import br.com.fiap.domain.service.EnderecoService;
import br.com.fiap.domain.service.RedeHospitalarService;
import br.com.fiap.infra.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class FuncionarioRepository {
    private static final AtomicReference<FuncionarioRepository> instance = new AtomicReference<>();
    private ConnectionFactory factory;
    private RedeHospitalarService redeHospitalarService;
    private FuncionarioRepository(){
        this.factory = ConnectionFactory.build();
        this.redeHospitalarService = new RedeHospitalarService();
    }

    public static FuncionarioRepository build(){
        instance.compareAndSet(null, new FuncionarioRepository());
        return instance.get();
    }

    public List<Funcionario> findAll() {
        List<Funcionario> all = new ArrayList<>();
        Connection connection = factory.getConnection();
        Statement st = null;
        ResultSet rs = null;
        try {
            var sql = "SELECT * FROM T_IA_FUNCIONARIO";
            st = connection.createStatement();
            rs = st.executeQuery(sql);
            if (rs.isBeforeFirst()){
                while (rs.next()){
                    Long id = rs.getLong("ID_FUNCIONARIO");
                    String nome = rs.getString("NM_FUNCIONARIO");
                    String cpf = rs.getString("NR_CPF");
                    String email = rs.getString("DS_EMAIL");
                    Long idRedeHospitalar = rs.getLong("ID_REDE_HOSPITALAR");
                    RedeHospitalar redeHospitalar = redeHospitalarService.findById(idRedeHospitalar);
                    all.add(new Funcionario(id,nome,cpf,email,redeHospitalar));
                }
            }
        }
        catch (SQLException e){
            System.err.println("Não foi possível realizar a busca por todas os Funcionários");
        }
        finally {
            fecharObjetos(rs,st,connection);
        }
        return all;
    }

    public Funcionario findById(Long id) {
        Funcionario funcionario = null;
        Connection connection = factory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            var sql = "SELECT * FROM T_IA_FUNCIONARIO WHERE ID_FUNCIONARIO=?";
            ps = connection.prepareStatement(sql);
            ps.setLong(1,id);
            rs = ps.executeQuery();
            if (rs.isBeforeFirst()){
                while (rs.next()){
                    String nome = rs.getString("NM_FUNCIONARIO");
                    String cpf = rs.getString("NR_CPF");
                    String email = rs.getString("DS_EMAIL");
                    Long idRedeHospitalar = rs.getLong("ID_REDE_HOSPITALAR");
                    RedeHospitalar redeHospitalar = redeHospitalarService.findById(idRedeHospitalar);
                    funcionario = new Funcionario(id,nome,cpf,email,redeHospitalar);
                }
            }
            else {
                System.err.println("Funcionario não encontrada com o id:"+id);
            }
        }
        catch (SQLException e){
            System.err.println("Não foi possível realizar a consulta no banco de dados "+ e.getMessage());
        }
        finally {
            fecharObjetos(rs,ps,connection);
        }
        return funcionario;
    }

    public Funcionario persist(Funcionario funcionario) {
        System.out.println("entrei no repository");
        System.out.println(funcionario.getRedeHospitalar());
        var sql = "BEGIN INSERT INTO T_IA_FUNCIONARIO (NM_FUNCIONARIO, NR_CPF, DS_EMAIL,ID_REDE_HOSPITALAR) VALUES (?,?,?,?) returning ID_FUNCIONARIO into ?; END;";
        Connection connection = factory.getConnection();
        CallableStatement cs = null;
        try{
            cs = connection.prepareCall(sql);
            cs.setString(1, funcionario.getNome());
            cs.setString(2, funcionario.getCpf());
            cs.setString(3, funcionario.getEmail());
            cs.setLong(4,funcionario.getRedeHospitalar().getId());

            cs.registerOutParameter(5,Types.BIGINT);
            cs.executeUpdate();
            funcionario.setId(cs.getLong(5));
        }
        catch (SQLException e){
            System.err.println("Não foi possível inserir o Funcionario" + e.getMessage());
        }
        finally {
            fecharObjetos(null,cs,connection);
        }
        if (Objects.isNull(funcionario.getId())){
            return null;
        }
        return funcionario;
    }
    public boolean delete(Long id) {
        var sql = "DELETE FROM T_IA_FUNCIONARIO WHERE ID_FUNCIONARIO=?";
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
    public Funcionario update(Funcionario funcionario)   {
        var sql = "UPDATE T_IA_FUNCIONARIO SET DS_EMAIL=? , ID_REDE_HOSPITALAR=?  WHERE ID_FUNCIONARIO=?";
        Connection connection = factory.getConnection();
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, funcionario.getEmail());
            ps.setLong(2,funcionario.getRedeHospitalar().getId());
            ps.setLong(3,funcionario.getId());
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                return findById(funcionario.getId());
            }
        } catch (SQLException e) {
            System.err.println("Não foi possível atualizar o Funcionário: " + e.getMessage() + e.getCause() + e.getErrorCode() );
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
