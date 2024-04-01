package br.ifsp.covid.controller;

import br.ifsp.covid.model.Bulletin;
import br.ifsp.covid.model.State;
import br.ifsp.covid.persistence.BulletinDao;
import br.ifsp.covid.persistence.BulletinDaoImpl;
import br.ifsp.covid.view.BulletinApp;
import br.ifsp.covid.view.Funcao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class BulletinManagementController {

    @FXML private TableView<Bulletin> tableView;
    @FXML private TableColumn<Bulletin, String> cCity;
    @FXML private TableColumn<Bulletin, LocalDate> cDate;
    @FXML private TableColumn<Bulletin, String> cState;
    @FXML private TableColumn<Bulletin, Integer> cId;
    @FXML private TableColumn<Bulletin, Integer> cInfected;
    @FXML private TableColumn<Bulletin, Double> cIcuRatio;
    @FXML private TableColumn<Bulletin, Integer> cDeaths;

    @FXML private ComboBox<String> cbState;
    @FXML private DatePicker dpEnd;
    @FXML private DatePicker dpBegin;
    @FXML private TextField txtCity;

    @FXML private Label lbAverageIcu;
    @FXML private Label lbTotalDeaths;
    @FXML private Label lbTotalInfected;

    private List<Bulletin> databaseData;
    private ObservableList<Bulletin> tableData;

    @FXML
    private void initialize() {
        loadStates();
        bindTableViewToItemsList();
        bindColumnsToValueSources();
        loadDataAndUpdateTable();
    }

    private void loadStates() {
        final var stateNames = Arrays.stream(State.values())
                .map(State::toString)
                .collect(Collectors.toList());
        cbState.setItems(FXCollections.observableArrayList(stateNames));
    }

    private void bindTableViewToItemsList() {
        tableData = FXCollections.observableArrayList();
        tableView.setItems(tableData);
    }

    private void bindColumnsToValueSources() {
        cId.setCellValueFactory(new PropertyValueFactory<>("id"));
        cCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        cState.setCellValueFactory(new PropertyValueFactory<>("state"));
        cInfected.setCellValueFactory(new PropertyValueFactory<>("infected"));
        cDeaths.setCellValueFactory(new PropertyValueFactory<>("deaths"));
        cIcuRatio.setCellValueFactory(new PropertyValueFactory<>("IcuRatio"));
        cDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    private void loadDataAndUpdateTable() {
        BulletinDao dao = new BulletinDaoImpl();
        databaseData = dao.findAll();
        updateTable(databaseData);
        updateStatistics(databaseData);
    }

    private void updateTable(List<Bulletin> data){
        tableData.clear();
        tableData.addAll(data);
    }

    @FXML
    public void newBulletin() throws IOException {
        BulletinApp.setRoot("bulletin");
        BulletinController controller = (BulletinController) BulletinApp.getController();
        controller.setFuncao(Funcao.SALVAR);
        controller.setBulletinsList(databaseData);
    }

    @FXML
    public void editBulletin() throws IOException {
        Bulletin bulletin = tableView.getSelectionModel().getSelectedItem();
        if (bulletin==null) return;
        BulletinApp.setRoot("bulletin");
        BulletinController controller = (BulletinController) BulletinApp.getController();
        controller.setBulletinIntoView(bulletin);
        controller.setBulletin(bulletin);
        controller.setBulletinsList(databaseData);
        controller.setFuncao(Funcao.EDITAR);
    }

    @FXML
    public void removeBulletin() {
        Bulletin bulletin = tableView.getSelectionModel().getSelectedItem();
        if(bulletin==null)return;
        BulletinDao dao = new BulletinDaoImpl();
        dao.delete(bulletin.getId());
        loadDataAndUpdateTable();
    }

    @FXML
    public void filter() {
        List<Bulletin> list = databaseData;
        if(cbState.getValue()!=null){
            list = list.stream().filter(bulletin -> String.valueOf(bulletin.getState()).equals(cbState.getValue())).toList();
        }
        if(dpBegin.getValue()!=null && dpEnd.getValue()!=null){
            list = list.stream().filter(bulletin -> bulletin.getDate().isAfter(dpBegin.getValue())
                    && bulletin.getDate().isBefore(dpEnd.getValue())).toList();
        }
        if(!txtCity.getText().equals("")){
            list = list.stream().filter(bulletin -> bulletin.getCity().equals(txtCity.getText())).toList();
        }
        updateTable(list);
    }

    private void updateStatistics(List<Bulletin> data) {
        int totalDeath = 0;
        int totalInfect = 0;
        double leitos = 0;
        for (Bulletin bulletin : data) {
            totalInfect += bulletin.getInfected();
            totalDeath += bulletin.getDeaths();
            leitos += bulletin.getIcuRatio();
        }
        lbAverageIcu.setText(String.format("%.2f%%",leitos/data.size()));
        lbTotalDeaths.setText(String.valueOf(totalDeath));
        lbTotalInfected.setText(String.valueOf(totalInfect));
    }
}
