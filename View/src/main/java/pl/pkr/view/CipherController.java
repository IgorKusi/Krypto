package pl.pkr.view;

import javafx.scene.control.*;
import javafx.stage.Stage;
import pl.pkr.model.Util;

import java.io.*;
import java.math.BigInteger;

public class CipherController {
    public TextField txt_plain_path;
    public TextField txt_cipher_path;
    public TextArea txt_area_plaintext;
    public TextArea txt_area_ciphertext;
    public TextField txt_key1;
    public TextField txt_key2;
    public TextField txt_key3;


    public void onGenKeysButtonClick() {
        Static.key1 = Util.generateKey();
        Static.key2 = Util.generateKey();
        Static.key3 = Util.generateKey();

        txt_key1.setText(Static.key1.toString(16).toUpperCase());
        txt_key2.setText(Static.key2.toString(16).toUpperCase());
        txt_key3.setText(Static.key3.toString(16).toUpperCase());
    }

    public void onLoadKeysButtonClick() {
        File saveFile = ViewUtil.getFileChooser("Select file to load.").showOpenDialog(new Stage());

        try (BufferedReader br = new BufferedReader(new FileReader(saveFile))) {
            Static.key1 = new BigInteger(br.readLine(), 16);
            Static.key2 = new BigInteger(br.readLine(), 16);
            Static.key3 = new BigInteger(br.readLine(), 16);

            txt_key1.setText(Static.key1.toString(16).toUpperCase());
            txt_key2.setText(Static.key2.toString(16).toUpperCase());
            txt_key3.setText(Static.key3.toString(16).toUpperCase());

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.NO).show();
        }
    }

    public void onSaveKeysButtonClick() {
        File saveFile = ViewUtil.getFileChooser("Select save path.").showSaveDialog(new Stage());

        try (FileWriter fw = new FileWriter(saveFile)) {
            fw.write(Static.key1.toString(16).toUpperCase());
            fw.write("\n");
            fw.write(Static.key2.toString(16).toUpperCase());
            fw.write("\n");
            fw.write(Static.key3.toString(16).toUpperCase());
            fw.write("\n");
            fw.flush();

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.NO).show();
        }
    }

    public void onLoadPlainButtonClick() {

    }

    public void onSavePlainButtonClick() {

    }

    public void onLoadCipherButtonClick() {

    }

    public void onSaveCipherButtonClick() {

    }

    public void onEncryptButtonClick() {

    }

    public void onDecryptButtonClick() {

    }


}