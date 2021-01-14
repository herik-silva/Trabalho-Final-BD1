package Model.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interface para o Data Acess Object
 * @author herik
 */
public interface DAO {
    
    public Object criar(ResultSet resultado) throws SQLException;
    public Object pesquisarPorPK(Object pk) throws SQLException;
    public void inserir(Object o) throws SQLException;
    public void deletar(Object pk) throws SQLException;
    public void carregar() throws SQLException;
    public void atualizar(Object o) throws SQLException;
}
