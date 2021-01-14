package Controllers;

import Model.DAO.Cargo_DAO;
import Model.DAO.CategoriaProduto_DAO;
import Model.DAO.Cliente_DAO;
import Model.DAO.Funcionario_DAO;
import Model.DAO.Produto_DAO;
import Model.DAO.Venda_DAO;
import Model.Entidades.Cargo;
import Model.Entidades.CategoriaProduto;
import Model.Entidades.Cliente;
import Model.Entidades.Funcionario;
import Model.Entidades.Produto;
import Model.Entidades.Venda;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SistemaController implements Initializable {

    public Stage stage;
    public Parent gerenciarFuncionario, gerenciarCliente, gerenciarProduto;
    public Parent centralNotificacoes, realizarVenda;
        
    public static boolean isPressed = false;
    public static Funcionario funcionario_logado;
    
    @FXML
    private Text text_infoUser;
    
    @FXML
    private Button btn_funcionarios;
    
    @FXML
    private AnchorPane View;
    
    public void carregarParents() throws IOException{
        gerenciarCliente = FXMLLoader.load(getClass()
                .getResource("/Fxml/Gerenciamento_Clientes.fxml"));
        gerenciarFuncionario = FXMLLoader.load(getClass()
                .getResource("/Fxml/Gerenciamento_Funcionarios.fxml"));
        gerenciarProduto = FXMLLoader.load(getClass()
                .getResource("/Fxml/Gerenciamento_Produtos.fxml"));
        centralNotificacoes = FXMLLoader.load(getClass()
                .getResource("/Fxml/Central_Notificacoes.fxml"));
        realizarVenda = FXMLLoader.load(getClass()
                .getResource("/Fxml/Realizar_Venda.fxml"));
    }
    
    public void carregarDados(){
        Funcionario_DAO funcionarioDAO = new Funcionario_DAO();
        Cargo_DAO cargoDAO = new Cargo_DAO();
        CategoriaProduto_DAO categoriaDAO = new CategoriaProduto_DAO();
        Cliente_DAO clienteDAO = new Cliente_DAO();
        Produto_DAO produtoDAO = new Produto_DAO();
        
        try {
            // Carregar dados
            funcionarioDAO.carregar();
            cargoDAO.carregar();
            categoriaDAO.carregar();
            clienteDAO.carregar();
            produtoDAO.carregar();
            
            
        } catch (SQLException ex) {
            System.err.println("Erro: " + ex);
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        Main.addOnChangeScreen(new Main.onChangeScreen() {
            @Override
            public void onScreenChanged(String novaTela, Object objeto) {
                if(novaTela.equals("sistema")){
                    funcionario_logado = (Funcionario) objeto;
                    // verifica se o funcionário logado é Gerente ou Vendedor
                    if(funcionario_logado.getId_cargo() == 1){
                        text_infoUser.setText("Logado como: Gerente");
                    }
                    else{
                        text_infoUser.setText("Logado como: Vendedor");
                        // Desativa o botão de gerenciar funcionários
                        btn_funcionarios.setDisable(true);
                    }
                }
            }
        });
        
        try {
            carregarDados();
            carregarParents();
        } catch (IOException ex) {
            System.err.println("Erro: " + ex);
        }
        
        View.getChildren().setAll(centralNotificacoes);
    }    

    @FXML
    private void gerenciarCliente(MouseEvent event){
        View.getChildren().setAll(gerenciarCliente);
    }

    @FXML
    private void gerenciarFuncionarios(MouseEvent event) throws IOException{
        View.getChildren().setAll(gerenciarFuncionario);
    }

    @FXML
    private void gerenciarProdutos(MouseEvent event){
        View.getChildren().setAll(gerenciarProduto);
    }

    @FXML
    private void realizarVendas(MouseEvent event) throws IOException{
        View.getChildren().setAll(realizarVenda);
    }

    @FXML
    private void deslogar(MouseEvent event) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar");
        alerta.setHeaderText("Deslogar da conta?");
        Optional<ButtonType> confirmacao = alerta.showAndWait();
        if(confirmacao.get().equals(ButtonType.OK)){
            Main.trocarTela(1);
        }
    }

    @FXML
    private void centralNotificacoes(MouseEvent event){
        View.getChildren().setAll(centralNotificacoes);
    }
    
    @FXML
    void Fechar(MouseEvent event) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar");
        alerta.setHeaderText("Fechar programa?");
        Optional<ButtonType> confirmacao = alerta.showAndWait();
        if(confirmacao.get().equals(ButtonType.OK)){
            Main.stage.close();
        }
    }
    
    @FXML
    void minimizar(MouseEvent event) {
        Main.stage.setIconified(true);
    }
    
    @FXML
    void press(MouseEvent event) {
        isPressed = true;
    }
    
    @FXML
    void realeased(MouseEvent event) {
        isPressed = false;
    }
}
