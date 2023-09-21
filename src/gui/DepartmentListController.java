package gui;

import application.Main;
import db.DbIntegrityException;
import gui.gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable, DataChangeListener {


    private DepartmentService service;

    @FXML
    private TableView<Department> tableViewDepartamento;

    @FXML
    private TableColumn<Department, Integer> tableColumID;
    @FXML
    private TableColumn<Department, String> tableColumnNome;
    @FXML
    TableColumn<Department, Department> tableColumnEDIT;
    @FXML
    TableColumn<Department, Department> tableColumnRemove;
    @FXML
    private Button btNew;

    private ObservableList<Department> obsList;

    @FXML
    public void onBtNewAction(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);
        Department obj = new Department();
        createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
    }

    public void setDepartamentService(DepartmentService service) {
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
        initRemoveButtons();
        initEditButtons();
    }

    private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            DepartmentFormController controller = loader.getController();
            controller.setDepartment(obj);
            controller.setDepartmenteService(new DepartmentService());
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

    private void initEditButtons() {
        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>() {
            private final Button button = new Button("Editar");

            @Override
            protected void updateItem(Department obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                        event -> createDialogForm(
                                obj, "/gui/DepartmentForm.fxml", Utils.currentStage(event)));
            }
        });
    }

    private void initRemoveButtons() {
        tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnRemove.setCellFactory(param -> new TableCell<Department, Department>() {
            private final Button button = new Button("Remover");

            @Override
            protected void updateItem(Department obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> removeEntity(obj));
            }


        });
    }

    private void removeEntity(Department obj) {
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmar", "Está certo disso?");

        if (result.get() == ButtonType.OK) {
            if (service == null) {
                throw new IllegalStateException("Service estava nulo");
            }
            try {
                service.remove(obj);
                updateTableView();
            } catch (DbIntegrityException e) {
                Alerts.showAlert("Erro ao remover item", null, e.getMessage(), Alert.AlertType.ERROR);
            }

        }
    }
}
