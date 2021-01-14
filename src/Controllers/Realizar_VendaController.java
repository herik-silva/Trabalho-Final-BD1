/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Model.DAO.Cliente_DAO;
import Model.DAO.Produto_DAO;
import Model.Entidades.CategoriaProduto;
import Model.Entidades.Cliente;
import Model.Entidades.Produto;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author herik
 */
public class Realizar_VendaController implements Initializable {
    public List<String> categorias = new ArrayList<String>();
    public List<String> compras = new ArrayList<String>();
    public ObservableList<String> obsProduto;
    private List<Produto> carrinho = new ArrayList<Produto>();
    
    int indexProdutoSelecionado = -1;
    Produto produtoPesquisado;
    Cliente clientePesquisado, clienteDefinido;

    @FXML
    private Text text_nomeP;
    @FXML
    private Text text_preco;
    @FXML
    private Text text_codigo;
    @FXML
    private Text text_estoque;
    @FXML
    private Text text_categoria;
    @FXML
    private Text text_nomeC;
    @FXML
    private Text text_CPF;
    @FXML
    private Text text_telContato;
    @FXML
    private Text text_email;
    @FXML
    private Text text_dataNascimento;
    @FXML
    private TextField txt_codigoP;
    @FXML
    private TextField txt_cpf;
    @FXML
    private ListView<String> List_carrinhoCompras;
    @FXML
    private TextField txt_totalPagar;
    
    private void selecionarProduto(int indexProduto){
        indexProdutoSelecionado = indexProduto;
    }
    
    public Alert criarAlerta(Alert.AlertType tipo, String titulo, String descricao, String cabecalho){
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setContentText(descricao);
        alerta.setHeaderText(cabecalho);
        
        return alerta;
    }
    
    private void atualizarLista(){
        obsProduto = FXCollections.observableArrayList(compras);
        
        List_carrinhoCompras.setItems(obsProduto);
        List_carrinhoCompras.refresh();
        
        atualizarPrecoTotal();
    }

    private void apresentarDadosP(Produto produto){
        text_nomeP.setText("Nome: "+produto.getNome());
        text_codigo.setText("Código: "+Integer.toString(produto.getCodigo()));
        text_estoque.setText("Estoque: "+Integer.toString(produto.getEstoque()));
        text_preco.setText("Preço: "+Float.toString(produto.getPreco()));
        text_categoria.setText("Categoria: "+categorias.get(produto.getId_categoria()-1));
    }
    
    private void apresentarDadosC(Cliente cliente){
        text_nomeC.setText("Nome: "+cliente.getNome());
        text_CPF.setText("CPF: "+cliente.getCpf());
        text_dataNascimento.setText("Data de Nascimento: "+cliente.getData_nascimento());
        text_email.setText("Email: "+cliente.getEmail());
        text_telContato.setText("Tel. Contato: "+cliente.getTel_contato());
    }
    
    private void registrarVenda(int id_cliente, int codigo_produto, float preco_produto){
        final int idVendedor = SistemaController.funcionario_logado.getId();
        
        Calendar dataV = Calendar.getInstance();
        SimpleDateFormat formatarData = new SimpleDateFormat("dd/MM/yyyy");
        Date data = dataV.getTime();
        final String dataVenda = formatarData.format(data);
                
        // Registrando a venda no sistema
        SistemaController.funcionario_logado.registrarVenda(
                dataVenda, 
                codigo_produto, 
                id_cliente, 
                idVendedor, 
                preco_produto, 
                1
        );
        
        decrementarEstoque(codigo_produto);
    }
    
    private float calcularPreco(){
        float precoTotal = 0;
        
        // Calculando o preço total a pagar
        for(Produto produto: carrinho){
            precoTotal += produto.getPreco();
        }
        
        return precoTotal;
    }
    
    private void atualizarPrecoTotal(){
        float precoTotal = calcularPreco();
        txt_totalPagar.setText("Total: R$" + precoTotal);
    }
    
