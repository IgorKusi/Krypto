package pl.pkr.view;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ViewUtil {
    static FileChooser getFileChooser() {
        FileChooser fc = new FileChooser();

        File dir = new File(System.getenv("LOCALAPPDATA") + "/PKR");
        if (dir.exists() || dir.mkdirs()) {
            fc.setInitialDirectory(dir);
        }

        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("all", "*.*"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("key files (.keys)", "*.keys"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("text files (.txt)", "*.txt"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("encrypted files (.desx)", "*.desx"));
        return fc;
    }

    public static FileChooser getFileChooser(String title) {
        FileChooser fc = getFileChooser();
        fc.setTitle(title);
        return fc;
    }

    public static String readFile(File loadFile) {
        StringBuilder returnText = new StringBuilder();

        if (!loadFile.canRead()){
            new Alert(Alert.AlertType.ERROR, new IOException().getMessage(), ButtonType.FINISH).show();
        }

        try (FileReader fr = new FileReader(loadFile)) {
            int read;
            while ((read = fr.read()) != -1) {
                char c = (char) read;
                returnText.append(c);
            }

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.NO).show();
            return null;
        }

        return returnText.toString();
    }

    public static File saveFile(String fileContent){
        File saveFile = ViewUtil.getFileChooser("Select save path.").showSaveDialog(new Stage());

        try(FileWriter fw = new FileWriter(saveFile)){
            fw.write(fileContent);

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.YES).show();
            return null;
        }

        return saveFile;
    }
}