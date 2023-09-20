package gui;

import application.Main;
import gui.gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartamentoService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ListaDepartamentoController implements Initializable, DataChangeListener {


    private DepartamentoService service;

    @FXML
    private TableView<Department> tableViewDepartamento;

    @FXML
    private TableColumn<Department, Integer> tableColumID;
    @FXML
    private TableColumn<Department, String> tableColumnNome;
    @FXML
    private Button btNew;

    private ObservableList<Department> obsList;

    @FXML
    public void onBtNewAction(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);
        Department obj = new Department();
        createDiologForm(obj, "/gui/FormularioDepartamento.fxml", parentStage);
    }

    public void setDepartamentService(DepartamentoService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();

    }

    private void initializeNodes() {
        tableColumID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewDepartamento.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Service tava nulo");
        }
        List<Department> list = service.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewDepartamento.setItems(obsList);
    }

    private void createDiologForm( Department obj,String absoluteName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            FormularioDepartamentoController controller = loader.getController();
            controller.setDepartment(obj);
            controller.setDerpartmenteService(new DepartamentoService());
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Novo departamento");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();

        } catch (IOException e) {
            Alerts.showAlert("IO Exception", "Erro ao mostrar a janela", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    @Override
    public void onDataChange() {
        updateTableView();
    }
}
