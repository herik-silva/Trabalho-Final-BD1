package Controllers;

import Model.DAO.Funcionario_DAO;
import Model.Entidades.Cargo;
import Model.Entidades.Funcionario;
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

public class Cadastro_FuncionariosController implements Initializable {
    public static boolean isOpen;
    public ObservableList<String> cargos;
    public static List<String> nomeCargo = new ArrayList<String>();
    public Funcionario f;

    @FXML
    private Text text_titulo;
    
    @FXML
    private Button btn_atualizar;
    
    @FXML
    private Button btn_cadastrar;
    
    @FXML
    private TextField txt_nome;
    @FXML
    private TextField txt_cpf;
    @FXML
    private TextField txt_telContato;
    @FXML
    private TextField txt_dataNascimento;
    @FXML
    private TextField txt_senha;
    @FXML
    private TextField txt_salario;
    @FXML
    private TextField txt_rua;
    @FXML
    private TextField txt_bairro;
    @FXML
    private TextField txt_numero;
    @FXML
    private ComboBox<String> combo_cargo;

    // Limpa os inputs
    private void limparInput(){
        txt_nome.setText("");
        txt_cpf.setText("");
        txt_dataNascimento.setText("");
        txt_numero.setText("");
        txt_rua.setText("");
        txt_bairro.setText("");
        txt_telContato.setText("");
        txt_salario.setText("");
        txt_senha.setText("");
    }
    
