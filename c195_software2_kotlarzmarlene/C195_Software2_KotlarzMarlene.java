/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195_software2_kotlarzmarlene;

import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DBConnection;

/**
 *
 * @author Marlene
 */
public class C195_Software2_KotlarzMarlene extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
        // Uncomment Login.fxml when app is ready to avoid logging in every time when program starts
         Parent root = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("/View_Controller/Login.fxml"));        
        Scene scene = new Scene(root);        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws SQLException, Exception{
        
        DBConnection.startConnection();
        launch(args);
        DBConnection.closeConnection();
        
    }
    
}
