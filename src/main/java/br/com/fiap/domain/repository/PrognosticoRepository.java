package br.com.fiap.domain.repository;

import br.com.fiap.domain.entity.*;
import br.com.fiap.domain.service.FuncionarioService;
import br.com.fiap.domain.service.PacienteService;
import br.com.fiap.infra.ConnectionFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class PrognosticoRepository {
    private static final AtomicReference<PrognosticoRepository> instance = new AtomicReference<>();
    private ConnectionFactory factory;
    private PacienteService pacienteService;
    private FuncionarioService funcionarioService;
    private PrognosticoRepository(){
        this.factory = ConnectionFactory.build();
        this.pacienteService = new PacienteService();
        this.funcionarioService = new FuncionarioService();
    }

    public static PrognosticoRepository build(){
        instance.compareAndSet(null, new PrognosticoRepository());
        return instance.get();
    }

    public List<Prognostico> findAll() {
        List<Prognostico> all = new ArrayList<>();
        Connection connection = factory.getConnection();
        Statement st = null;
        ResultSet rs = null;
        try {
            var sql = "SELECT * FROM T_IA_PROGNOSTICO";
            st = connection.createStatement();
            rs = st.executeQuery(sql);
            if (rs.isBeforeFirst()){
                while (rs.next()){
                    Long id = rs.getLong("ID_PROGNOSTICO");
                    LocalDateTime data = rs.getTimestamp("DT_PROGNOSTICO").toLocalDateTime();
                    ResultadoPrognostico resultado = ResultadoPrognostico.valueOf(rs.getString("RS_PROGNOSTICO"));
                    String observacoes = rs.getString("DS_OBSERVACOES");
                    Long idPaciente = rs.getLong("ID_PACIENTE");
                    Long idFuncionario = rs.getLong("ID_FUNCIONARIO");
                    Paciente paciente = pacienteService.findById(idPaciente);
                    Funcionario funcionario = funcionarioService.findById(idFuncionario);
                    all.add(new Prognostico(id,data,resultado,observacoes,paciente,funcionario));
                }
            }
        }
        catch (SQLException e){
            System.err.println("Não foi possível realizar a busca por todas os Prognosticos");
        }
        finally {
            fecharObjetos(rs,st,connection);
        }
        return all;
    }

    public Prognostico findById(Long id) {
        Prognostico prognostico = null;
        Connection connection = factory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            var sql = "SELECT * FROM T_IA_PROGNOSTICO WHERE ID_PROGNOSTICO=?";
            ps = connection.prepareStatement(sql);
            ps.setLong(1,id);
            rs = ps.executeQuery();
            if (rs.isBeforeFirst()){
                while (rs.next()){
                    LocalDateTime data = rs.getTimestamp("DT_PROGNOSTICO").toLocalDateTime();
                    ResultadoPrognostico resultado = ResultadoPrognostico.valueOf(rs.getString("RS_PROGNOSTICO"));
                    String observacoes = rs.getString("DS_OBSERVACOES");
                    Long idPaciente = rs.getLong("ID_PACIENTE");
                    Long idFuncionario = rs.getLong("ID_FUNCIONARIO");
                    Paciente paciente = pacienteService.findById(idPaciente);
                    Funcionario funcionario = funcionarioService.findById(idFuncionario);
                    prognostico = new Prognostico(id,data,resultado,observacoes,paciente,funcionario);
                }
            }
            else {
                System.err.println("Prognostico não encontrado com o id:"+id);
            }
        }
        catch (SQLException e){
            System.err.println("Não foi possível realizar a consulta no banco de dados "+ e.getMessage());
        }
        finally {
            fecharObjetos(rs,ps,connection);
        }
        return prognostico;
    }

    public Prognostico persist(Prognostico prognostico) {
        var sql = "BEGIN INSERT INTO T_IA_PROGNOSTICO (DT_PROGNOSTICO, RS_PROGNOSTICO, DS_OBSERVACOES,ID_PACIENTE, ID_FUNCIONARIO) VALUES (?,?,?,?,?) returning ID_PROGNOSTICO into ?; END;";
        Connection connection = factory.getConnection();
        CallableStatement cs = null;
        try{
            cs = connection.prepareCall(sql);
            cs.setTimestamp(1, Timestamp.valueOf(prognostico.getData()));
            cs.setString(2,prognostico.getResultadoPrognostico().toString());
            cs.setString(3, prognostico.getObservacoes());
            cs.setLong(4,prognostico.getPaciente().getId());
            cs.setLong(5,prognostico.getFuncionario().getId());
            cs.registerOutParameter(6,Types.BIGINT);
            cs.executeUpdate();
            prognostico.setId(cs.getLong(6));
        }
        catch (SQLException e){
            System.err.println("Não foi possível inserir o Prognóstico" + e.getMessage());
        }
        finally {
            fecharObjetos(null,cs,connection);
        }
        if (Objects.isNull(prognostico.getId())){
            return null;
        }
        return prognostico;
    }
    public boolean delete(Long id) {
        var sql = "DELETE FROM T_IA_PROGNOSTICO WHERE ID_PROGNOSTICO=?";
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
