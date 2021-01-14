package Controllers;

import Model.DAO.Produto_DAO;
import Model.Entidades.CategoriaProduto;
import Model.Entidades.Produto;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class Cadastro_ProdutoController implements Initializable{

    public static boolean isOpen = false;
    public List<String> nomeCategoria = new ArrayList<String>();
    public ObservableList<String> categoria;
    
    public Produto produto;
    
    @FXML
    private TextField txt_nome;

    @FXML
    private TextField txt_preco;

    @FXML
    private TextField txt_estoque;

    @FXML
    private ComboBox<String> combo_categoria;

    @FXML
    private TextField txt_codigo;
    @FXML
    private Text text_titulo;
    @FXML
    private Button btn_atualizarProduto;
    @FXML
    private Button btn_cadastrarProduto;
    
    // Limpa os inputs
    private void limparInput(){
        txt_nome.setText("");
        txt_codigo.setText("");
        txt_estoque.setText("");
        txt_preco.setText("");
    }
    
    // Verifica se existe algum input vazio
    private boolean inputVazio(){
        if(txt_codigo.getText().isEmpty() 
                || txt_estoque.getText().isEmpty()
                || txt_nome.getText().isEmpty() 
                || txt_preco.getText().isEmpty()){
            
            return false;
        }
        else{
            return true;
        }
    }

    public Alert criarAlerta(Alert.AlertType tipo, String titulo, String descricao, String cabecalho){
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setContentText(descricao);
        alerta.setHeaderText(cabecalho);
        
        return alerta;
    }
    
    @FXML
    void cadastrarProduto(MouseEvent event) {
        if(inputVazio()){
            // Cadastrando o produto no sistema
            SistemaController.funcionario_logado.cadastrarProduto(
                    Integer.parseInt(txt_codigo.getText()), 
                    txt_nome.getText(),
                    Float.parseFloat(txt_preco.getText()), 
                    Integer.parseInt(txt_estoque.getText()),
                    combo_categoria.getSelectionModel().getSelectedIndex()+1
            );

            // Cria um alerta informando o usuário que o Produto foi cadastrado
            Alert alerta = criarAlerta(
                    Alert.AlertType.INFORMATION, 
                    "Sucesso", 
                    "O Produto foi cadastrado no sistema", 
                    "Produto cadastrado!"
            );
            alerta.show();

            // Notifica o controller gerenciamento_Produtos para atualizar a 
            // tabela
            Main.notificarOuvintes("gerenciamentoProdutos", null);

            fechar(event);
        }
        else{
            // Cria um alerta de erro informando o usuário que não foi possível
            // realizar o cadastro do produto
            Alert alerta = criarAlerta(
                    Alert.AlertType.ERROR, 
                    "Erro", 
                    "Verifique se todos os campos estão preenchidos", 
                    "Campo vazio"
            );
            alerta.show();
        }
    }
    
    @FXML
    void minimizar(MouseEvent event) {
        Gerenciamento_ProdutosController.tela_cadastroProduto.setIconified(true);
    }

    @FXML
    void cancelar(MouseEvent event) {
        fechar(event);
    }

    @FXML
    void fechar(MouseEvent event) {
        Gerenciamento_ProdutosController.fecharPopUp();
        btn_cadastrarProduto.setVisible(true);
        btn_atualizarProduto.setVisible(false);
        text_titulo.setText("Cadastrar Produto");
        txt_codigo.setDisable(false);
        limparInput();
    }

    @FXML
    private void atualizarProduto(MouseEvent event) throws SQLException {
        if(inputVazio()){
            produto.setCodigo(Integer.parseInt(txt_codigo.getText()));
            produto.setEstoque(Integer.parseInt(txt_estoque.getText()));
            produto.setNome(txt_nome.getText());
            produto.setPreco(Float.parseFloat(txt_preco.getText()));

            // Atualizando no Banco de Dados
            Produto_DAO produtoDAO = new Produto_DAO();
            produtoDAO.atualizar(produto);

            // Cria um alerta para informar ao usuário que o produto foi atualizado
            Alert alerta = criarAlerta(
                    Alert.AlertType.INFORMATION, 
                    "Sucesso", 
                    "Produto atualizado com sucesso", 
                    "Produto Atualizado!"
            );
            alerta.show();

            // Notifica a tela de gerenciamento de produtos que a tabela foi
            // atualziada.
            Main.notificarOuvintes("gerenciamentoProdutos", null);

            fechar(event);
        }
        else{
            Alert alerta = criarAlerta(
                    Alert.AlertType.ERROR,
                    "Erro",
                    "Verifique qual campo está vazio",
                    "Campo vazio!"
            );
            alerta.show();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.addOnChangeScreen(new Main.onChangeScreen() {
            @Override
            public void onScreenChanged(String novaTela, Object objeto) {
                if(novaTela.equals("telaCadastroP")){
                    produto = (Produto) objeto;
                    txt_nome.setText(produto.getNome());
                    txt_estoque.setText(Integer.toString(produto.getEstoque()));
                    txt_preco.setText(Float.toString(produto.getPreco()));
                    txt_codigo.setText(Integer.toString(produto.getCodigo()));
                    
                    // Esconde o botão cadastrar e mostra o botão
                    // Atualizar
                    btn_cadastrarProduto.setVisible(false);
                    btn_atualizarProduto.setVisible(true);
                    txt_codigo.setDisable(true);
                    
                    text_titulo.setText("Editar Produto");
                }
            }
        });
                        
        // Limpando o ObservableList para garantir que não tera dados
        // duplicados.
        nomeCategoria.clear();
        
        // Inserindo o nome de cada cargo
        for(CategoriaProduto cargo: CategoriaProduto.categorias){
            nomeCategoria.add(cargo.getDescricao());
        }
        
        // Inserindo no combo box.
         categoria= FXCollections.observableArrayList(nomeCategoria);
        combo_categoria.setItems(categoria);
    }
}
