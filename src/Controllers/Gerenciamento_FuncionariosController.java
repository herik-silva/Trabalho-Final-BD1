package Controllers;

import Model.Entidades.Funcionario;
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

public class Gerenciamento_FuncionariosController implements Initializable {
    public static List<Funcionario> funcionarios;
    public static Stage tela_cadastrarFuncionario;
    public static Scene cadastrarFuncionario;
    
    public static ObservableList obsFuncionarios;
    
    public Funcionario funcionarioSelecionado;

    @FXML
    private TableView<Funcionario> td_funcionarios;
    @FXML
    private TableColumn<Funcionario, String> col_nome;
    @FXML
    private TableColumn<Funcionario, String> col_cpf;
    @FXML
    private TableColumn<Funcionario, String> col_telContato;
    @FXML
    private TableColumn<Funcionario, Float> col_salario;
    
    public Alert criarAlerta(Alert.AlertType tipo, String titulo, String cabecalho, String conteudo){
        Alert alerta = new Alert(tipo);
        alerta.setHeaderText(cabecalho);
        alerta.setTitle(titulo);
        alerta.setContentText(conteudo);
        
        return alerta;
    }
    
    public void selecionarFuncionario(Funcionario funcionario){
        funcionarioSelecionado = funcionario;
    }
    
    public void carregarTela() throws IOException{
            Parent FXML_cadastro = FXMLLoader.load(getClass().getResource("/Fxml/Cadastro_Funcionarios.fxml"));

            tela_cadastrarFuncionario = new Stage();

            cadastrarFuncionario = new Scene(FXML_cadastro);

            tela_cadastrarFuncionario.setScene(cadastrarFuncionario);
            tela_cadastrarFuncionario.initStyle(StageStyle.UNDECORATED);
    }
    
    public void carregarTabela(){
        col_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        col_cpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        col_telContato.setCellValueFactory(new PropertyValueFactory<>("tel_contato"));
        col_salario.setCellValueFactory(new PropertyValueFactory<>("salario"));
        obsFuncionarios = FXCollections.observableArrayList(Funcionario.funcionarios);
        td_funcionarios.setItems(obsFuncionarios);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarTabela();
        
        td_funcionarios.getSelectionModel().selectedItemProperty().addListener(
                (observador,valorAntigo,novoValor)->selecionarFuncionario(novoValor)
        );
        
        try {
            carregarTela();
        } catch (IOException ex) {
            System.err.println("Erro: " + ex);
        }
        
        Main.addOnChangeScreen(new Main.onChangeScreen() {
            @Override
            public void onScreenChanged(String novaTela, Object objeto) {
                if(novaTela.equals("gerenciamentoFuncionarios")){
                    carregarTabela();
                    td_funcionarios.refresh();
                }
            }
        });
    }    

    @FXML
    private void iniciarFuncionario(MouseEvent event) throws IOException {
       if(!Cadastro_FuncionariosController.isOpen){
            tela_cadastrarFuncionario.show();
            Cadastro_FuncionariosController.isOpen = true;
        }
    }

    @FXML
    private void editarFuncionario(MouseEvent event){
        if(!Cadastro_FuncionariosController.isOpen && funcionarioSelecionado != null){
            Main.notificarOuvintes("telaCadastro", funcionarioSelecionado);
            tela_cadastrarFuncionario.show();
            Cadastro_FuncionariosController.isOpen = true;
        }
    }

    @FXML
    private void removerFuncionario(MouseEvent event) {
        if(funcionarioSelecionado != null){
            
            // Cria um alerta para que o usuário confirme se o funcionário
            // deve ser removido
            Alert alerta = criarAlerta(
                    Alert.AlertType.CONFIRMATION,
                    "Deletar Funcionario",
                    "Deseja Remover?",
                    "Tem certeza que deseja remover "
                            + funcionarioSelecionado.getNome() + "?"
            );
            
            // Verifica se o botão OK foi clicado
            Optional<ButtonType> confirmacao = alerta.showAndWait();
            if(confirmacao.get().equals(ButtonType.OK)){
                SistemaController.funcionario_logado
                        .removerFuncionario(funcionarioSelecionado.getCpf());
                
                alerta.close();

                // Cria um alerta para avisar ao usuário que o funcionário
                // foi removido
                alerta = criarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Sucesso",
                        "Funcionário removido!",
                        funcionarioSelecionado.getNome() + " foi removido!"
                );
                alerta.show();

                funcionarioSelecionado = null;
                carregarTabela();
            }
        }
    }
}