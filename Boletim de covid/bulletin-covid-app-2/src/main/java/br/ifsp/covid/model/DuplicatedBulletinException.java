package br.ifsp.covid.model;

import javafx.scene.control.Alert;


public class DuplicatedBulletinException extends Exception{
    public void alert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Duplicated bulletin");
        alert.showAndWait();
    }
}