    private boolean inputVazio(){
        if(txt_nome.getText().isEmpty() || txt_bairro.getText().isEmpty() 
                || txt_dataNascimento.getText().isEmpty() || txt_cpf.getText().isEmpty() 
                || txt_numero.getText().isEmpty() || txt_rua.getText().isEmpty() 
                || txt_salario.getText().isEmpty() || txt_senha.getText().isEmpty()
                || txt_telContato.getText().isEmpty()){
            
            return false;
        }
        else{
            return true;
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        Main.addOnChangeScreen(new Main.onChangeScreen() {
            @Override
            public void onScreenChanged(String novaTela, Object objeto) {
                if(novaTela.equals("telaCadastro")){
                    f = (Funcionario) objeto;
                    txt_nome.setText(f.getNome());
                    txt_cpf.setText(f.getCpf());
                    txt_dataNascimento.setText(f.getData_nascimento());
                    String[] endereco = f.getEndereco().split(",");
                    txt_rua.setText(endereco[0]);
                    txt_numero.setText(endereco[1]);
                    txt_bairro.setText(endereco[2]);
                    txt_telContato.setText(f.getTel_contato());
                    txt_salario.setText(Float.toString(f.getSalario()));
                    txt_senha.setText(f.getSenha());
                    combo_cargo.getSelectionModel().select(nomeCargo.get(f.getId_cargo()-1));
                    
                    // Esconde o botão cadastrar e mostra o botão
                    // Atualizar
                    btn_cadastrar.setVisible(false);
                    btn_atualizar.setVisible(true);
                    
                    text_titulo.setText("Editar Funcionário");
                }
            }
        });
                        
        // Limpando o ObservableList para garantir que não tera dados
        // duplicados.
        nomeCargo.clear();
        
        // Inserindo o nome de cada cargo
        for(Cargo cargo: Cargo.cargos){
            nomeCargo.add(cargo.getDescricao());
        }
                
        // Inserindo no combo box.
        cargos = FXCollections.observableArrayList(nomeCargo);
        combo_cargo.setItems(cargos);
        combo_cargo.getSelectionModel().selectFirst();
    }
    
    @FXML
    private void atualizarFuncionario(MouseEvent event){
        if(inputVazio()){

            String endereco = txt_rua.getText() + ", " + txt_numero.getText() + ", " + txt_bairro.getText();

            // Atualizando os dados do funcionario
            f.setCpf(txt_cpf.getText());
            f.setData_nascimento(txt_dataNascimento.getText());
            f.setEndereco(endereco);
            f.setId_cargo(combo_cargo.getSelectionModel().getSelectedIndex()+1);
            f.setNome(txt_nome.getText());
            f.setSalario(Float.parseFloat(txt_salario.getText()));
            f.setSenha(txt_senha.getText());
            f.setTel_contato(txt_telContato.getText());

            // Atualiza no banco de dados
            Funcionario_DAO funcionarioDAO = new Funcionario_DAO();
            try {
                funcionarioDAO.atualizar(f);
            } catch (SQLException ex) {
                System.err.println("Erro: " + ex);
            }

            // Mostrando um alerta para informar que o funcionário foi atualizado
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setHeaderText("Dados atualizados!");
            alerta.setTitle("Sucesso");
            alerta.setContentText("Os dados do funcionário foram atualizados!");
            alerta.show();

            // Notifica a tela de Gerenciamento para atualizar a tabela
            Main.notificarOuvintes("gerenciamentoFuncionarios", null);

            fechar(event);
        }
        else{
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setContentText("Verifique qual campo está vazio");
            alerta.setTitle("Erro");
            alerta.setHeaderText("Campo vazio!");
            alerta.show();
        }
    }

    @FXML
    private void cadastrarFuncionario(MouseEvent event){
        if(inputVazio()){
            // Concatenando os campos de endereço.
            String endereco = txt_rua.getText() + ", " + txt_numero.getText() + ", " + txt_bairro.getText();

            // Realizando o Cadastro.
            if(SistemaController.funcionario_logado != null){ // Verifica se existe algum funcionário logado
                SistemaController.funcionario_logado.cadastrarFuncionario(
                        txt_cpf.getText(),
                        txt_nome.getText(),
                        txt_telContato.getText(),
                        txt_dataNascimento.getText(),
                        endereco,
                        txt_senha.getText(),
                        Float.parseFloat(txt_salario.getText()),
                        combo_cargo.getSelectionModel().getSelectedIndex()+1
                );

                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setHeaderText("Funcionário cadastrado!");
                alerta.setTitle("Sucesso");
                alerta.setContentText("O funcionário foi cadastrado com sucesso!");
                alerta.show();

                System.out.println("Notifica a tela de gerenciamento");
                Main.notificarOuvintes("gerenciamentoFuncionarios", null);

                fechar(event);
            }
            else{
                // Apenas no primeiro cadastro
                Funcionario funcionario = new Funcionario(null, null, null, null, null);
                funcionario.cadastrarFuncionario(
                        txt_cpf.getText(),
                        txt_nome.getText(),
                        txt_telContato.getText(),
                        txt_dataNascimento.getText(),
                        endereco,
                        txt_senha.getText(),
                        Float.parseFloat(txt_salario.getText()),
                        combo_cargo.getSelectionModel().getSelectedIndex()+1
                );

                Main.notificarOuvintes("gerenciamentoFuncionarios", null);

                fechar(event);
            }
        }
        else{
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setContentText("Verifique qual campo está vazio");
            alerta.setTitle("Erro");
            alerta.setHeaderText("Campo vazio!");
            alerta.show();
        }
    }

    @FXML
    private void cancelar(MouseEvent event){
        fechar(event);
    }
    
    @FXML
    private void fechar(MouseEvent event){
        if(LoginController.tela_cadastro != null){
            // Verifica se não foi aberto na tela de Login
            if(!LoginController.tela_cadastro.isShowing()){
                Gerenciamento_FuncionariosController
                        .tela_cadastrarFuncionario.close();
                isOpen = false;
                btn_cadastrar.setVisible(false);
                btn_atualizar.setVisible(true);
                text_titulo.setText("Cadastrar Funcionario");
                limparInput();
            }
            else{
                LoginController.tela_cadastro.close();
                isOpen = false;
                limparInput();
            }
        }
        else{
            Gerenciamento_FuncionariosController
                        .tela_cadastrarFuncionario.close();
            isOpen = false;
            btn_cadastrar.setVisible(false);
            btn_atualizar.setVisible(true);
            text_titulo.setText("Cadastrar Funcionario");
            limparInput();
        }
    }
    
    @FXML
    private void minimizar(MouseEvent event){
        // Verifica se não foi aberto na tela de Login
        if(!LoginController.tela_cadastro.isShowing()){
           Gerenciamento_FuncionariosController
                    .tela_cadastrarFuncionario.setIconified(true);
        }
        else{
            LoginController.tela_cadastro.setIconified(true);
        }
    }
    
}
