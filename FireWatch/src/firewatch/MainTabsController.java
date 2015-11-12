/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firewatch;

import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author alan
 */
public class MainTabsController implements Initializable {
    @FXML
    private PieChart pieChart;
    @FXML
    private RadioButton pieCauseRadio;
    @FXML
    private ToggleGroup pieRadioGroup;
    @FXML
    private RadioButton pieWeatherRadio;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private Button pieUpdateButton;
    
    private DatabaseModel dm;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            DatabaseModel dm = new DatabaseModel();
        } catch (SQLException e) {
            System.out.println("Error connection to database");
        }
    }    

    @FXML
    private void pieGroupByCause(ActionEvent event) {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                new PieChart.Data("Grapefruit", 13),
                new PieChart.Data("Oranges", 25),
                new PieChart.Data("Plums", 10),
                new PieChart.Data("Pears", 22),
                new PieChart.Data("Apples", 30));
        pieChart.setData(pieChartData);
    }

    @FXML
    private void pieGroupByWeather(ActionEvent event) {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                new PieChart.Data("Megan", 323),
                new PieChart.Data("David", 257),
                new PieChart.Data("Aidan", 53),
                new PieChart.Data("William", 122),
                new PieChart.Data("Brandon", 230));
        pieChart.setData(pieChartData);
    }

    @FXML
    private void pieUpdateDateRange(ActionEvent event) {
        if (pieCauseRadio.isSelected()) {
            System.out.println("Clicked button!");
            try {
                updateCauseData();
            } catch (SQLException | ParseException ex) {System.out.println("Exception:" + ex.toString()); }
        } else if (pieWeatherRadio.isSelected()) {
            //updateWeatherData();
        }
    }
    
    private void updateCauseData() throws SQLException, ParseException {
        System.out.println("updating data");
        String start = fromDate.getValue().toString();
        String end = toDate.getValue().toString();
                String query = "SELECT * FROM Wildfire " +"WHERE fire_start_date > '"
                +start +"' AND ex_fs_date < '" +end +"'";
        
        System.out.println(query);
        List<Wildfire> fires = dm.getRangeInclusive(fromDate.getValue().toString(), toDate.getValue().toString());
                   
        System.out.println("got data");
        Map<String, Integer> data = new HashMap<>();
        for (Wildfire wf : fires) {
            String cause = wf.getGenCause();
            if (cause == null) continue;
            
            if (data.containsKey(cause)) {
                data.put(cause, data.get(cause) + 1);
            } else {
                data.put(cause, 1);
            }
        }
        
        System.out.println("got causes");
        
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (String cause : data.keySet()) {
            pieChartData.add(new PieChart.Data(cause, data.get(cause)));
            System.out.println(cause + ": " + data.get(cause));
        }
        
        pieChart.setData(pieChartData);
        
    }
    
}
