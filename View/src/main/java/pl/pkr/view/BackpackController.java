package pl.pkr.view;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import pl.pkr.model.Backpack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class BackpackController {

    public TextField txt_key1;
    public TextField txt_key2;
    public TextField txt_key3;
    public Button btn_gen_keys;
    public Button btn_load_keys;
    public Button btn_save_keys;
    public TextField txt_plain_path;
    public TextField txt_cipher_path;
    public Button btn_load_plain;
    public Button btn_save_plain;
    public Button btn_load_cipher;
    public Button btn_save_cipher;
    public TextArea txt_area_plaintext;
    public TextArea txt_area_ciphertext;
    public Button btn_encrypt;
    public Button btn_decrypt;
    public Label txt_label1;
    public Button btn_file_encrypt;
    public Button btn_file_decrypt;

    public void onGenKeysButtonClick(MouseEvent mouseEvent) {
        Backpack bp = new Backpack();
        txt_key1.setText(bp.getW_1().toString() + ", " + bp.getM().toString() + ", " + Arrays.toString(bp.getPrivateKey()));
        txt_key2.setText(Arrays.toString(bp.getPublicKey()));
    }

    public void onLoadKeysButtonClick(MouseEvent mouseEvent) {
         File saveFile = ViewUtil.getFileChooser("Select file to load.").showOpenDialog(new Stage());
        if (saveFile == null) return;

        try ( BufferedReader br = new BufferedReader(new FileReader(saveFile))) {
            Static.loadKeys(
                br.readLine(),
                br.readLine()
            );

            txt_key1.setText(Static.s_key1);
            txt_key2.setText(Static.s_key2);

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.NO).show();
        }
    }

    public void onSaveKeysButtonClick(MouseEvent mouseEvent) {

    }

    public void onLoadPlainButtonClick(MouseEvent mouseEvent) {
    }

    public void onSavePlainButtonClick(MouseEvent mouseEvent) {
    }

    public void onLoadCipherButtonClick(MouseEvent mouseEvent) {
    }

    public void onSaveCipherButtonClick(MouseEvent mouseEvent) {
    }

    public void onEncryptButtonClick(ActionEvent actionEvent) {
        Backpack bp = new Backpack();
        txt_area_ciphertext.setText(Arrays.toString(bp.encryptBytes(txt_area_plaintext.getText().getBytes())));
    }

    public void onDecryptButtonClick(ActionEvent actionEvent) {
    }

    public void onEncryptFileButtonClick(ActionEvent actionEvent) {

    }

    public void onDecryptFileButtonClick(ActionEvent actionEvent) {
    }
}
