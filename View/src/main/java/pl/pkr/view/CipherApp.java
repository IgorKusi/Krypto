package pl.pkr.view;



import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CipherApp extends Application {
    public static Scene backpackScene, DESXScene;
    private static Stage stejcz;
    @Override
    public void start(Stage stage) throws IOException {

        stejcz = stage;
        backpackScene = new Scene( new FXMLLoader(CipherApp.class.getResource("backpack-view.fxml")).load());
        DESXScene = new Scene( new FXMLLoader(CipherApp.class.getResource("cipher-view.fxml")).load());
        stage.setResizable(false);
        stage.setTitle("Cipher Tool by GL, IK - FTIMS 2023");
        stage.setScene(backpackScene);
        stage.show();

    }
    public static void switchScenetoDesX(){
        stejcz.setScene(DESXScene);
    }
    public static void switchScenetoBackpack(){
        stejcz.setScene(backpackScene);
    }

    public static void main(String[] args) {
        launch();
    }
}