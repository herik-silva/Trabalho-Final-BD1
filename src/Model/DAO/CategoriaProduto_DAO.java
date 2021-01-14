/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Connection.Conexao;
import Model.Entidades.CategoriaProduto;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author herik
 */
public class CategoriaProduto_DAO implements DAO{
    @Override
    public CategoriaProduto criar(ResultSet resultado) throws SQLException {
        CategoriaProduto categoria = new CategoriaProduto(Integer.parseInt(
                resultado.getString("id")),
                resultado.getString("descricao")
        );
        
        return categoria;
    }

    @Override
    public CategoriaProduto pesquisarPorPK(Object pk) throws SQLException {
        final String comando = "SELECT * FROM categoria_produto WHERE id=";
        Connection conexao = Conexao.getConnection();
        CategoriaProduto categoria;
        
        // Realizando a consulta no Banco de Dados
        Statement stmt = conexao.createStatement();
        ResultSet resultado = stmt.executeQuery(comando + (int)pk);
        
        // Criando o objeto cargo
        categoria = criar(resultado);
        
        // Fechando a conexão com o Banco de Dados
        Conexao.fecharConexao(conexao, stmt, resultado);
        
        return categoria;
    }

    @Override
    public void inserir(Object o) throws SQLException {
        final String comando = "INSERT INTO categoria_produto(descricao) VALUES(";
        Connection conexao = Conexao.getConnection();
        
        // Realizando o Cast para converter o objeto em Cargo
        CategoriaProduto categoria = (CategoriaProduto) o;
        
        // Inserindo o Cargo no Banco de Dados
        Statement stmt = conexao.createStatement();
        stmt.executeUpdate(comando + categoria.getDescricao() + ")");
        
        // Fechando a conexão com o Banco de Dados
        Conexao.fecharConexao(conexao, stmt);
    }

    @Override
    public void deletar(Object pk) throws SQLException {
        final String comando = "DELETE FROM categoria_produto WHERE id="+(int)pk;
        Connection conexao = Conexao.getConnection();
        
        // Deletando o Cargo do Banco de Dados
        Statement stmt = conexao.createStatement();
        stmt.executeUpdate(comando);
        
        // Fechando a conexão com o Banco de Dados
        Conexao.fecharConexao(conexao, stmt);
    }

    @Override
    public void carregar() throws SQLException {
        if(CategoriaProduto.categorias.isEmpty()){
            final String comando = "SELECT * FROM categoria_produto";
            Connection conexao = Conexao.getConnection();

            // Buscando os cargos armazenados no Banco de Dados
            Statement stmt = conexao.createStatement();
            ResultSet resultado = stmt.executeQuery(comando);

            // Adicionando na lista de cargos
            while(resultado.next()){
                CategoriaProduto categoria = criar(resultado);
                CategoriaProduto.categorias.add(categoria);
            }
        }
        else{
            CategoriaProduto.categorias.clear();
            carregar();
        }
    }

    /**
     * Atualiza a Categoria do Produto no Banco de Dados
     * @param o
     * @throws SQLException
     */
    @Override
    public void atualizar(Object o) throws SQLException {
        CategoriaProduto categoria = (CategoriaProduto) o;
        final String comando = "UPDATE categoria_produto "
                + "SET cpf='"+categoria.getDescricao()+"' "
                + "WHERE id = " +categoria.getId();
        
        Connection conexao = Conexao.getConnection();
        
        // Atualizando os dados do cargo
        Statement stmt = conexao.createStatement();
        stmt.executeUpdate(comando);

        // Fechando a conexão com o Banco de Dados
        Conexao.fecharConexao(conexao, stmt);
    }
}