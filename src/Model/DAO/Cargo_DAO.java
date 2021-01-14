package Model.DAO;

import Connection.Conexao;
import Model.Entidades.Cargo;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Cargo_DAO implements DAO{

    @Override
    public Cargo criar(ResultSet resultado) throws SQLException {
        Cargo cargo = new Cargo(Integer.parseInt(
                resultado.getString("id")),
                resultado.getString("descricao")
        );
        
        return cargo;
    }

    @Override
    public Cargo pesquisarPorPK(Object pk) throws SQLException {
        final String comando = "SELECT * FROM cargos WHERE id=";
        Connection conexao = Conexao.getConnection();
        Cargo cargo;
        
        // Realizando a consulta no Banco de Dados
        Statement stmt = conexao.createStatement();
        ResultSet resultado = stmt.executeQuery(comando + (int)pk);
        
        // Criando o objeto cargo
        cargo = criar(resultado);
        
        // Fechando a conexão com o Banco de Dados
        Conexao.fecharConexao(conexao, stmt, resultado);
        
        return cargo;
    }

    @Override
    public void inserir(Object o) throws SQLException {
        final String comando = "INSERT INTO cargos(descricao) VALUES(";
        Connection conexao = Conexao.getConnection();
        
        // Realizando o Cast para converter o objeto em Cargo
        Cargo cargo = (Cargo) o;
        
        // Inserindo o Cargo no Banco de Dados
        Statement stmt = conexao.createStatement();
        stmt.executeUpdate(comando + cargo.getDescricao() + ")");
        
        // Fechando a conexão com o Banco de Dados
        Conexao.fecharConexao(conexao, stmt);
    }

    @Override
    public void deletar(Object pk) throws SQLException {
        final String comando = "DELETE FROM cargos WHERE id="+(int)pk;
        Connection conexao = Conexao.getConnection();
        
        // Deletando o Cargo do Banco de Dados
        Statement stmt = conexao.createStatement();
        stmt.executeUpdate(comando);
        
        // Fechando a conexão com o Banco de Dados
        Conexao.fecharConexao(conexao, stmt);
    }

    @Override
    public void carregar() throws SQLException {
        if(Cargo.cargos.isEmpty()){
            System.out.println("Vazio bora buscar");
            final String comando = "SELECT * FROM cargos";
            Connection conexao = Conexao.getConnection();

            // Buscando os cargos armazenados no Banco de Dados
            Statement stmt = conexao.createStatement();
            ResultSet resultado = stmt.executeQuery(comando);

            // Adicionando na lista de cargos
            while(resultado.next()){
                Cargo cargo = criar(resultado);
                Cargo.cargos.add(cargo);
            }
        }
        else{
            Cargo.cargos.clear();
            carregar();
        }
    }

    @Override
    public void atualizar(Object o) throws SQLException {
        Cargo cargo = (Cargo) o;
        final String comando = "UPDATE cargos "
                + "SET cpf='"+cargo.getDescricao()+"' "
                + "WHERE id = " +cargo.getId();
        
        Connection conexao = Conexao.getConnection();
        
        // Atualizando os dados do cargo
        Statement stmt = conexao.createStatement();
        stmt.executeUpdate(comando);

        // Fechando a conexão com o Banco de Dados
        Conexao.fecharConexao(conexao, stmt);
    }
}