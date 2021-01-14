package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao {
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static final String URL = "jdbc:mysql://localhost/bdloja?allowPublicKeyRetrieval=true";
    public static final String USER = "root";
    public static final String PASS = "Arvorebinaria123";
    
    public static Connection getConnection(){
        try {
            Class.forName(DRIVER);
            
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException ex) {
            throw new RuntimeException("Erro: ", ex);
        }
    }
    
    public static void fecharConexao(Connection conexao){
        if(conexao != null){
            try {
                conexao.close();
            } catch (SQLException ex) {
                System.err.println("Erro: " + ex);
            }
        }
    }
    
    public static void fecharConexao(Connection conexao, Statement stmt){
        if(stmt != null){
            try {
                stmt.close();
            } catch (SQLException ex) {
                System.err.println("Erro: " + ex);
            }
        }
        
        fecharConexao(conexao);
    }
    public static void fecharConexao(Connection conexao, Statement stmt, ResultSet resultado){
        if(resultado != null){
            try {
                resultado.close();
            } catch (SQLException ex) {
                System.err.println("Erro: " + ex);
            }
        }
        
        fecharConexao(conexao, stmt);
    }
}
