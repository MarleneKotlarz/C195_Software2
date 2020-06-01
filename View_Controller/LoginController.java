/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DBQuery;


/**
 * FXML Controller class
 *
 * @author Marlene
 */
public class LoginController implements Initializable {

    @FXML
    private Label labelTitle;
    @FXML
    private Button btLogin;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private Label labelUsername;
    @FXML
    private Label labelPassword;
    @FXML
    private Button btExit;
    
    Stage stage;
    Parent scene;
    ResourceBundle rb;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        rb = ResourceBundle.getBundle("Languages/Language", Locale.getDefault());
        if(Locale.getDefault().getLanguage().equals("de") || 
            Locale.getDefault().getLanguage().equals("en"))
            labelTitle.setText(rb.getString("title"));
            labelUsername.setText(rb.getString("username"));
            labelPassword.setText(rb.getString("password"));
            btLogin.setText(rb.getString("buttonLogin"));
            btExit.setText(rb.getString("buttonExit")); 

    }    

    @FXML
    private void onActionLogin(ActionEvent event) throws IOException {
        // ResourceBundle added for error messages.
        rb = ResourceBundle.getBundle("Languages/Language", Locale.getDefault());
        
        String userNameInput = txtUsername.getText();
        String passwordInput = txtPassword.getText();
        
        //Used to have this code first for the try/if 
        //boolean login = DBQuery.checkLogin(userNameInput, passwordInput);
        //if(login)
        
        try {
            if(DBQuery.checkLogin(userNameInput, passwordInput)) {
                stage = (Stage)((Button)event.getSource()).getScene().getWindow(); 
                scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("errorTitle"));
            alert.setContentText(rb.getString("errorText"));
            alert.showAndWait();            
        }
        } catch (IOException e) {
            System.out.println(e.getMessage());         
        }
    }

    @FXML
    private void onActionExit(ActionEvent event) {
        
        // Exit pop-up window
        rb = ResourceBundle.getBundle("Languages/Language", Locale.getDefault());        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(rb.getString("buttonExit"));
        alert.setHeaderText(rb.getString("confirmExit"));
        alert.setContentText(rb.getString("confirmExitTxt"));

        // OK-Button exit pop-up window action 
        Optional<ButtonType> result = alert.showAndWait();        
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
        else {
            System.out.println("You clicked cancel.");
        }
    }
    
}
