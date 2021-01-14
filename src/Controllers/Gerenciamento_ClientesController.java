/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Model.Entidades.Cliente;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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

/**
 * FXML Controller class
 *
 * @author herik
 */
public class Gerenciamento_ClientesController implements Initializable {

    public static List<Cliente> clientes;
    public static ObservableList<Cliente> obsClientes;
    
    public static Stage tela_cadastrarCliente;
    public static Scene cadastrarCliente;
    
    public Cliente clienteSelecionado;
    
    @FXML
    private TableView<Cliente> td_clientes;
    @FXML
    private TableColumn<Cliente, String> col_nome;
    @FXML
    private TableColumn<Cliente, String> col_cpf;
    @FXML
    private TableColumn<Cliente, String> col_telContato;
    @FXML
    private TableColumn<Cliente, String> col_email;
    
    public Alert criarAlerta(Alert.AlertType tipo, String titulo, String descricao, String cabecalho){
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setContentText(descricao);
        alerta.setHeaderText(cabecalho);
        
        return alerta;
    }
    
    public void selecionarCliente(Cliente cliente){
        clienteSelecionado = cliente;
    }
    
    public void carregarTela(){
        try {
            Parent FXML_cadastrarCliente = FXMLLoader.load(getClass()
                    .getResource("/Fxml/Cadastro_Clientes.fxml"));
            
            tela_cadastrarCliente = new Stage();
            
            cadastrarCliente = new Scene(FXML_cadastrarCliente);
            tela_cadastrarCliente.setScene(cadastrarCliente);
            tela_cadastrarCliente.initStyle(StageStyle.UNDECORATED);
        } catch (IOException ex) {
            System.err.println("Erro: " + ex);
        }
    }
    
    public void carregarTabela(){
        col_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        col_cpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        col_telContato.setCellValueFactory(new PropertyValueFactory<>("tel_contato"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        obsClientes = FXCollections.observableArrayList(Cliente.clientes);
        td_clientes.setItems(obsClientes);
        System.out.println("Tabela carregada!");
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Fica ouvindo se essa tela vai ser notificada
        Main.addOnChangeScreen(new Main.onChangeScreen() {
            @Override
            public void onScreenChanged(String novaTela, Object objeto) {
                if(novaTela.equals("gerenciamentoClientes")){
                    carregarTabela();
                    td_clientes.refresh();
                }
            }
        });
        
        td_clientes.getSelectionModel().selectedItemProperty()
                .addListener((observador, valorAntigo, novoValor)->selecionarCliente(novoValor));
        
        // Inicializa a tabela
        carregarTabela();
        
        // Define a tela de cadastro de clientes
        carregarTela();
    }    

    @FXML
    private void iniciarCadastro(MouseEvent event) {
        if(!Cadastro_ClientesController.isOpen){
            tela_cadastrarCliente.show();
            Cadastro_ClientesController.isOpen = true;
        }
    }

    @FXML
    private void editarCliente(MouseEvent event){
        if(!Cadastro_FuncionariosController.isOpen && clienteSelecionado != null){
            Main.notificarOuvintes("telaCadastroC", clienteSelecionado);
            tela_cadastrarCliente.show();
            Cadastro_ClientesController.isOpen = true;
        }
    }

    @FXML
    private void removerCliente(MouseEvent event) {
        if(clienteSelecionado != null){
            
            // Cria um alerta para que o usuário confirme se o funcionário
            // deve ser removido
            Alert alerta = criarAlerta(
                    Alert.AlertType.CONFIRMATION,
                    "Remover Cliente",
                    "Deseja Remover?",
                    "Tem certeza que deseja remover "
                            + clienteSelecionado.getNome() + "?"
            );
            
            // Verifica se o botão OK foi clicado
            Optional<ButtonType> confirmacao = alerta.showAndWait();
            if(confirmacao.get().equals(ButtonType.OK)){
                SistemaController.funcionario_logado
                        .removerCliente(clienteSelecionado.getCpf());
                
                alerta.close();

                // Cria um alerta para avisar ao usuário que o funcionário
                // foi removido
                alerta = criarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Sucesso",
                        "Cliente removido!",
                        clienteSelecionado.getNome() + " foi removido!"
                );
                alerta.show();

                clienteSelecionado = null;
                carregarTabela();
            }
        }
    }
    
}
