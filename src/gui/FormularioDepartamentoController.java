package gui;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class FormularioDepartamentoController implements Initializable {

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
    public void onBtSaveAction(){
        System.out.println("Salvoo");
    }
    public void onBtCancelAction(){
        System.out.println("Canceloo");
    }
    private void initializeNodes(){
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtNome, 30);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    initializeNodes();
    }
}
