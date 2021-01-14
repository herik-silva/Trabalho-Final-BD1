package Model.DAO;

import Connection.Conexao;
import Model.Entidades.Cliente;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Data Acess Object que se conecta as tabelas pessoas e clientes
 * do Banco de Dados
 * @author herik
 */
public class Cliente_DAO implements DAO{

    /**
     * Cria e retorna um objeto Cliente
     * @param resultado
     * @return cliente
     * @throws SQLException
     */
    @Override
    public Cliente criar(ResultSet resultado) throws SQLException {
        Cliente cliente = new Cliente(
                resultado.getString("cpf"),
                resultado.getString("nome"),
                resultado.getString("tel_contato"),
                resultado.getString("data_nascimento"),
                resultado.getString("endereco")
        );
        
        // Verifica se o campo cartao não esta vazio
        if(resultado.getString("cartao") != null){
            cliente.setCartao(resultado.getString("cartao"));
        }
        
        // Verifica se o campo email não esta vazio
        if(resultado.getString("email") != null){
            cliente.setEmail(resultado.getString("email"));
        }
        
        return cliente;
    }

    /**
     * Pesquisa o cliente pela chave primária no Banco de Dados
     * @param pk
     * @return cliente
     * @throws SQLException
     */
    @Override
    public Cliente pesquisarPorPK(Object pk) throws SQLException {
        final String comando = "SELECT * FROM clientesLoja "
                + "WHERE cpf='" + (String)pk + "'";
        
        Connection conexao = Conexao.getConnection();
        Cliente cliente;
        
        Statement stmt = conexao.createStatement();
        ResultSet resultado = stmt.executeQuery(comando);
        
        // Verifica se foi retornado algum cliente do Banco de Dados
        resultado.last();
        if(resultado.getRow()<1){
            return null;
        }
        
        // Criando o objeto cliente
        resultado.first();
        cliente = criar(resultado);
        
        // Fechando a conexão com o Banco de Dados
        Conexao.fecharConexao(conexao, stmt, resultado);
        
        return cliente;
    }

    /**
     * Insere um cliente na tabela clientes do Banco de Dados
     * @param o
     * @throws SQLException
     */
    @Override
    public void inserir(Object o) throws SQLException {
        Connection conexao = Conexao.getConnection();
        Cliente cliente = (Cliente) o;
        
        // Comando que insere dodos na tabela Pessoas
        final String comandoPessoas = "INSERT INTO pessoas VALUES('"
                + cliente.getCpf()+"','"
                + cliente.getNome()+"','"
                + cliente.getTel_contato()+"','"
                + cliente.getEndereco()+"','"
                + cliente.getData_nascimento()+"')";
        
        // Comando que insere dados na tabela Clientes
        final String comandoClientes = "INSERT INTO clientes(cpf, email, cartao)"
                + "VALUES('"+cliente.getCpf()+"','"
                + cliente.getEmail()+"','"
                + cliente.getCartao()+"')";
        
        Statement stmt = conexao.createStatement();
        
        // Inserindo na tabela Pessoas
        stmt.executeUpdate(comandoPessoas);
        
        // Inserindo na tabela Clientes
        stmt.executeUpdate(comandoClientes);
        
        // Fechando a conexão com o Banco de Dados
        Conexao.fecharConexao(conexao, stmt);
    }

    /**
     * Deleta um cliente da tabela clientes no Banco de Dados
     * @param pk
     * @throws SQLException
     */
    @Override
    public void deletar(Object pk) throws SQLException {
        Connection conexao = Conexao.getConnection();
        
        // Comando que remove os dados na tabela Clientes
        final String comandoClientes = "DELETE FROM clientes "
                + "WHERE cpf='" + (String) pk + "'";
        
        // Comando que remove os dados na tabela Pessoas
        final String comandoPessoas = "DELETE FROM pessoas "
                + "WHERE cpf='" + (String) pk + "'";
        
        Statement stmt = conexao.createStatement();
        
        // Removendo o cliente da tabela Clientes
        stmt.executeUpdate(comandoClientes);
        
        // Removendo o cliente da tabela Pessoas
        stmt.executeUpdate(comandoPessoas);
        
        // Fechando a conexão com o Banco de Dados
        Conexao.fecharConexao(conexao, stmt);
    }

    /**
     * Carrega todos os clientes da view clientesLoja
     * @throws SQLException
     */
    @Override
    public void carregar() throws SQLException {        
        if(Cliente.clientes.isEmpty()){
            // Comando que seleciona todos os clientes na View clientesLoja
            final String comando = "SELECT * FROM clientesLoja";
            Connection conexao = Conexao.getConnection();

            Statement stmt = conexao.createStatement();
            ResultSet resultado = stmt.executeQuery(comando);

            // Inserindo os clientes na lista de Clientes
            while(resultado.next()){
                Cliente cliente = criar(resultado);
                Cliente.clientes.add(cliente);
            }
            
            // Fechando a conexão com o Banco de Dados
            Conexao.fecharConexao(conexao, stmt, resultado);
        }
        else{
            Cliente.clientes.clear();
            carregar();
        }
    }

    @Override
    public void atualizar(Object o) throws SQLException {
        Cliente cliente = (Cliente) o;
        final String comandoPessoas = "UPDATE pessoas "
                + "SET cpf='"+cliente.getCpf()+"', "
                + "nome='"+cliente.getNome()+"', "
                + "tel_contato='"+cliente.getTel_contato()+"', "
                + "endereco='"+cliente.getEndereco()+"', "
                + "data_nascimento='"+cliente.getData_nascimento()+"'"
                + "WHERE cpf='"+cliente.getCpf()+"';";
        
        final String comandoClientes = "UPDATE clientes "
                + "SET cpf='"+cliente.getCpf()+"', "
                + "email='"+cliente.getEmail()+"', "
                + "cartao='"+cliente.getCartao()+"' "
                + "WHERE id = "+cliente.getId()+";";
        
        Connection conexao = Conexao.getConnection();
        
        // Atualizando os dados do cliente nas seguintes tabelas
        Statement stmt = conexao.createStatement();
        stmt.executeUpdate(comandoPessoas); // Tabela pessoas
        stmt.executeUpdate(comandoClientes); // Tabela clientes

        // Fechando a conexão com o Banco de Dados
        Conexao.fecharConexao(conexao, stmt);
    }

}