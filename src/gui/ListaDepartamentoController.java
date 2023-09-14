package gui;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Departamento;

import java.net.URL;
import java.util.ResourceBundle;

public class ListaDepartamentoController implements Initializable {

    @FXML
    private TableView<Departamento> tableViewDepartamento;

    @FXML
    private TableColumn<Departamento, Integer> tableColumID;
    @FXML
    private TableColumn<Departamento, String> tableColumnNome;
    @FXML
    private Button btNew;

    @FXML
    public void onBtNewAction(){
        System.out.println("onBtNewAction");
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
}
