/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Appointment;
import Model.Report;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utils.DBQuery;

/**
 * FXML Controller class
 *
 * @author Marlene
 */
public class Report_ApptTypesByMonth implements Initializable {

    ////////// APPOINTMENT TYPE TABLEVIEW //////////
    @FXML private TableView<Report> tableViewReportApptTypesByMonth;
    @FXML private TableColumn<Report, String> colMonth;
    @FXML private TableColumn<Report, String> colTypeCount;
    @FXML private TableColumn<Report, String> colType;


    Stage stage;
    Parent scene;    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        DBQuery.getNumberOfApptTypesByMonth().clear();
        //Display Number of appt types by month in tableview by calling DB method
        tableViewReportApptTypesByMonth.setItems(DBQuery.getNumberOfApptTypesByMonth());
        // Set up columns in tableView
        colMonth.setCellValueFactory(new PropertyValueFactory<>("month"));
        colTypeCount.setCellValueFactory(new PropertyValueFactory<>("typeCount"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
    }    
    
    @FXML
    private void onActionDisplayMain(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow(); 
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();        
    }
    
}
