module covid_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens br.ifsp.covid.view to javafx.fxml;
    exports br.ifsp.covid.view;

    exports br.ifsp.covid.model;
    opens br.ifsp.covid.model to javafx.fxml;

    exports br.ifsp.covid.persistence;
    opens br.ifsp.covid.persistence to javafx.fxml;

    exports br.ifsp.covid.controller;
    opens br.ifsp.covid.controller to javafx.fxml;
}