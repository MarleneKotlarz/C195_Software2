/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Marlene
 */
public class MainScreenController implements Initializable {

    @FXML
    private TextField txtApptID;
    @FXML
    private TextField txtCustomer;
    @FXML
    private TextField txtTitle;
    @FXML
    private TextField txtDescription;
    @FXML
    private Button btAddAppt;
    @FXML
    private ComboBox<?> comboType;
    @FXML
    private DatePicker dateAppt;
    @FXML
    private ComboBox<?> comboStart;
    @FXML
    private ComboBox<?> comboEnd;
    @FXML
    private Button btUpdateAppt;
    @FXML
    private Button btSelectCustomer;
    @FXML
    private TableView<?> tableViewCustomer;
    @FXML
    private TableColumn<?, ?> colCusId;
    @FXML
    private TableColumn<?, ?> colCusName;
    @FXML
    private TextField txtSearchCustomer;
    @FXML
    private Button btDeleteAppt;
    @FXML
    private Button btSearchAppt;
    @FXML
    private TextField txtSearchAppointment;
    @FXML
    private RadioButton rbtByMonth;
    @FXML
    private ToggleGroup TG;
    @FXML
    private RadioButton rbtByWeek;
    @FXML
    private RadioButton rbtAll;
    @FXML
    private DatePicker dateView;
    @FXML
    private Label labelTitle;
    @FXML
    private TableView<?> tableViewAppt;
    @FXML
    private TableColumn<?, ?> colApptId;
    @FXML
    private TableColumn<?, ?> colCusAppt;
    @FXML
    private TableColumn<?, ?> colTitle;
    @FXML
    private TableColumn<?, ?> colType;
    @FXML
    private TableColumn<?, ?> colDate;
    @FXML
    private TableColumn<?, ?> colStart;
    @FXML
    private TableColumn<?, ?> colEnd;
    @FXML
    private Button btDisplayCus;
    @FXML
    private Button btDisplayReports;
    @FXML
    private Button btLogout;
    
    Stage stage;
    Parent scene;

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void onActionAddAppt(ActionEvent event) {
    }

    @FXML
    private void onActionDateAppt(ActionEvent event) {
    }

    @FXML
    private void onActionUpdateAppt(ActionEvent event) {
    }

    @FXML
    private void onActionSearchAppt(ActionEvent event) {
    }

    @FXML
    private void onActionDeleteAppt(ActionEvent event) {
    }

    @FXML
    private void onActionRbtByMonth(ActionEvent event) {
    }

    @FXML
    private void onActionRbtByWeek(ActionEvent event) {
    }

    @FXML
    private void onActionRbtViewAll(ActionEvent event) {
    }

    @FXML
    private void onActionDateView(ActionEvent event) {
    }

    @FXML
    private void onActionDisplayCus(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow(); 
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/Customer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();        
    }

    @FXML
    private void onActionDisplayReports(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow(); 
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/Reports.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();        
    }

    @FXML
    private void onActionLogout(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow(); 
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/Login.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();        
    }
    
}
