package Model.DAO;


import Connection.Conexao;
import Model.DAO.DAO;
import Model.Entidades.Produto;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author herik
 */
public class Produto_DAO implements DAO{

    @Override
    public Produto criar(ResultSet resultado) throws SQLException {
        Produto produto = new Produto(Integer.parseInt(
                resultado.getString("codigo")),
                resultado.getString("nome"),
                Float.parseFloat(resultado.getString("preco")),
                Integer.parseInt(resultado.getString("estoque")),
                Integer.parseInt(resultado.getString("id_categoria"))
        );
        return produto;
    }

    @Override
    public Produto pesquisarPorPK(Object pk) throws SQLException {
        final String query = "SELECT * FROM produtos WHERE id=";
        Connection conexao = Conexao.getConnection();
        Produto produto;
        
        try{
            Statement stmt = conexao.createStatement();
            
            // Consultando no Banco de Dados na tabela protudos
            ResultSet resultado = stmt.executeQuery(query + (int)pk);
            
            // Verifica se foi retornado algum produto do Banco de Dados
            resultado.last();
            if(resultado.getRow()<1){
                return null;
            }
            
            // Criando o objeto produto
            resultado.first();
            produto = criar(resultado);
            
            // Fechando a conexão com o Banco de Dados
            Conexao.fecharConexao(conexao, stmt, resultado);
            
            return produto;
            
        }catch(SQLException ex){
            System.err.println("Erro: " + ex);
        }
        
        return null;
    }

    /**
     * Insere um produto na tabela produtos.
     * @param o
     * @throws SQLException
     */
    @Override
    public void inserir(Object o) throws SQLException {
        Produto produto = (Produto) o;
        final String comando = "INSERT INTO produtos VALUES("
                + produto.getCodigo() + ", '"
                + produto.getNome() + "', "
                + produto.getPreco() + ", "
                + produto.getEstoque() + ", "
                + produto.getId_categoria() + ")";
        
        Connection conexao = Conexao.getConnection();
        
        // Realizando o Cast para converter o objeto em Produto
        
        try{
            // Inserindo o produto no Banco de Dados
            Statement stmt = conexao.createStatement();
            stmt.executeUpdate(comando);
            
            // Fecha a conexão com o Banco de Dados
            Conexao.fecharConexao(conexao, stmt);
            
        }catch(SQLException ex){
            System.err.println("Erro: " + ex);
        }
    }

    @Override
    public void deletar(Object pk) throws SQLException {
        final String comando = "DELETE FROM produtos WHERE codigo=";
        Connection conexao = Conexao.getConnection();
        
        try{
            // Removendo o produto do Banco de Dados
            Statement stmt = conexao.createStatement();
            stmt.executeUpdate(comando + (int)pk);
            
            // Fecha a conexão com o Banco de Dados
            Conexao.fecharConexao(conexao, stmt);
            
        }catch(SQLException ex){
            System.err.println("Erro: " + ex);
        }
        
    }

    @Override
    public void carregar() throws SQLException {
        final String comando = "SELECT * FROM produtos";
        Connection conexao = Conexao.getConnection();
        
        if(Produto.produtos.isEmpty()){
            Statement stmt = conexao.createStatement();
            ResultSet resultado = stmt.executeQuery(comando);
            
            while(resultado.next()){
                // Criando objeto produto
                Produto produto = criar(resultado);
                
                // Adicionando o produto na lista de produtos.
                Produto.produtos.add(produto);
            }
            
            // Fechando a conexão com o Banco de Dados
            Conexao.fecharConexao(conexao, stmt, resultado);
        }
        else{
            Produto.produtos.clear();
            carregar();
        }
    }

    @Override
    public void atualizar(Object o) throws SQLException {
        Produto produto = (Produto) o;
        final String comando = "UPDATE produtos "
                + "SET nome = '" + produto.getNome() + "', "
                + "preco = " + produto.getPreco() + ", "
                + "estoque = " + produto.getEstoque() + ", "
                + "id_categoria = " + produto.getId_categoria()
                + " WHERE codigo = " + produto.getCodigo();
        
        Connection conexao = Conexao.getConnection();
        
        // Atualizando os dados do Produto
        Statement stmt = conexao.createStatement();
        stmt.executeUpdate(comando);
        
        // Fechando a conexão com o Banco de Dados
        Conexao.fecharConexao(conexao, stmt);
    }
}