package Controllers;

import Model.Entidades.Produto;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;


public class Central_NotificacoesController implements Initializable {

    private List<String> notificacoes = new ArrayList<String>();
    private ObservableList<String> obsNotificacoes;
    
    @FXML
    private ListView<String> list_notificacoes;
    
    private void criarNotificacao(String notificacao){
        notificacoes.add(notificacao);
        atualizarLista();
    }
    
    private void checarEstoque(){
        for(Produto produto: Produto.produtos){
            if(produto.getEstoque() < 10){
                criarNotificacao("O produto " + produto.getNome() + " está com estoque abaixo de 10");
            }
        }
        
    }
    
    private void inicializarLista(){
        obsNotificacoes = FXCollections.observableArrayList(notificacoes);
        list_notificacoes.setItems(obsNotificacoes);
    }
    
    private void atualizarLista(){
        obsNotificacoes = FXCollections.observableArrayList(notificacoes);
        list_notificacoes.setItems(obsNotificacoes);
        list_notificacoes.refresh();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Inicia a lista
        inicializarLista();
        
        // Fica ouvindo sempre que acontecer alguma alteração no estoque
        Main.addOnChangeScreen(new Main.onChangeScreen() {
            @Override
            public void onScreenChanged(String novaTela, Object objeto) {
                if(novaTela.equals("centralNotificacoes")){
                    checarEstoque();
                }
            }
        });
    }    
    
}