    private void decrementarEstoque(int codigoProduto){
        for(int i=0; i<Produto.produtos.size(); i++){
            if(Produto.produtos.get(i).getCodigo() == codigoProduto){
                Produto.produtos.get(i)
                        .setEstoque(Produto.produtos.get(i).getEstoque()-1);
                
                break;
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Adicionando a descrição das categorias
        for(CategoriaProduto categoria: CategoriaProduto.categorias){
            categorias.add(categoria.getDescricao());
        }
        
        List_carrinhoCompras.getSelectionModel().selectedIndexProperty().addListener(
                (observador, valorAntigo, novoValor)->selecionarProduto((int) novoValor)
        );
        
    }    

    @FXML
    private void pesquisarCliente(MouseEvent event) throws SQLException {
        System.out.println("Pesquisando Cliente: " + txt_cpf.getText());
        if(!Cliente.clientes.isEmpty()){
            // Procurando pelo produto
            for(Cliente cliente: Cliente.clientes){
                System.out.println("CPF do cliente: " + cliente.getCpf());
                if(cliente.getCpf().equals(txt_cpf.getText())){
                    System.out.println("Nome: " + cliente.getNome());
                    clientePesquisado = cliente;
                    apresentarDadosC(clientePesquisado);
                    break;
                }
            }
        }
        else{
            // Carregando Clientes
            Cliente_DAO clienteDAO = new Cliente_DAO();
            clienteDAO.carregar();
            pesquisarProduto(event);
        }
    }

    @FXML
    private void pesquisarProduto(MouseEvent event) throws SQLException{
        if(!Produto.produtos.isEmpty()){
            // Procurando pelo produto
            for(Produto produto: Produto.produtos){
                if(produto.getCodigo() == Integer.parseInt(txt_codigoP.getText())){
                    produtoPesquisado = produto;
                    apresentarDadosP(produtoPesquisado);
                    break;
                }
            }
        }
        else{
            // Carregando os Produtos
            Produto_DAO produtoDAO = new Produto_DAO();
            produtoDAO.carregar();
            pesquisarProduto(event);
        }
    }

    @FXML
    private void finalizarCompra(MouseEvent event) throws SQLException {
        if(clienteDefinido != null){
            for(Produto p: carrinho){
                registrarVenda((clienteDefinido.getId()+1), p.getCodigo(), p.getPreco());
                Produto_DAO produtoDAO = new Produto_DAO();
                produtoDAO.atualizar(p);
            }

            // Limpando o carrinho de compras
            carrinho.clear();
            compras.clear();
            atualizarLista();

            Alert alerta = criarAlerta(
                    Alert.AlertType.INFORMATION, 
                    "Sucesso", 
                    "A venda foi registrada no sistema.", 
                    "Venda realizada!"
            );
            alerta.show();

            // Notifica a tela de gerenciamento de produtos atualizando a tabela
            Main.notificarOuvintes("gerenciamentoProdutos", null);
            Main.notificarOuvintes("centralNotificacoes", null);
        }
        else{
            Alert alerta = criarAlerta(
                    Alert.AlertType.ERROR, 
                    "Erro", 
                    "Defina um cliente para realizar a venda", 
                    "Cliente não definido!"
            );
            alerta.show();
        }
    }

    @FXML
    private void addProduto(MouseEvent event){
        compras.add(produtoPesquisado.getNome());
        carrinho.add(produtoPesquisado);
        
        atualizarLista();
    }

    @FXML
    private void definirCliente(MouseEvent event){
        if(clientePesquisado != null && clientePesquisado != clienteDefinido){
            clienteDefinido = clientePesquisado;
            Alert alerta = criarAlerta(
                    Alert.AlertType.INFORMATION, 
                    "Sucesso", 
                    null, 
                    "Cliente Definido!"
            );
            alerta.show();
        }
    }

    @FXML
    private void limparInputC(MouseEvent event) {
        txt_cpf.setText("");
    }

    @FXML
    private void limparInputP(MouseEvent event) {
        txt_codigoP.setText("");
    }

    @FXML
    private void removerProduto(MouseEvent event) {
        if(indexProdutoSelecionado != -1){
            carrinho.remove(indexProdutoSelecionado);
            compras.remove(indexProdutoSelecionado);
            atualizarLista();
        }
    }
    
}
