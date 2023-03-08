module pl.pkr.view {
    requires javafx.controls;
    requires javafx.fxml;
    requires Model;

    opens pl.pkr.view to javafx.fxml;
    exports pl.pkr.view;
}