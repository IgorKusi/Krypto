package pl.pkr.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import pl.pkr.model.Util;

public class CipherController {
    @FXML
    private Label welcomeText;

    @FXML
    private TextField txt_key1;

    @FXML
    private TextField txt_key2;

    @FXML
    private TextField txt_key3;



    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void onGenKeysButtonClick() {
        Static.key1 = Util.generateKey();
        Static.key2 = Util.generateKey();
        Static.key3 = Util.generateKey();

        txt_key1.setText(Static.key1.toString(16).toUpperCase());
        txt_key2.setText(Static.key2.toString(16).toUpperCase());
        txt_key3.setText(Static.key3.toString(16).toUpperCase());
    }
}