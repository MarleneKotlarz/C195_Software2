/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Report;
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
public class Report_ApptFilteredByTime implements Initializable {
    ////////// REPORT TABLEVIEW //////////
    @FXML private TableView<Report> tableViewApptFilteredByTime;
    @FXML private TableColumn<Report, String> colApptCount;
    @FXML private TableColumn<Report, String> colApptTime;    

    Stage stage;
    Parent scene;  
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        //DBQuery.reportApptFilteredByTime().removeAll();
        tableViewApptFilteredByTime.setItems(DBQuery.reportApptFilteredByTime());        
        colApptCount.setCellValueFactory(new PropertyValueFactory<>("apptCount"));
        colApptTime.setCellValueFactory(new PropertyValueFactory<>("apptTime"));
    
    }    

    
    //-------- RETURN TO MAIN SCREEN --------//
    
    @FXML private void onActionDisplayMain(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow(); 
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();       
    }
    
}
