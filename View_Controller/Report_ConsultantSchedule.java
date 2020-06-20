/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Appointment;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utils.DBQuery;

/**
 * FXML Controller class
 *
 * @author Marlene
 */
public class Report_ConsultantSchedule implements Initializable {


@FXML private TableView<Appointment> tableViewReportConsultantSchedule;
@FXML private TableColumn<Appointment, String> colApptId;
@FXML private TableColumn<Appointment, String> colUser;
@FXML private TableColumn<Appointment, String> colCusId;
@FXML private TableColumn<Appointment, String> colCusName;
@FXML private TableColumn<Appointment, String> colTitle;
@FXML private TableColumn<Appointment, String> colDesc;
@FXML private TableColumn<Appointment, String> colType;
@FXML private TableColumn<Appointment, String> colStart;
@FXML private TableColumn<Appointment, String> colEnd;

Stage stage;
Parent scene;   
 
/**
* Initializes the controller class.
*/
@Override
public void initialize(URL url, ResourceBundle rb) {
    // TODO
     
    tableViewReportConsultantSchedule.setItems(DBQuery.reportGetAllAppointmentsByUser());
    colApptId.setCellValueFactory(new PropertyValueFactory<>("apptId"));
    colUser.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
    colCusId.setCellValueFactory(new PropertyValueFactory<>("cusId"));
    colCusName.setCellValueFactory(new PropertyValueFactory<>("cusName"));
    colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
    colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
    colType.setCellValueFactory(new PropertyValueFactory<>("type"));
    colStart.setCellValueFactory(new PropertyValueFactory<>("start"));
    colEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
    
 }  

 
@FXML void onActionDisplayMain(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow(); 
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();    
}
  


    
}
