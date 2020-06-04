/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Address;
import Model.Customer;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import utils.DBQuery;

/**
 * FXML Controller class
 *
 * @author Marlene
 */
public class CustomerController implements Initializable {
    ////////// Textfields //////////
    @FXML private TextField txtCusId;
    @FXML private TextField txtName;
    @FXML private TextField txtAddress;
    @FXML private TextField txtAddress2;
    @FXML private ComboBox comboCity;
    @FXML private TextField txtPostalCode;
    @FXML private TextField txtPhone;
    @FXML private TextField txtSearchCus;
    ////////// Buttons //////////
    @FXML private Button btAddCus;
    @FXML private Button btUpdateCus;
    @FXML private Button btClear;
    @FXML private Button btDisplayMain;
    @FXML private Button btDeleteCus;
    @FXML private Button btSearchCus;
    ////////// TableView //////////
    @FXML private TableView<Customer> tableViewCustomer;
    @FXML private TableColumn<Customer, String> colCusId;
    @FXML private TableColumn<Customer, String> colName;
    @FXML private TableColumn<Customer, String> colAddress;
    @FXML private TableColumn<Customer, String> colAddress2;
    @FXML private TableColumn<Customer, String> colCity;
    @FXML private TableColumn<Customer, String> colPostalCode;
    @FXML private TableColumn<Customer, String> colCountry;
    @FXML private TableColumn<Customer, String> colPhone;

   
    Stage stage;
    Parent scene;
    Customer cus;
    String cusId;
    Address address;
    
 
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO        
        // Set Auto-Generated CustomerID
        txtCusId.setDisable(true);        

        // Display all Customers in the Tableview        
        tableViewCustomer.setItems(DBQuery.getAllCustomers());
        
        // Set up columns in Customer tableView 
        colCusId.setCellValueFactory(new PropertyValueFactory<>("customerId")); // customerId is how it is named in the Database
        colName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colAddress2.setCellValueFactory(new PropertyValueFactory<>("address2"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        colCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
                
        // Display ComboBox Cities
        comboCity.setItems(DBQuery.getAllCities());  
        
    }    
    
    
    ////////// ADD NEW CUSTOMER //////////
    
    @FXML private void onActionAddCus(ActionEvent event) {
        //clear() method is used to remove all the elements from a Set not to delete them.
        DBQuery.customerList.clear();
        // User input textfields
        String name = txtName.getText();
        String address = txtAddress.getText();
        String address2 = txtAddress2.getText();
        // get String value from comboCity selection
        String coCity = comboCity.getValue().toString(); 
        // get cityId based on coCity value
        String cityId = DBQuery.getCityId(coCity); 
        String postalCode = txtPostalCode.getText();
        String phone = txtPhone.getText();
        String addressId = null;
        
        // pass input textfields to addAddress method        
        DBQuery.addAddress(address, address2, cityId, postalCode, phone); 
        // get addressId from last added address
        addressId = DBQuery.getAddressIdFromLastCustomerAdded();
        // pass customerName and associated addressId
        DBQuery.addCustomer(name, addressId); 
        // Populate Customer tableView
        displayCustomers();
    }
    
    
    ////////// UPDATE CUSTOMER //////////
    
    @FXML private void onActionUpdateCus(ActionEvent event) {
        
        try {
            String customerId = txtCusId.getText();
            String name = txtName.getText();
            String address = txtAddress.getText();
            String address2 = txtAddress2.getText();
            // get String value from comboCity selection
            String coCity = comboCity.getValue().toString(); 
            // get cityId based on coCity value
            String cityId = DBQuery.getCityId(coCity); 
            String postalCode = txtPostalCode.getText();
            String phone = txtPhone.getText();         
            
            DBQuery.updateCustomer(customerId, name, address, address2, cityId, postalCode, phone);
            // Clear list to prevent doubled entries
            DBQuery.customerList.clear();
            // Get current customer list
            DBQuery.getAllCustomers();
        }catch(Exception e) {
          System.out.println("Error editing customer: " + e.getMessage());
        }      
    }

    
    ////////// DELETE CUSTOMER //////////
    
    @FXML private void onActionDeleteCus(ActionEvent event) {
        // select customer
        Customer deleteCus = tableViewCustomer.getSelectionModel().getSelectedItem();
        String customerId = deleteCus.getCustomerId();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to delete this customer?");
        alert.setTitle("Delete Customer");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {            
            //call delete Customer method and pass in value
            DBQuery.deleteCustomer(customerId);           
            // refresh customer list and load tableview again
            DBQuery.customerList.clear();        
            displayCustomers();
        }
    }

    
    ////////// DISPLAY MAINSCREEN //////////
    
    @FXML private void onActionDisplayMain(ActionEvent event) throws IOException {
        DBQuery.customerList.clear();
        DBQuery.cityList.clear();        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow(); 
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    

    ////////// CLEAR TEXTFIELDS //////////
    
    @FXML private void onActionClear(ActionEvent event) {
            txtCusId.setText("");
            txtName.setText("");
            txtAddress.setText("");
            txtAddress2.setText("");
            comboCity.setValue("");
            txtPostalCode.setText("");
            txtPhone.setText("");    
    }

        
    ////////// POPULATE TEXTFIELDS ////////// 
    
    @FXML void onClickDisplaySelectedCustomer(MouseEvent event) {
        // Select a customer in tableview to display details in textfields
        Customer displayCustomer = tableViewCustomer.getSelectionModel().getSelectedItem();
        if (displayCustomer != null) {
            // get data from tableview to textfields
            txtCusId.setText(displayCustomer.getCustomerId());
            txtName.setText(displayCustomer.getCustomerName());
            txtAddress.setText((displayCustomer.getAddress()));
            txtAddress2.setText((displayCustomer.getAddress2()));
            comboCity.setValue(displayCustomer.getCity());
            txtPostalCode.setText(displayCustomer.getPostalCode());
            txtPhone.setText(displayCustomer.getPhone());   
        } else {
            System.out.println("No customer selection");
        }
    }
    
    
    ////////// POPULATE CUSTOMER TABLEVIEW //////////
    
    public void displayCustomers() {        
        tableViewCustomer.setItems(DBQuery.getAllCustomers());
    }

    
   
}
