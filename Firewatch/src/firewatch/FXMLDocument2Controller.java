/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firewatch;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;

/**
 * FXML Controller class
 *
 * @author MeganBaluch
 */
public class FXMLDocument2Controller implements Initializable {
    private Set<Object> set = new HashSet<>();
    
    @FXML
    private DatePicker datePicker;
    
    @FXML
    private CheckBox checkbox1;
    @FXML
    private CheckBox checkbox2;
    @FXML
    private CheckBox checkbox3;
    
    @FXML
    public void DateChoosen(ActionEvent event) {
        LocalDate date = datePicker.getValue();
        System.out.println(date);
        
    } 
    @FXML
    public void CheckBoxes(ActionEvent event) {
        if (checkbox1.isSelected()) {
            set.add(checkbox1);
        }
        if (checkbox2.isSelected()) {
            set.add(checkbox2);
        }
        if (checkbox3.isSelected()) {
            set.add(checkbox3);
        }
     System.out.println(set);
    }
    
    @FXML
    public void CreateTables(ActionEvent event) {
        System.out.println("DOES NOTHING");
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    
    
}
