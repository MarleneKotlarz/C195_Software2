/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Customer;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import utils.DBQuery;

/**
 * FXML Controller class
 *
 * @author Marlene
 */
public class CustomerController implements Initializable {

    @FXML
    private TextField txtCusId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtAddress2;
    @FXML
    private Button btAddCus;
    @FXML
    private Button btUpdateCus;
    @FXML
    private ComboBox comboCity;
    @FXML
    private TextField txtPostalCode;
    @FXML
    private TextField txtPhone;
    @FXML
    private Button btClear;
    @FXML
    private TableView<Customer> tableViewCustomer;
    @FXML
    private TableColumn<Customer, String> colCusId;
    @FXML
    private TableColumn<Customer, String> colName;
    @FXML
    private TableColumn<Customer, String> colAddress;
    @FXML
    private TableColumn<Customer, String> colAddress2;
    @FXML
    private TableColumn<Customer, String> colCity;
    @FXML
    private TableColumn<Customer, String> colPostalCode;
    @FXML
    private TableColumn<Customer, String> colCountry;
    @FXML
    private TableColumn<Customer, String> colPhone;
    @FXML
    private TextField txtSearchCus;
    @FXML
    private Button btDisplayMain;
    @FXML
    private Button btDeleteCus;
    
    Stage stage;
    Parent scene;
    Customer cus;
    String cusId;
 
    
    // Tableview CustomerList
    ObservableList<Customer> customerTable = FXCollections.observableArrayList();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // Set Auto-Generated CustomerID

        // Populate Customer tableView
        tableViewCustomer.setItems(customerTable);
        
        // Set up columns in Customer tableView 
        colCusId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("Adress"));
        colAddress2.setCellValueFactory(new PropertyValueFactory<>("Adress2"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("City"));
        colPostalCode.setCellValueFactory(new PropertyValueFactory<>("PostalCode"));
        colCountry.setCellValueFactory(new PropertyValueFactory<>("Country"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        
        // Display ComboBox Cities
        comboCity.setItems(DBQuery.getAllCities());   

    }    

    @FXML
    private void onActionAddCus(ActionEvent event) {
        // User input textfields
        String name = txtName.getText();
        String address = txtAddress.getText();
        String address2 = txtAddress2.getText();
        String coCity = comboCity.getValue().toString(); // get String value from comboCity selection
        String cityId = DBQuery.getCityId(coCity); // get cityId based on coCity value
        String postalCode = txtPostalCode.getText();
        String phone = txtPhone.getText();
        String addressId = null;
        
        // pass input textfields to addAddress method
        DBQuery.addAddress(address, address2, cityId, postalCode, phone);     
        addressId = DBQuery.getAddressIdFromLastCostumerAdded(); // get addressId from last added address
        DBQuery.addCustomer(name, addressId); // pass customerName and associated addressId
    }

    @FXML
    private void onActionUpdateCus(ActionEvent event) {
    }

    @FXML
    private void onActionClear(ActionEvent event) {
    }

    @FXML
    private void onActionDisplayMain(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow(); 
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    private void onActionDeleteCus(ActionEvent event) {
    }
    
    // Assumes the city table is prepopulated and linked to country table
    // Set City ComboBox by using String.Converter
//   public void setCityCombo() {       
//       comboCity.setItems(DBQuery.getAllCities());     
//       comboCity.getSelectionModel().getSelectedItem();
//       comboCity.setConverter(new StringConverter<Customer>(){           
//           @Override
//           public String toString(Customer city) {
//               System.out.println(city.getCity());
//
//               return city.getCity();
//           }
//           @Override
//           public Customer fromString(String string) {
//               return comboCity.get               
//           }
//           });
//
//    }    
    
    
}
