package Controllers;

import Model.Entidades.Produto;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Gerenciamento_ProdutosController implements Initializable {
    public static List<Produto> produtos;
    public static Stage tela_cadastroProduto;
    public static Scene cadastro_produto;
    
    public static ObservableList<Produto> obsProduto;
    
    public Produto produtoSelecionado;
    
    @FXML
    private TableView<Produto> td_produto;
    @FXML
    private TableColumn<Produto, String> col_nome;
    @FXML
    private TableColumn<Produto, Float> col_preco;
    @FXML
    private TableColumn<Produto, Integer> col_estoque;
    
     public Alert criarAlerta(Alert.AlertType tipo, String titulo, String descricao, String cabecalho){
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setContentText(descricao);
        alerta.setHeaderText(cabecalho);
        
        return alerta;
    }
    
    public void selecionarProduto(Produto produto){
        produtoSelecionado = produto;
    }
    
    public void carregarTelas(){
        Parent FXML_cadastro;
        try {
            FXML_cadastro = FXMLLoader.load(getClass().getResource("/Fxml/Cadastro_Produto.fxml"));
            tela_cadastroProduto = new Stage();

            cadastro_produto = new Scene(FXML_cadastro);

            tela_cadastroProduto.setScene(cadastro_produto);
            tela_cadastroProduto.initStyle(StageStyle.UNDECORATED);
        } catch (IOException ex) {
            System.err.println("Erro: "+ex);
        }
    }
    
    public void carregarTabela(){
        col_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        col_preco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        col_estoque.setCellValueFactory(new PropertyValueFactory<>("estoque"));
        obsProduto = FXCollections.observableArrayList(Produto.produtos);
        td_produto.setItems(obsProduto);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        // Carrega os dados na Tabela
        carregarTabela();
        
        td_produto.getSelectionModel().selectedItemProperty().addListener(
                (observador,valorAntigo,novoValor)->selecionarProduto(novoValor)
        );
        
        // Carrega a tela de cadastro/editar
        carregarTelas();
        
        Main.addOnChangeScreen(new Main.onChangeScreen() {
            @Override
            public void onScreenChanged(String novaTela, Object objeto) {
                if(novaTela.equals("gerenciamentoProdutos")){
                    carregarTabela();
                    td_produto.refresh();
                }
            }
        });
    }

    @FXML
    private void iniciarCadastro(MouseEvent event) throws IOException {
        if(!Cadastro_ProdutoController.isOpen){
            tela_cadastroProduto.show();
            Cadastro_ProdutoController.isOpen = true;
        }
    }
    
    @FXML
    private void editarCliente(MouseEvent event) {
        if(!Cadastro_ProdutoController.isOpen && produtoSelecionado != null){
            Main.notificarOuvintes("telaCadastroP", produtoSelecionado);
            tela_cadastroProduto.show();
            Cadastro_ProdutoController.isOpen = true;
        }
    }

    @FXML
    private void removerCliente(MouseEvent event) {
        if(produtoSelecionado != null){
            
            // Cria um alerta para que o usuário confirme se o funcionário
            // deve ser removido
            Alert alerta = criarAlerta(
                    Alert.AlertType.CONFIRMATION,
                    "Deletar Produto",
                    "Deseja Remover?",
                    "Tem certeza que deseja remover "
                            + produtoSelecionado.getNome() + "?"
            );
            
            // Verifica se o botão OK foi clicado
            Optional<ButtonType> confirmacao = alerta.showAndWait();
            if(confirmacao.get().equals(ButtonType.OK)){
                SistemaController.funcionario_logado
                        .removerProduto(produtoSelecionado.getCodigo());
                
                alerta.close();

                // Cria um alerta para avisar ao usuário que o funcionário
                // foi removido
                alerta = criarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Sucesso",
                        "Produto removido!",
                        produtoSelecionado.getNome() + " foi removido!"
                );
                alerta.show();

                produtoSelecionado = null;
                carregarTabela();
            }
        }
    }
    
    public static void fecharPopUp(){
        tela_cadastroProduto.close();
        Cadastro_ProdutoController.isOpen = false;
    }
}
