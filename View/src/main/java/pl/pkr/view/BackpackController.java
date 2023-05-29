package pl.pkr.view;

import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import pl.pkr.model.Backpack;
import pl.pkr.model.DES;
import pl.pkr.model.DESX;
import pl.pkr.model.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.Arrays;

public class BackpackController {
    public TextField txt_keyPri;
    public TextField txt_keyPub;
    public TextField txt_M;
    public TextField txt_W1;
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
        Static.bp_M = bp.getM();
        Static.bp_W_1 = bp.getW_1();
        Static.bp_keyPri = bp.getPrivateKey();
        Static.bp_keyPub = bp.getPublicKey();

        txt_M.setText(Static.bp_M.toString());
        txt_W1.setText(Static.bp_W_1.toString());
        txt_keyPri.setText(Arrays.toString(Static.bp_keyPri));
        txt_keyPub.setText(Arrays.toString(Static.bp_keyPub));
    }

    public void onLoadKeysButtonClick(MouseEvent mouseEvent) {
        File saveFile = ViewUtil.getFileChooser("Select file to load.").showOpenDialog(new Stage());
        if ( saveFile == null ) return;

        try ( BufferedReader br = new BufferedReader(new FileReader(saveFile)) ) {
            Static.bp_M = new BigInteger(br.readLine());
            Static.bp_W_1 = new BigInteger(br.readLine());

            Static.bp_keyPri = Util.strToBIArray(br.readLine());
            Static.bp_keyPub = Util.strToBIArray(br.readLine());

            txt_M.setText(Static.bp_M.toString());
            txt_W1.setText(Static.bp_W_1.toString());
            txt_keyPri.setText(Arrays.toString(Static.bp_keyPri));
            txt_keyPub.setText(Arrays.toString(Static.bp_keyPub));

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.NO).show();
        }
    }

    public void onSaveKeysButtonClick(MouseEvent mouseEvent) {
        String sb = String.valueOf(Static.bp_M) + '\n' +
                Static.bp_W_1 + '\n' +
                Arrays.toString(Static.bp_keyPri) + '\n' +
                Arrays.toString(Static.bp_keyPub) + '\n';
        ViewUtil.saveFile(sb);
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

    public void onEncryptButtonClick(ActionEvent actionEvent) {
        Static.bp_M = new BigInteger(txt_M.getText());
        Static.bp_W_1 = new BigInteger(txt_W1.getText());
        Static.bp_keyPri = Util.strToBIArray(txt_keyPri.getText());
        Static.bp_keyPub = Util.strToBIArray(txt_keyPub.getText());

        Backpack bp = new Backpack(Static.bp_keyPub, Static.bp_keyPri, Static.bp_M, Static.bp_W_1);
        txt_area_ciphertext.setText(Arrays.toString(bp.encryptBytes(txt_area_plaintext.getText().getBytes())));
    }

    public void onDecryptButtonClick(ActionEvent actionEvent) {
        Static.bp_M = new BigInteger(txt_M.getText());
        Static.bp_W_1 = new BigInteger(txt_W1.getText());
        Static.bp_keyPri = Util.strToBIArray(txt_keyPri.getText());
        Static.bp_keyPub = Util.strToBIArray(txt_keyPub.getText());

        Backpack bp = new Backpack(Static.bp_keyPub, Static.bp_keyPri, Static.bp_M, Static.bp_W_1);
        txt_area_plaintext.setText(new String(
                bp.decryptBytes(Util.strToBIArray(txt_area_ciphertext.getText()))
        ));
    }

    public void onEncryptFileButtonClick(ActionEvent actionEvent) throws IOException {
        Static.bp_M = new BigInteger(txt_M.getText());
        Static.bp_W_1 = new BigInteger(txt_W1.getText());
        Static.bp_keyPri = Util.strToBIArray(txt_keyPri.getText());
        Static.bp_keyPub = Util.strToBIArray(txt_keyPub.getText());

        Backpack bp = new Backpack(Static.bp_keyPub, Static.bp_keyPri, Static.bp_M, Static.bp_W_1);
        File loadFile = ViewUtil.getFileChooser("Select file to load.").showOpenDialog(new Stage());
        byte[] fBytes = Files.readAllBytes(loadFile.toPath());

        String plaintext = new String(fBytes);
//        txt_area_plaintext.setText(plaintext);

        BigInteger[] ciphertext = bp.encryptBytes(fBytes);

//        txt_area_ciphertext.setText(Arrays.toString(ciphertext));
        ViewUtil.saveFile(Arrays.toString(ciphertext));
    }

    public void onDecryptFileButtonClick(ActionEvent actionEvent) {
        Static.bp_M = new BigInteger(txt_M.getText());
        Static.bp_W_1 = new BigInteger(txt_W1.getText());
        Static.bp_keyPri = Util.strToBIArray(txt_keyPri.getText());
        Static.bp_keyPub = Util.strToBIArray(txt_keyPub.getText());

        Backpack bp = new Backpack(Static.bp_keyPub, Static.bp_keyPri, Static.bp_M, Static.bp_W_1);
        File loadFile = ViewUtil.getFileChooser("Select file to load.").showOpenDialog(new Stage());

        String ciphertext = ViewUtil.readFile(loadFile);
//        txt_area_ciphertext.setText(ciphertext);

        byte[] plaintext = bp.decryptBytes(Util.strToBIArray(ciphertext));

//        txt_area_plaintext.setText(new String(plaintext));
        ViewUtil.saveFile(plaintext);
    }

    public void onSwitchDesxButtonClick(MouseEvent mouseEvent) throws IOException {
        CipherApp.switchScenetoDesX();
    }
}
