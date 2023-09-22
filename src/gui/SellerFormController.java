package gui;

import db.DbException;
import gui.gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class SellerFormController implements Initializable {

    private Seller entity;

    private SellerService service;

    private final List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtEmail;
    @FXML
    private DatePicker dpAniversario;
    @FXML
    private TextField txtSalario;
    @FXML
    private Label labelErrorName;
    @FXML
    private Label labelErrorEmail;
    @FXML
    private Label labelErrorAniversario;
    @FXML
    private Label labelErrorSalario;

    @FXML
    private Button btSalvar;
    @FXML
    private Button btCancelar;

    private Seller getFormData() {//pega os dados do formulario
        Seller obj = new Seller();
        ValidationException exception = new ValidationException("Erro de Validação");
        obj.setId(Utils.tryParseToInt(txtId.getText()));

        if (txtNome.getText() == null || txtNome.getText().trim().isEmpty()) {
            exception.addError("nome", "O campo nome não pode ser vazio ");
        }
        obj.setName(txtNome.getText());
        if (exception.getErrors().size() > 0) {
            throw exception;
        }

        return obj;
    }

    public void setSeller(Seller entity) {
        this.entity = entity;
    }

    public void setSellerService(SellerService service) {
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
        Constraints.setTextFieldMaxLength(txtNome, 70);
        Constraints.setTextFieldDouble(txtSalario);
        Constraints.setTextFieldMaxLength(txtEmail, 60);
        Utils.formatDatePicker(dpAniversario, "dd/MM/yyyy");
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
        txtNome.setText(entity.getName());
        txtEmail.setText(entity.getEmail());
        Locale.setDefault(Locale.US);
        txtSalario.setText(String.format("%.2f", entity.getBaseSalary()));
        if(entity.getBirthDate() != null){
            dpAniversario.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
        }

    }

    private void setErrorMessages(Map<String, String> error) {
        Set<String> fields = error.keySet();
        if (fields.contains("nome")) {
            labelErrorName.setText(error.get("nome"));
        }
    }
}
