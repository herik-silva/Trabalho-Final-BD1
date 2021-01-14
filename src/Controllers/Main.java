package Controllers;

import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    
    private static ArrayList<onChangeScreen> ouvintes = new ArrayList<>();
    
    public static Stage stage;
    public static Scene login;
    public static Scene menu;
    private double xOffset=0, yOffset=0;
    
    public static boolean verificarInput(String input){
        boolean valido = true;
        final String caracteresAceitaveis = "abcdefghijklmnopqrstuvwxyz0123456789";
                
        // Validação
        for(int i=0; i<input.length(); i++){
            if(valido){
                for(int k=0; k < caracteresAceitaveis.length(); k++){
                    if(input.charAt(i) == caracteresAceitaveis.charAt(k)){
                        valido = true;
                        break;
                    }
                    else{
                        valido = false;
                    }
                }
            }
        }
        return valido;
    }
    
    public static void trocarTela(int id_tela){
        stage.close();
        switch(id_tela){
            case 1:
                stage.setScene(login);
                stage.show();
                break;
            
            case 2: 
                stage.setScene(menu);
                stage.show();
                break;
        }
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent FXML_login = FXMLLoader.load(getClass().getResource("/Fxml/Login.fxml"));
        login = new Scene(FXML_login);
        
        FXML_login.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        
        FXML_login.setOnMouseDragged(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                stage.setScene(login);
                stage.setX(event.getScreenX()-xOffset);
                stage.setY(event.getScreenY()-yOffset);
            }
        });
        
        Parent FXML_menu = FXMLLoader.load(getClass().getResource("/Fxml/Sistema.fxml"));
        menu = new Scene(FXML_menu);
        
        FXML_menu.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        
        FXML_menu.setOnMouseDragged(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if(SistemaController.isPressed){
                    stage.setScene(menu);
                    stage.setX(event.getScreenX()-xOffset);
                    stage.setY(event.getScreenY()-yOffset);
                }
            }
        });
        
        primaryStage.setTitle("Sale $ell");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(login);
        primaryStage.show();
        stage = primaryStage;        
    }
        
    public static interface onChangeScreen{
       void onScreenChanged(String novaTela, Object objeto);
    }
    
    public static void addOnChangeScreen(onChangeScreen novoOuvinte){
        ouvintes.add(novoOuvinte);
    }
    
    public static void notificarOuvintes(String novatela, Object objeto){
        ouvintes.forEach((_item) -> {
            _item.onScreenChanged(novatela, objeto);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
