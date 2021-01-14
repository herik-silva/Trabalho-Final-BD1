package Model.DAO;

import Connection.Conexao;
import Model.Entidades.Funcionario;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Classe que realiza conexões com o Banco de Dados na Tabela Funcionário
 * @author herik
 */
public class Funcionario_DAO implements DAO{

    @Override
    public Funcionario criar(ResultSet resultado) throws SQLException {
        Funcionario funcionario = new Funcionario(resultado.getString("cpf"), 
                    resultado.getString("nome"), 
                    resultado.getString("tel_contato"), 
                    resultado.getString("data_nascimento"), 
                    resultado.getString("endereco"), 
                    resultado.getString("senha"), 
                    Float.parseFloat(resultado.getString("salario")), 
                    Integer.parseInt(resultado.getString("id_cargo"))
        );
        
        funcionario.setId(Integer.parseInt(resultado.getString("id")));
        
        return funcionario;
    }

    /**
     * Pesquisa pelo funcionário no banco de dados e instancia o objeto
     * em seguida o retorna
     * @param pk
     * @return
     * @throws SQLException
     */
    @Override
    public Funcionario pesquisarPorPK(Object pk) throws SQLException {
        Connection conexao = Conexao.getConnection();
        Funcionario funcionario;
        
        try {
            Statement stmt = conexao.createStatement();
            
            // Realizando a consulta.
            ResultSet resultado = stmt.executeQuery("SELECT * FROM funcionariosLoja "
                    + "WHERE cpf = '"+ (String) pk +"'"
            );
            
            // Verificando se não foi retornado nenhuma linha na consulta.
            resultado.last();
            if(resultado.getRow()<1){
                return null;
            }
            
            // Setando o apontador para a primeira linha.
            resultado.first();
            
            // Construindo o objeto funcionário.
            funcionario = criar(resultado);
            
            // Fechando a conexão
            Conexao.fecharConexao(conexao, stmt, resultado);
            
            // Retornando o funcionário resgatado.
            return funcionario;
            
        } catch (SQLException ex) {
            System.err.println("Erro: " + ex);
            return null;
        }
    }

    /**
     * Insere o funcionário no Banco de Dados
     * @param o
     * @throws SQLException
     */
    @Override
    public void inserir(Object o) throws SQLException {
        Connection conexao = Conexao.getConnection();
        Statement stmt = conexao.createStatement();

        // Fazendo o cast para converter um Funcionario
        Funcionario funcionario = (Funcionario) o;

        // Inserindo na Tabela Pessoas.
        stmt.executeUpdate("INSERT INTO pessoas VALUES('"+funcionario.getCpf()
                +"', '"+ funcionario.getNome()
                +"', '"+ funcionario.getTel_contato()
                +"', '"+ funcionario.getEndereco()
                +"', '"+ funcionario.getData_nascimento() + "')"
        );

        // Inserindo na Tabela Funcionarios.
        stmt.executeUpdate("INSERT INTO funcionarios VALUES('"+funcionario.getId()
                +"', '"+ funcionario.getCpf()
                +"', "+ funcionario.getId_cargo()
                +", '"+ funcionario.getSenha()
                +"', "+ funcionario.getSalario()+")"
        );


        // Fechando a Conexão.
        Conexao.fecharConexao(conexao, stmt);            
    }

    /**
     * Deleta o funcionário selecionado
     * @param pk
     * @throws SQLException
     */
    @Override
    public void deletar(Object pk) throws SQLException {
        Connection conexao = Conexao.getConnection();
        
        try {
            Statement stmt = conexao.createStatement();
            
            // Removendo da tabela funcionarios 
            stmt.executeUpdate("DELETE FROM funcionarios WHERE cpf='"+(String)pk+"'");
            
            // Removendo da tabela pessoas
            stmt.executeUpdate("DELETE FROM pessoas WHERE cpf='"+(String)pk+"'");
            
            // Fechando a conexão com o Banco de Dados
            Conexao.fecharConexao(conexao, stmt);
            
        } catch (SQLException ex) {
            System.err.println("Erro: " + ex);
        }
    }

    /**
     * Carrega os dados dos funcionários
     * @throws SQLException
     */
    @Override
    public void carregar() throws SQLException {
        Connection conexao = Conexao.getConnection();
        
        if(Funcionario.funcionarios.isEmpty()){ // Verifica se a lista está vazia
                Statement stmt = conexao.createStatement();

                // Realizando a consulta.
                ResultSet resultado = stmt.executeQuery("SELECT * FROM funcionariosLoja ");
                while(resultado.next()){
                    // Instanciando o objeto
                    Funcionario funcionario = criar(resultado);

                    // Setando o id do funcionário
                    funcionario.setId(Integer.parseInt(resultado.getString("id")));

                    // Inserindo o objeto na lista
                    Funcionario.funcionarios.add(funcionario);
                }

                Conexao.fecharConexao(conexao, stmt, resultado);
        }
        else{
            Funcionario.funcionarios.clear();
            carregar();
        }
    }

    /**
     * Método responsável por atualizar os dados do funcionário no
     * Banco de Dados
     * @param o
     * @throws SQLException
     */
    @Override
    public void atualizar(Object o) throws SQLException {
        Funcionario funcionario = (Funcionario) o;
        final String comandoPessoas = "UPDATE pessoas "
                + "SET cpf='"+funcionario.getCpf()+"', "
                + "nome='"+funcionario.getNome()+"', "
                + "tel_contato='"+funcionario.getTel_contato()+"', "
                + "endereco='"+funcionario.getEndereco()+"', "
                + "data_nascimento='"+funcionario.getData_nascimento()+"'"
                + "WHERE cpf='"+funcionario.getCpf()+"';";
        
        final String comandoFuncionarios = "UPDATE funcionarios "
                + "SET cpf='"+funcionario.getCpf()+"', "
                + "id_cargo="+funcionario.getId_cargo()+", "
                + "senha='"+funcionario.getSenha()+"', "
                + "salario="+funcionario.getSalario()+" "
                + "WHERE id = "+funcionario.getId()+";";
        
        Connection conexao = Conexao.getConnection();
        
        // Atualizando os dados do funcionario nas seguintes tabelas
        Statement stmt = conexao.createStatement();
        stmt.executeUpdate(comandoPessoas); // Tabela pessoas
        stmt.executeUpdate(comandoFuncionarios); // Tabela funcionarios

        // Fechando a conexão com o Banco de Dados
        Conexao.fecharConexao(conexao, stmt);
    }
}