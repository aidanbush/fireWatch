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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
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
    @FXML
    private BarChart<Number, Number> barChart;
    @FXML
    private RadioButton numFiresBarRadio;
    @FXML
    private ToggleGroup barRadioGroup;
    @FXML
    private RadioButton burnedBarRadio;

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
        updateWeatherData();
    }

    @FXML
    private void pieUpdateDateRange(ActionEvent event) {
        if (pieCauseRadio.isSelected()) {
            updateCauseData();
        } else if (pieWeatherRadio.isSelected()) {
            updateWeatherData();
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
    
    private void updateWeatherData() {
        
    }

    @FXML
    private void barGroupByNumFires(ActionEvent event) {
        barChart.getData().clear();
        List<Wildfire> fires = dm.getRangeInclusive("1900-01-01", "3000-01-01");

        Map<Integer, Integer> counts = new HashMap<>();
        for (Wildfire wf : fires) {
            int year = wf.getYear();
            
            if (counts.containsKey(year)) {
                counts.put(year, counts.get(year) + 1);
            } else {
                counts.put(year, 1);
            }
        }
                
        XYChart.Series bcseries = new XYChart.Series();
        counts.keySet().stream().forEach((year) -> {
            bcseries.getData().add(new XYChart.Data(Integer.toString(year), counts.get(year)));
        });
        
        barChart.getData().add(bcseries);
    }

    @FXML
    private void barGroupByHaBurned(ActionEvent event) {
        barChart.getData().clear();
        List<Wildfire> fires = dm.getRangeInclusive("1900-01-01", "3000-01-01");

        Map<Integer, Double> counts = new HashMap<>();
        for (Wildfire wf : fires) {
            int year = wf.getYear();
            
            if (counts.containsKey(year)) {
                counts.put(year, counts.get(year) + wf.getSize());
            } else {
                counts.put(year, wf.getSize());
            }
        }
                
        XYChart.Series bcseries = new XYChart.Series();
        counts.keySet().stream().forEach((year) -> {
            bcseries.getData().add(new XYChart.Data(Integer.toString(year), counts.get(year)));
        });
        
        barChart.getData().add(bcseries);
    }
    
}
