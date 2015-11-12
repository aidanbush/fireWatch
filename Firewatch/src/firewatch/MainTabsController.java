/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firewatch;

import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
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
import javafx.scene.control.Tooltip;

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
            dm = new DatabaseModel();
        } catch (SQLException e) {
            System.err.println("Error: cannot connect to database");
        }
        fromDate.setValue(LocalDate.now());
        toDate.setValue(LocalDate.now());
    }    

    @FXML
    private void pieGroupByCause(ActionEvent event) {
        updateCauseData();
    }

    @FXML
    private void pieGroupByWeather(ActionEvent event) {
        //updateWeatherData();
    }

    @FXML
    private void pieUpdateDateRange(ActionEvent event) {
        if (pieCauseRadio.isSelected()) {
            System.out.println("Clicked button!");
            updateCauseData();
        } else if (pieWeatherRadio.isSelected()) {
            //updateWeatherData();
        }
    }
    
    private void updateCauseData() {
        List<Wildfire> fires = dm.getRangeInclusive(fromDate.getValue().toString(), 
                toDate.getValue().toString());

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
                
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (String cause : data.keySet()) {
            PieChart.Data pcd = new PieChart.Data(cause, data.get(cause));
            pieChartData.add(pcd);
        }
        
        pieChart.setData(pieChartData);

        // set tooltips for pie slices
        DecimalFormat df = new DecimalFormat("#.##");
        pieChart.getData().stream().forEach(pcd -> {
            Tooltip tooltip = new Tooltip();
            tooltip.setText((int) pcd.getPieValue() + 
                    " (" + df.format(pcd.getPieValue()/fires.size()*100) + "%)");
            Tooltip.install(pcd.getNode(), tooltip);
        });
    }
    
}
