package gui;

import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartamentoService;

import java.net.URL;
import java.util.ResourceBundle;

public class FormularioDepartamentoController implements Initializable {

    private Department entity;

    private DepartamentoService service;

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNome;
    @FXML
    private Label labelErrorName;
    @FXML
    private Button btSalvar;
    @FXML
    private Button btCancelar;

    private Department getFormData() {//pega os dados do formulario
        Department obj = new Department();
        obj.setId(Utils.tryParseToInt(txtId.getText()));
        obj.setNome(txtNome.getText());

        return obj;
    }

    @FXML
    public void onBtSaveAction(ActionEvent event) {
        if (entity == null) {
            throw new IllegalStateException("Entidade está nula");
        }
        if (service == null) {
            throw new IllegalStateException("Entidade está nula");
        }
        try {
            entity = getFormData();
            service.saveOrUpdate(entity);
            Utils.currentStage(event).close();
        } catch (DbException e) {
            Alerts.showAlert("Erro ao salvar objeto", null, e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    public void onBtCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtNome, 30);
    }

    public void setDepartment(Department entity) {
        this.entity = entity;
    }

    public void setDerpartmenteService(DepartamentoService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    public void updateFormData() {
        if (entity == null) {
            throw new IllegalStateException("Entidade está nula");
        }
        txtId.setText(String.valueOf(entity.getId()));
        txtNome.setText(entity.getNome());
    }
}
