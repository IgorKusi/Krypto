package pl.pkr.view;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pl.pkr.model.DES;
import pl.pkr.model.DESX;
import pl.pkr.model.Util;

import java.io.*;
import java.nio.file.Files;

public class CipherController {
    public TextField txt_plain_path;
    public TextField txt_cipher_path;
    public TextArea txt_area_plaintext;
    public TextArea txt_area_ciphertext;
    public TextField txt_key1;
    public TextField txt_key2;
    public TextField txt_key3;


    public void onGenKeysButtonClick() {
        Static.loadKeys(
                Util.generateDesKey(),
                Util.generateDesKey(),
                Util.generateDesKey()
        );

        txt_key1.setText(Static.s_key1);
        txt_key2.setText(Static.s_key2);
        txt_key3.setText(Static.s_key3);
    }

    public void onLoadKeysButtonClick() {
        File saveFile = ViewUtil.getFileChooser("Select file to load.").showOpenDialog(new Stage());
        if (saveFile == null) return;

        try (BufferedReader br = new BufferedReader(new FileReader(saveFile))) {
            Static.loadKeys(
                br.readLine(),
                br.readLine(),
                br.readLine()
            );

            txt_key1.setText(Static.s_key1);
            txt_key2.setText(Static.s_key2);
            txt_key3.setText(Static.s_key3);

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.NO).show();
        }
    }

    public void onSaveKeysButtonClick() {
        File saveFile = ViewUtil.getFileChooser("Select save path.").showSaveDialog(new Stage());
        if (saveFile == null) return;

        try (FileWriter fw = new FileWriter(saveFile)) {
            fw.write(Static.s_key1);
            fw.write("\n");
            fw.write(Static.s_key2);
            fw.write("\n");
            fw.write(Static.s_key3);
            fw.write("\n");
            fw.flush();

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.NO).show();
        }
    }

    public void onLoadPlainButtonClick() {
        File loadFile = ViewUtil.getFileChooser("Select file to load.").showOpenDialog(new Stage());
        txt_area_plaintext.setText(ViewUtil.readFile(loadFile));
        txt_plain_path.setText(loadFile.getAbsolutePath());
    }

    public void onSavePlainButtonClick() {
        File saveFile = ViewUtil.saveFile(txt_area_plaintext.getText());
        if (saveFile != null)
            txt_plain_path.setText(saveFile.getAbsolutePath());
    }

    public void onLoadCipherButtonClick() {
        File loadFile = ViewUtil.getFileChooser("Select file to load.").showOpenDialog(new Stage());
        txt_area_ciphertext.setText(ViewUtil.readFile(loadFile));
        txt_cipher_path.setText(loadFile.getAbsolutePath());
    }

    public void onSaveCipherButtonClick() {
        File saveFile = ViewUtil.saveFile(txt_area_ciphertext.getText());
        if (saveFile != null)
            txt_cipher_path.setText(saveFile.getAbsolutePath());
    }


    public void onEncryptButtonClick() {
        Static.loadKeys(
                txt_key1.getText(),
                txt_key2.getText(),
                txt_key3.getText()
        );

        DES des = new DESX(Static.key1, Static.key2, Static.key3);
        String ciphertext = des.encrypt_string(txt_area_plaintext.getText());
        txt_area_ciphertext.setText(ciphertext);
    }

    public void onDecryptButtonClick() {
        Static.loadKeys(
                txt_key1.getText(),
                txt_key2.getText(),
                txt_key3.getText()
        );

        DES des = new DESX(Static.key1, Static.key2, Static.key3);
        String plaintext = des.decrypt_string(txt_area_ciphertext.getText());
        txt_area_plaintext.setText(plaintext);
    }


    public void onEncryptFileButtonClick(ActionEvent actionEvent) throws IOException {
        Static.loadKeys(
                txt_key1.getText(),
                txt_key2.getText(),
                txt_key3.getText()
        );

        DES des = new DESX(Static.key1, Static.key2, Static.key3);
        File loadFile = ViewUtil.getFileChooser("Select file to load.").showOpenDialog(new Stage());
        byte[] fBytes = Files.readAllBytes(loadFile.toPath());

        String plaintext = new String(fBytes);
        txt_area_plaintext.setText(plaintext);

        String ciphertext = des.encrypt_bytes(fBytes);

        txt_area_ciphertext.setText(ciphertext);
        ViewUtil.saveFile(ciphertext);
    }

    public void onDecryptFileButtonClick(ActionEvent actionEvent) {
        Static.loadKeys(
                txt_key1.getText(),
                txt_key2.getText(),
                txt_key3.getText()
        );

        DES des = new DESX(Static.key1, Static.key2, Static.key3);
        File loadFile = ViewUtil.getFileChooser("Select file to load.").showOpenDialog(new Stage());

        String ciphertext = ViewUtil.readFile(loadFile);
        txt_area_ciphertext.setText(ciphertext);

        byte[] plaintext = des.decrypt_to_bytes(ciphertext);

        txt_area_plaintext.setText(new String(plaintext));
        ViewUtil.saveFile(plaintext);
    }
}