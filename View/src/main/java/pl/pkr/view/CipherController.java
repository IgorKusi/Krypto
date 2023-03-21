package pl.pkr.view;

import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pl.pkr.model.DES;
import pl.pkr.model.Util;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;

public class CipherController {
    public TextField txt_plain_path;
    public TextField txt_cipher_path;
    public TextArea txt_area_plaintext;
    public TextArea txt_area_ciphertext;
    public TextField txt_key1;
    public TextField txt_key2;
    public TextField txt_key3;


        public void onGenKeysButtonClick() {
        BigInteger b = Util.generateKey();
        Static.key1 = BitSet.valueOf(b.toByteArray());
        Static.s_key1 = b.toString(16).toUpperCase();
        b = Util.generateKey();
        Static.key2 = BitSet.valueOf(b.toByteArray());
        Static.s_key2 = b.toString(16).toUpperCase();
        b = Util.generateKey();
        Static.key3 = BitSet.valueOf(b.toByteArray());
        Static.s_key3 = b.toString(16).toUpperCase();

        txt_key1.setText(Static.s_key1);
        txt_key2.setText(Static.s_key2);
        txt_key3.setText(Static.s_key3);
    }

    public void onLoadKeysButtonClick() {
        File saveFile = ViewUtil.getFileChooser("Select file to load.").showOpenDialog(new Stage());
        if (saveFile == null) return;

        try (BufferedReader br = new BufferedReader(new FileReader(saveFile))) {
            BigInteger b = new BigInteger(br.readLine(), 16);
            Static.key1 = BitSet.valueOf(b.toByteArray());
            Static.s_key1 = b.toString(16).toUpperCase();
            b = new BigInteger(br.readLine(), 16);
            Static.key2 = BitSet.valueOf(b.toByteArray());
            Static.s_key2 = b.toString(16).toUpperCase();
            b = new BigInteger(br.readLine(), 16);
            Static.key3 = BitSet.valueOf(b.toByteArray());
            Static.s_key3 = b.toString(16).toUpperCase();

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


    }

    public void onDecryptButtonClick() {

    }


}