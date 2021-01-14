package Model.Entidades;


import Controllers.Cadastro_FuncionariosController;
import Model.DAO.Cargo_DAO;
import Model.DAO.CategoriaProduto_DAO;
import Model.DAO.Cliente_DAO;
import Model.DAO.Funcionario_DAO;
import Model.DAO.Produto_DAO;
import Model.DAO.Venda_DAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa o Funcionário no sistema.
 * @author herik
 */
public class Funcionario extends Pessoa{
    // Atributos
    private int id;
    private int id_cargo;
    private String senha;
    private float salario;
    
    // Lista de Funcionários
    public static List<Funcionario> funcionarios = new ArrayList<Funcionario>();
    
    /**
     * Construtor de Funcionário. Recebe como parâmetro o cpf, nome, tel_contato,
     * data_nascimento, endereco, id, senha e salario.
     * @param cpf
     * @param nome
     * @param tel_contato
     * @param data_nascimento
     * @param endereco
     * @param id
     * @param senha
     * @param salario
     * @param id_cargo
     */
    public Funcionario(String cpf, String nome, String tel_contato, String data_nascimento, String endereco,
            String senha, float salario, int id_cargo) {
        super(cpf, nome, tel_contato, data_nascimento, endereco);
        
        this.senha = senha;
        this.salario = salario;
        this.id_cargo = id_cargo;
    }
    
    /**
     * Construtor Padrão
     * @param cpf
     * @param nome
     * @param tel_contato
     * @param data_nascimento
     * @param endereco
     */
    public Funcionario(String cpf, String nome, String tel_contato, String data_nascimento, String endereco) {
        super(cpf, nome, tel_contato, data_nascimento, endereco);
    }

    // Getters e Setters
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_cargo() {
        return id_cargo;
    }

