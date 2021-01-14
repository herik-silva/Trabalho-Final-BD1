package Controllers;

import Model.DAO.Funcionario_DAO;
import Model.Entidades.Funcionario;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController {
    
    public static Scene cadastro; 
    public static Stage tela_cadastro;
    
    @FXML
    private TextField txt_login;

    @FXML
    private PasswordField txt_senha;

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
    void logar(MouseEvent event) throws SQLException {
        Funcionario funcionario;
        String login = txt_login.getText().toLowerCase();
        String senha = txt_senha.getText().toLowerCase();        
        login = login.trim();
        senha = senha.trim();
        
        // Verifica se não foi inserido algum caracter malicioso para 
        // tentativas de SQL Injetcion.
        if(Main.verificarInput(login) && Main.verificarInput(senha)){
            Funcionario_DAO funcionarioDAO = new Funcionario_DAO();
            funcionario = funcionarioDAO.pesquisarPorPK(login);
            if(funcionario != null && (funcionario.getCpf().equals(login) && funcionario.getSenha().equals(senha))){
                // Notifica a tela do sistema um funcionário
                Main.notificarOuvintes("sistema", funcionario);
                Main.trocarTela(2); // Troca para a próxima tela
            }
            else{
                // Cria um alerta de erro
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Login inválido");
                alerta.setHeaderText("Problema no Login");
                alerta.setContentText("Usuário ou Senha inválidos!");
                alerta.show();
            }
        }
    }
    
    @FXML
    void cadastrar(MouseEvent event) throws IOException{
        Parent FXML_cadastro = FXMLLoader
                .load(getClass().getResource("/Fxml/Cadastro_Funcionarios.fxml"));
        
        cadastro = new Scene(FXML_cadastro);
        
        tela_cadastro = new Stage();
        tela_cadastro.setScene(cadastro);
        tela_cadastro.initStyle(StageStyle.UNDECORATED);
        Cadastro_FuncionariosController.isOpen = true;
        tela_cadastro.show();
    }
}
