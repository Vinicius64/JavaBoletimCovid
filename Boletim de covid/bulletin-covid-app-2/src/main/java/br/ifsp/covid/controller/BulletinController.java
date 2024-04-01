package br.ifsp.covid.controller;

import br.ifsp.covid.model.Bulletin;
import br.ifsp.covid.model.DuplicatedBulletinException;
import br.ifsp.covid.model.State;
import br.ifsp.covid.persistence.BulletinDao;
import br.ifsp.covid.persistence.BulletinDaoImpl;
import br.ifsp.covid.view.BulletinApp;
import br.ifsp.covid.view.Funcao;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BulletinController {
    @FXML private Button btnCancel;
    @FXML private Button btnSave;
    @FXML private TextField txtCity;
    @FXML private ComboBox selectState;
    @FXML private TextField txtInfected;
    @FXML private TextField txtDeath;
    @FXML private TextField txtIcuRatio;
    @FXML private DatePicker selectDate;
    private Bulletin bulletin;
    private Funcao funcao;
    private List<Bulletin> bulletinsList;

    @FXML
    private void initialize(){
        final var stateNames = Arrays.stream(State.values())
                .map(State::toString)
                .collect(Collectors.toList());
        selectState.setItems(FXCollections.observableArrayList(stateNames));
    }
    public void setBulletinIntoView(Bulletin bulletin){
        txtCity.setText(bulletin.getCity());
        selectState.setValue(State.fromName(String.valueOf(bulletin.getState())));
        txtInfected.setText(String.valueOf(bulletin.getInfected()));
        txtDeath.setText(String.valueOf(bulletin.getDeaths()));
        txtIcuRatio.setText(String.valueOf(bulletin.getIcuRatio()));
        selectDate.setValue(bulletin.getDate());
    }
    public Bulletin getBulletinFromView(){
        if(bulletin==null)bulletin=new Bulletin();
        bulletin.setCity(txtCity.getText());
        bulletin.setState(State.fromName(String.valueOf(selectState.getValue())));
        bulletin.setInfected(Integer.parseInt(txtInfected.getText()));
        bulletin.setDeaths(Integer.parseInt(txtDeath.getText()));
        bulletin.setIcuRatio(Double.parseDouble(txtIcuRatio.getText()));
        bulletin.setDate(selectDate.getValue());
        return bulletin;
    }

    public void cancelInfo(ActionEvent actionEvent) throws IOException {
        BulletinApp.setRoot("bulletin_management");
    }

    public void saveInfo(ActionEvent actionEvent) throws IOException {
        bulletin = getBulletinFromView();
        BulletinDao dao = new BulletinDaoImpl();
        if(!verifyDupl(bulletin)){
            switch (funcao){
                case EDITAR -> dao.update(bulletin);
                case SALVAR -> dao.insert(bulletin);
            }
        }
        BulletinApp.setRoot("bulletin_management");
    }

    private boolean verifyDupl(Bulletin bulletin) {
        List<Bulletin> list = bulletinsList;
        try {
            for (Bulletin item : list) {
                if (item.getCity().equals(bulletin.getCity()) && item.getDate().isEqual(bulletin.getDate()) && item.getId()!=bulletin.getId()){
                    System.out.printf("Entrou");
                    throw new DuplicatedBulletinException();
                }
            }
        }catch (DuplicatedBulletinException e){
            e.alert();
            return true;
        }
        return false;
    }

    public void setBulletin(Bulletin bulletin) {
        this.bulletin = bulletin;
    }

    public void setFuncao(Funcao funcao) {
        this.funcao = funcao;
    }

    public void setBulletinsList(List<Bulletin> bulletinsList) {
        this.bulletinsList = bulletinsList;
    }
}
