package br.com.fiap.domain.repository;

import br.com.fiap.domain.entity.Endereco;
import br.com.fiap.domain.entity.Paciente;
import br.com.fiap.domain.entity.Sexo;
import br.com.fiap.infra.ConnectionFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class PacienteRepository {
    private static final AtomicReference<PacienteRepository> instance = new AtomicReference<>();
    private ConnectionFactory factory;
    private PacienteRepository(){
        this.factory = ConnectionFactory.build();
    }

    public static PacienteRepository build(){
        instance.compareAndSet(null, new PacienteRepository());
        return instance.get();
    }

    public List<Paciente> findAll() {
        List<Paciente> all = new ArrayList<>();
        Connection connection = factory.getConnection();
        Statement st = null;
        ResultSet rs = null;
        try {
            var sql = "SELECT * FROM T_IA_PACIENTE";
            st = connection.createStatement();
            rs = st.executeQuery(sql);
            if (rs.isBeforeFirst()){
                while (rs.next()){
                    Long id = rs.getLong("ID_PACIENTE");
                    String nome = rs.getString("NM_PACIENTE");
                    String cpf  = rs.getString("NR_CPF");
                    Integer idade = rs.getInt("NR_IDADE");
                    String email = rs.getString("DS_EMAIL");
                    LocalDate dataNascimento = rs.getDate("DT_NASCIMENTO").toLocalDate();
                    Sexo sexo = Sexo.valueOf(rs.getString("TP_SEXO"));
                    double peso = rs.getDouble("NR_PESO");
                    double altura = rs.getDouble("NR_ALTURA");
                    all.add(new Paciente(id,nome,cpf,email,dataNascimento,sexo,peso,altura));
                }
            }
        }
        catch (SQLException e){
            System.err.println("Não foi possível realizar a busca por todos os Pacientes");
        }
        finally {
            fecharObjetos(rs,st,connection);
        }
        return all;
    }

    public Paciente findById(Long id) {
        Paciente paciente = null;
        Connection connection = factory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            var sql = "SELECT * FROM T_IA_PACIENTE WHERE ID_PACIENTE=?";
            ps = connection.prepareStatement(sql);
            ps.setLong(1,id);
            rs = ps.executeQuery();
            if (rs.isBeforeFirst()){
                while (rs.next()){
                    String nome = rs.getString("NM_PACIENTE");
                    String cpf  = rs.getString("NR_CPF");
                    Integer idade = rs.getInt("NR_IDADE");
                    String email = rs.getString("DS_EMAIL");
                    LocalDate dataNascimento = rs.getDate("DT_NASCIMENTO").toLocalDate();
                    Sexo sexo = Sexo.valueOf(rs.getString("TP_SEXO"));
                    double peso = rs.getDouble("NR_PESO");
                    double altura = rs.getDouble("NR_ALTURA");
                    paciente = new Paciente(id,nome,cpf,email,dataNascimento,sexo,peso,altura);
                }
            }
            else {
                System.err.println("Paciente não encontrado com o id:"+id);
            }
        }
        catch (SQLException e){
            System.err.println("Não foi possível realizar a consulta no banco de dados "+ e.getMessage());
        }
        finally {
            fecharObjetos(rs,ps,connection);
        }
        return paciente;
    }
    public Paciente findByCpf(String cpf) {
        Paciente paciente = null;
        Connection connection = factory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            var sql = "SELECT * FROM T_IA_PACIENTE WHERE NR_CPF=?";
            ps = connection.prepareStatement(sql);
            ps.setString(1,cpf);
            rs = ps.executeQuery();
            if (rs.isBeforeFirst()){
                while (rs.next()){
                    Long id = rs.getLong("ID_PACIENTE");
                    String nome = rs.getString("NM_PACIENTE");
                    Integer idade = rs.getInt("NR_IDADE");
                    String email = rs.getString("DS_EMAIL");
                    LocalDate dataNascimento = rs.getDate("DT_NASCIMENTO").toLocalDate();
                    Sexo sexo = Sexo.valueOf(rs.getString("TP_SEXO"));
                    double peso = rs.getDouble("NR_PESO");
                    double altura = rs.getDouble("NR_ALTURA");
                    paciente = new Paciente(id,nome,cpf,email,dataNascimento,sexo,peso,altura);
                }
            }
            else {
                System.err.println("Paciente não encontrado com o cpf:"+cpf);
            }
        }
        catch (SQLException e){
            System.err.println("Não foi possível realizar a consulta no banco de dados "+ e.getMessage());
        }
        finally {
            fecharObjetos(rs,ps,connection);
        }
        return paciente;
    }

    public Paciente persist(Paciente paciente) {
        var sql = "BEGIN INSERT INTO T_IA_PACIENTE (NM_PACIENTE, NR_CPF, NR_IDADE, DS_EMAIL,DT_NASCIMENTO,TP_SEXO, NR_PESO, NR_ALTURA) VALUES (?,?,?,?,?,?,?,?) returning ID_PACIENTE into ?; END;";
        Connection connection = factory.getConnection();
        CallableStatement cs = null;
        try{
            cs = connection.prepareCall(sql);
            cs.setString(1, paciente.getNome());
            cs.setString(2, paciente.getCpf());
            cs.setInt(3,paciente.getIdade());
            cs.setString(4, paciente.getEmail());
            cs.setDate(5,Date.valueOf(paciente.getDataNascimento()));
            cs.setString(6, String.valueOf(paciente.getSexo()));
            cs.setDouble(7,paciente.getPeso());
            cs.setDouble(8,paciente.getAltura());

            cs.registerOutParameter(9,Types.BIGINT);
            cs.executeUpdate();
            paciente.setId(cs.getLong(9));
        }
        catch (SQLException e){
            System.err.println("Não foi possível inserir o paciente" + e.getMessage());
        }
        finally {
            fecharObjetos(null,cs,connection);
        }
        if (Objects.isNull(paciente.getId())){
            return null;
        }
        return paciente;
    }
    public boolean delete(Long id) {
        var sql = "DELETE FROM T_IA_PACIENTE WHERE ID_PACIENTE=?";
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
    public Paciente update(Paciente paciente)   {
        var sql = "UPDATE T_IA_PACIENTE SET NM_PACIENTE=?, DS_EMAIL=?, NR_PESO=?, NR_ALTURA=? WHERE ID_PACIENTE=?";
        Connection connection = factory.getConnection();
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, paciente.getNome());
            ps.setString(2, paciente.getEmail());
            ps.setDouble(3,paciente.getPeso());
            ps.setDouble(4,paciente.getAltura());
            ps.setLong(5,paciente.getId());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                return findById(paciente.getId());
            }
        } catch (SQLException e) {
            System.err.println("Não foi possível atualizar o Paciente: " + e.getMessage() + e.getCause() + e.getErrorCode() );
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