    public void setId_cargo(int id_cargo) {
        this.id_cargo = id_cargo;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public float getSalario() {
        return salario;
    }

    public void setSalario(float salario) {
        this.salario = salario;
    }
   
    /**
     * Método que realiza o cadastro de Cargos no sistema.
     * @param id
     * @param descricao
     */
    public void cadastrarCargo(int id, String descricao){
        Cargo cargo = new Cargo(id,descricao);
        Cargo.cargos.add(cargo);
        Cadastro_FuncionariosController.nomeCargo.add(cargo.getDescricao());
    }
    
    /**
     * Método que realiza o cadastro de funcionarios no sistema
     * @param cpf
     * @param nome
     * @param tel_contato
     * @param data_nascimento
     * @param endereco
     * @param senha
     * @param salario
     * @param id_cargo
     */
    public void cadastrarFuncionario(String cpf, String nome, String tel_contato, 
            String data_nascimento, String endereco, String senha, float salario,
            int id_cargo){
        
        // Criando o funcionario
        Funcionario funcionario = new Funcionario(cpf, nome, 
                tel_contato, data_nascimento, 
                endereco, senha, 
                salario, id_cargo
        );
        
        // Adicionando o funcionario na lista funcionarios
        funcionarios.add(funcionario);
        
        // Criando o objeto funcionarioDAO que realizara a conexão
        // com o Banco de Dados
        Funcionario_DAO funcionarioDAO = new Funcionario_DAO();
        try {
            // Inserindo o funcionário cadastrado no Banco de Dados
            funcionarioDAO.inserir(funcionario);
        } catch (SQLException ex) {
            System.err.println("Erro: " + ex);
        }
    }
    
    /**
     * Método que realiza o cadastro de Clientes no sistema.
     * @param cpf
     * @param nome
     * @param tel_contato
     * @param data_nascimento
     * @param endereco
     * @param email
     * @param cartao
     */
    public void cadastrarCliente(String cpf, String nome, String tel_contato, String data_nascimento,
            String endereco, String email, String cartao){
        
        // Instanciando o bojeto cliente
        Cliente cliente = new Cliente(cpf, nome, tel_contato, data_nascimento, endereco);
        
        // Verifica se não está vazio
        if(!cartao.isEmpty()){
            cliente.setCartao(cartao);
        }
        else{
            cliente.setCartao("");
        }
        
        // Verifica se não está vazio
        if(!email.isEmpty()){
            cliente.setEmail(email);
        }
        else{
            cliente.setCartao("");
        }
        
        cliente.setId(Cliente.clientes.get(Cliente.clientes.size()-1).getId()+1);
        
        // Adicionando o cliente na lista
        Cliente.clientes.add(cliente);
        
        // Inserindo no banco de dados
        Cliente_DAO clienteDAO = new Cliente_DAO();
        try {
            clienteDAO.inserir(cliente); // Cliente inserido
        } catch (SQLException ex) {
            System.err.println("Erro: " + ex);
        }
    }
    
    /**
     * Método que realiza o cadastro de produtos no sistema.
     * @param codigo
     * @param nome
     * @param preco
     * @param estoque
     * @param id_categoria
     */
    public void cadastrarProduto(int codigo, String nome, float preco, int estoque, int id_categoria){
        // Instanciando o objeto produto
        Produto produto = new Produto(codigo, nome, preco, estoque, id_categoria);
        
        // Adiciona o produto na lista
        Produto.produtos.add(produto);
        
        // Inserindo no Banco de Dados
        Produto_DAO produtoDAO = new Produto_DAO();
        try {
            produtoDAO.inserir(produto); // Produto inserido
        } catch (SQLException ex) {
            System.err.println("Erro: " + ex);
        }
    }
    
    /**
     * Método que realiza o cadastro de Categorias de Produtos no sistema.
     * @param id
     * @param descricao
     */
    public void cadastrarCategoriaProduto(int id, String descricao){
        CategoriaProduto categoria = new CategoriaProduto(id, descricao);
        CategoriaProduto.categorias.add(categoria);
        
        CategoriaProduto_DAO categoriaDAO = new CategoriaProduto_DAO();
        try {
            categoriaDAO.inserir(categoria); // Categoria inserida
        } catch (SQLException ex) {
            System.err.println("Erro: " + ex);
        }
    }
    
    /**
     * Remove o funcionário do sistema
     * @param cpf
     */
    public void removerFuncionario(String cpf){  
        // Removendo o funcionário da List<Funcionario>
        for(int i=0; i<Funcionario.funcionarios.size(); i++){
            if(Funcionario.funcionarios.get(i).getCpf().equals(cpf)){
                Funcionario.funcionarios.remove(i); // Funcionário removido
                break;
            }
        }
        
        // Removendo o funcionário do Banco de Dados
        Funcionario_DAO funcionarioDAO = new Funcionario_DAO();
        try {
            funcionarioDAO.deletar(cpf); // Funcionário deletado
        } catch (SQLException ex) {
            System.err.println("Erro: " + ex);
        }
    }
    
    /**
     * Remove o cliente do sistema
     * @param cpf
     */
    public void removerCliente(String cpf){
        for(int i=0; i<Cliente.clientes.size(); i++){
            if(Cliente.clientes.get(i).getCpf().equals(cpf)){
                Cliente.clientes.remove(i); // Cliente removido
                break;
            }
        }
        
        // Removendo o cliente do Banco de Dados
        Cliente_DAO clienteDAO = new Cliente_DAO();
        try {
            clienteDAO.deletar(cpf); // Cliente removido
        } catch (SQLException ex) {
            System.err.println("Erro: " + ex);
        }
    }
    
    /**
     * Remove o Produto do sistema
     * @param codigo
     */
    public void removerProduto(int codigo){
        for(int i=0; i<Produto.produtos.size(); i++){
            if(Produto.produtos.get(i).getCodigo() == codigo){
                Produto.produtos.remove(i); // Produto removido
                break;
            }
        }
        
        // Removendo o produto do Banco de Dados
        Produto_DAO produtoDAO = new Produto_DAO();
        try {
            produtoDAO.deletar(codigo); // Produto removido
        } catch (SQLException ex) {
            System.err.println("Erro: " + ex);
        }
    }
    
    /**
     * Remove a Categoria do Produto selecionado
     * @param id
     */
    public void removerCategoriaProduto(int id){
        for(int i=0; i<CategoriaProduto.categorias.size(); i++){
            if(CategoriaProduto.categorias.get(i).getId() == id){
                CategoriaProduto.categorias.remove(i); // Categoria removida
                break;
            }
        }
        
        // Removendo a categoria do Banco de Dados
        CategoriaProduto_DAO categoriaDAO = new CategoriaProduto_DAO();
        try {
            categoriaDAO.deletar(id); // Categoria removida
        } catch (SQLException ex) {
            System.err.println("Erro: " + ex);
        }
    }
    
    /**
     * Remove o Cargo do sistema
     * @param id
     */
    public void removerCargo(int id){
        for(int i=0; i<Cargo.cargos.size(); i++){
            if(Cargo.cargos.get(i).getId() == id){
                Cargo.cargos.remove(i); // Cargo removido
                break;
            }
        }
        
        // Removendo o cargo do Banco de Dados
        Cargo_DAO cargoDAO = new Cargo_DAO();
        try {
            cargoDAO.deletar(id); // Cargo removido
        } catch (SQLException ex) {
            System.err.println("Erro: " + ex);
        }
    }
    
    /**
     * Método que registra os dados da venda do produto.
     * @param numero
     * @param data_venda
     * @param codigo_produto
     * @param id_cliente
     * @param id_funcionario
     * @param preco_venda
     * @param quantidade
     */
    public void registrarVenda(String data_venda, int codigo_produto, 
            int id_cliente, int id_funcionario, float preco_venda, int quantidade){
        
        // Instanciando o objeto venda
        Venda venda = new Venda(data_venda, codigo_produto, id_cliente, id_funcionario, preco_venda, quantidade);
        
        // Adicionando a venda na lista
        Venda.vendas.add(venda);
        
        // Inserindo a venda na tabela vendas do Banco de Dados
        Venda_DAO vendaDAO = new Venda_DAO();
        try {
            vendaDAO.inserir(venda); // Venda inserida
        } catch (SQLException ex) {
            System.err.println("Erro: " + ex);
        }
    }
}