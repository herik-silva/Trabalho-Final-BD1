/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Model.DAO.Cliente_DAO;
import Model.Entidades.Cliente;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author herik
 */
public class Cadastro_ClientesController implements Initializable {
    
    public static boolean isOpen;
    public Cliente cliente;
    
    @FXML
    private Text text_titulo;
    @FXML
    private TextField txt_nome;
    @FXML
    private TextField txt_cpf;
    @FXML
    private TextField txt_telContato;
    @FXML
    private TextField txt_dataNascimento;
    @FXML
    private TextField txt_email;
    @FXML
    private TextField txt_cartao;
    @FXML
    private TextField txt_rua;
    @FXML
    private TextField txt_bairro;
    @FXML
    private TextField txt_numero;
    @FXML
    private Button btn_cadastrarClientes;
    @FXML
    private Button btn_atualizarClientes;
    
    // Limpa os inputs
    private void limparInput(){
        txt_nome.setText("");
        txt_cpf.setText("");
        txt_dataNascimento.setText("");
        txt_numero.setText("");
        txt_rua.setText("");
        txt_bairro.setText("");
        txt_telContato.setText("");
        txt_email.setText("");
        txt_cartao.setText("");
    }
    
    private boolean inputVazio(){
        if(txt_bairro.getText().isEmpty() || txt_cpf.getText().isEmpty() 
                || txt_dataNascimento.getText().isEmpty() || txt_nome.getText().isEmpty()
                || txt_numero.getText().isEmpty() || txt_rua.getText().isEmpty()
                || txt_telContato.getText().isEmpty()){
            
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
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Main.addOnChangeScreen(new Main.onChangeScreen() {
            @Override
            public void onScreenChanged(String novaTela, Object objeto) {
                if(novaTela.equals("telaCadastroC")){
                    btn_atualizarClientes.setVisible(true);
                    btn_cadastrarClientes.setVisible(false);
                    cliente = (Cliente) objeto;
                    txt_nome.setText(cliente.getNome());
                    txt_cpf.setText(cliente.getCpf());
                    txt_dataNascimento.setText(cliente.getData_nascimento());
                    String[] endereco = cliente.getEndereco().split(",");
                    txt_rua.setText(endereco[0]);
                    txt_numero.setText(endereco[1]);
                    txt_bairro.setText(endereco[2]);
                    txt_telContato.setText(cliente.getTel_contato());
                    txt_email.setText(cliente.getEmail());
                    txt_cartao.setText(cliente.getCartao());
                    
                    // Esconde o botão cadastrar e mostra o botão
                    // Atualizar
                    text_titulo.setText("Editar Cliente");
                }
            }
        });

    }    

    @FXML
    private void atualizarCliente(MouseEvent event) throws SQLException{
        if(inputVazio()){
            String endereco = txt_rua.getText() + ", " + txt_numero.getText() + ", " + txt_bairro.getText();

            // Atualizando os dados do funcionario
            cliente.setCpf(txt_cpf.getText());
            cliente.setData_nascimento(txt_dataNascimento.getText());
            cliente.setEndereco(endereco);
            cliente.setNome(txt_nome.getText());
            cliente.setTel_contato(txt_telContato.getText());
            cliente.setCartao(txt_cartao.getText());
            cliente.setEmail(txt_email.getText());

            Cliente_DAO clienteDAO = new Cliente_DAO();

            clienteDAO.atualizar(cliente);

            // Criando alerta para informar o usuário que os dados do 
            // cliente foram atualizados
            Alert alerta = criarAlerta(
                    Alert.AlertType.INFORMATION, 
                    "Sucesso", 
                    "Os dados do cliente foram atualizados", 
                    "Dados Atualizados!"
            );
            alerta.show();

            // Retornando ao padrão da tela de cadastro
            text_titulo.setText("Cadastrar Cliente");

            // Notifica a tela de Gerenciamento para atualizar a tabela
            Main.notificarOuvintes("gerenciamentoClientes", null);

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
    
    @FXML
    private void cadastrarCliente(MouseEvent event) throws SQLException{
        if(inputVazio()){
            String endereco = txt_rua.getText()+","+txt_numero.getText()+","+txt_bairro.getText();

            SistemaController.funcionario_logado.cadastrarCliente(txt_cpf.getText(), 
                    txt_nome.getText(), 
                    txt_telContato.getText(), 
                    txt_dataNascimento.getText(), 
                    endereco, 
                    txt_email.getText(), 
                    txt_cartao.getText()
            );

            // Criando alerta para informar ao usuário que o cadastro foi feito
            Alert alerta = criarAlerta(Alert.AlertType.INFORMATION, "Sucesso", 
                    "O cliente foi cadastrado com sucesso", 
                    "Cliente cadastrado!"
            );

            alerta.show();

            // Notifica o controller Gerenciamento_Clientes para atualizar
            // a tabela de clientes
            Main.notificarOuvintes("gerenciamentoClientes", null);

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
    

    @FXML
    private void cancelar(MouseEvent event){
        fechar(event);
    }
    
        @FXML
    private void fechar(MouseEvent event) {
        Gerenciamento_ClientesController
                .tela_cadastrarCliente.close();
        isOpen = false;
        text_titulo.setText("Cadastrar Funcionario");
        btn_cadastrarClientes.setVisible(true);
        btn_atualizarClientes.setVisible(false);
        limparInput();
    }
    
    @FXML
    private void minimizar(MouseEvent event){
        Gerenciamento_ClientesController
                .tela_cadastrarCliente.setIconified(true);
    }
}
