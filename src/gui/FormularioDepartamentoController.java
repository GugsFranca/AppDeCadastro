package gui;

import db.DbException;
import gui.gui.listeners.DataChangeListener;
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
import model.exceptions.ValidationException;
import model.services.DepartamentoService;

import java.net.URL;
import java.util.*;

public class FormularioDepartamentoController implements Initializable {

    private Department entity;

    private DepartamentoService service;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

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
        ValidationException exception = new ValidationException("Erro de Validação");
        obj.setId(Utils.tryParseToInt(txtId.getText()));

        if (txtNome.getText() == null || txtNome.getText().trim().isEmpty()) {
            exception.addError("nome", "O campo nome não pode ser vazio ");
        }
        obj.setNome(txtNome.getText());
        if (exception.getErrors().size() > 0) {
            throw exception;
        }

        return obj;
    }

    public void setDepartment(Department entity) {
        this.entity = entity;
    }

    public void setDerpartmenteService(DepartamentoService service) {
        this.service = service;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    private void notifyDatachangeListeners() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChange();
        }
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
            notifyDatachangeListeners();
            Utils.currentStage(event).close();
        } catch (ValidationException e) {
            setErrorMessages(e.getErrors());
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

    private void setErrorMessages(Map<String, String> error) {
        Set<String> fields = error.keySet();
        if (fields.contains("nome")) {
            labelErrorName.setText(error.get("nome"));
        }
    }
}
