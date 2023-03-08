package pl.pkr.view;

import javafx.stage.FileChooser;

import java.io.File;

public class ViewUtil {
    static FileChooser getFileChooser() {
        FileChooser fc = new FileChooser();

        File dir = new File(System.getenv("LOCALAPPDATA") + "/PKR");
        if (dir.exists() || dir.mkdirs()) {
            fc.setInitialDirectory(dir);
        }

        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("key files", "*.keys"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("all", "*.*"));
        return fc;
    }

    public static FileChooser getFileChooser(String title) {
        FileChooser fc = getFileChooser();
        fc.setTitle(title);
        return fc;
    }
}
