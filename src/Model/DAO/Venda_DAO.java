/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Connection.Conexao;
import Model.Entidades.Venda;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Data Acess Object que se conecta com a tabela vendas
 * do Banco de Dados
 * @author herik
 */
public class Venda_DAO implements DAO{

    /**
     * Cria e retorna um objeto Venda
     * @param resultado
     * @return venda
     * @throws SQLException
     */
    @Override
    public Venda criar(ResultSet resultado) throws SQLException {
        Venda venda = new Venda(
                Integer.parseInt(resultado.getString("numero")),
                resultado.getString("data_venda"),
                Integer.parseInt(resultado.getString("codigo_produto")),
                Integer.parseInt(resultado.getString("id_funcionario")),
                Integer.parseInt(resultado.getString("id_cliente")),
                Float.parseFloat(resultado.getString("preco_venda")),
                Integer.parseInt(resultado.getString("quantidade"))
        );
        
        return venda;
    }

    /**
     * Pesquisa a venda pela chave primária na tabela vendas
     * do Banco de Dados
     * @param pk
     * @return venda
     * @throws SQLException
     */
    @Override
    public Venda pesquisarPorPK(Object pk) throws SQLException {
        final String comando = "SELECT * FROM vendas"
                + "WHERE numero="+ (int) pk;
        
        Connection conexao = Conexao.getConnection();
        Venda venda;
        
        Statement stmt = conexao.createStatement();
        ResultSet resultado = stmt.executeQuery(comando);
        
        // Verifica se foi retornado alguma venda do Banco de Dados
        resultado.last();
        if(resultado.getRow()<1){
            return null;
        }
        
        // Criando o objeto Venda
        resultado.first();
        venda = criar(resultado);
        
        // Fechando a conexão com o Banco de Dados
        Conexao.fecharConexao(conexao, stmt, resultado);
        
        return venda;
    }

    /**
     * Insere uma venda na tabela vendas do Banco de Dados
     * @param o
     * @throws SQLException
     */
    @Override
    public void inserir(Object o) throws SQLException {
        Venda venda = (Venda) o;
        Connection conexao = Conexao.getConnection();
        
        final String comando = "INSERT INTO vendas(data_venda, codigo_produto,"
                + "id_funcionario, id_cliente, preco_venda, quantidade)"
                + "VALUES('"+venda.getData_venda() + "',"
                + venda.getCodigo_produto() + ","
                + venda.getId_funcionario() + ", "
                + venda.getId_cliente() + ", "
                + venda.getPreco_venda() + ", "
                + venda.getQuantidade() + ")";
        
        // Inserindo a venda na tabela de vendas
        Statement stmt = conexao.createStatement();
        stmt.executeUpdate(comando);
        
        // Fechando a conexão com o Banco de Dados
        Conexao.fecharConexao(conexao, stmt);
    }

    @Override
    public void deletar(Object pk) throws SQLException {
        Connection conexao = Conexao.getConnection();
        final String comando = "DELETE FROM vendas "
                + "WHERE numero=" + (int) pk;
        
        // Removendo a venda da tabela vendas
        Statement stmt = conexao.createStatement();
        stmt.executeUpdate(comando);
        
        // Fecha a conexão com o Banco de Dados
        Conexao.fecharConexao(conexao, stmt);
    }

    /**
     * Carrega todas as vendas da tabela Vendas
     * @throws SQLException
     */
    @Override
    public void carregar() throws SQLException {
        
        if(Venda.vendas.isEmpty()){
            Connection conexao = Conexao.getConnection();
            final String comando = "SELECT * FROM vendas";
            
            Statement stmt = conexao.createStatement();
            ResultSet resultado = stmt.executeQuery(comando);
            
            // Inserindo as vendas na lista de vendas
            while(resultado.next()){
                Venda venda = criar(resultado);
                Venda.vendas.add(venda);
            }
        }
        else{
            Venda.vendas.clear();
            carregar();
        }
    }

    @Override
    public void atualizar(Object o) throws SQLException {
        Venda venda = (Venda) o;
        final String comando = "UPDATE vendas "
                + "SET data_venda = '" + venda.getData_venda() + "', "
                + "codigo_produto = " + venda.getCodigo_produto() + ", "
                + "id_funcionario = " + venda.getId_funcionario() + ", "
                + "id_cliente = " + venda.getId_cliente() + ", "
                + "preco_venda = " + venda.getPreco_venda() + ", "
                + "quantidade = " + venda.getQuantidade() + " "
                + "WHERE numero = " + venda.getNumero();
        
        Connection conexao = Conexao.getConnection();
        
        // Atualizando os dados da venda
        Statement stmt = conexao.createStatement();
        stmt.executeUpdate(comando);
        
        // Fechando a conexão com o Banco de Dados
        Conexao.fecharConexao(conexao, stmt);
    }
}
