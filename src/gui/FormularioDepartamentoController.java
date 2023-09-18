package gui;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

import java.net.URL;
import java.util.ResourceBundle;

public class FormularioDepartamentoController implements Initializable {

    private Department entity;

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

    @FXML
    public void onBtSaveAction() {
        System.out.println("Salvoo");
    }

    public void onBtCancelAction() {
        System.out.println("Canceloo");
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtNome, 30);
    }

    public void setDepartment(Department entity){
        this.entity = entity;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    public void updateFormData(){
        if(entity == null){
            throw new IllegalStateException("Entidade está nula");
        }
        txtId.setText(String.valueOf(entity.getId()));
        txtNome.setText(entity.getNome());
    }
}
