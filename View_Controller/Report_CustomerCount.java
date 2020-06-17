/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Customer;
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
public class Report_CustomerCount implements Initializable {

    @FXML
    private TableView<Report> tableViewReportApptTypesByMonth;
    @FXML
    private TableColumn<Report, String> colCount;
    @FXML
    private TableColumn<Report, String> colCusId;
    @FXML
    private TableColumn<Report, String> colCusName;

    Stage stage;
    Parent scene;  
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        DBQuery.getNumberOfCustomers().clear();
        tableViewReportApptTypesByMonth.setItems(DBQuery.getNumberOfCustomers());
        
        colCount.setCellValueFactory(new PropertyValueFactory<>("count"));
//        colCusId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
//        colCusName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
    }    

    @FXML
    private void onActionDisplayMain(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow(); 
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();       
    }
    
}
