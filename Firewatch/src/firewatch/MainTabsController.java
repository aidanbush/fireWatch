/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firewatch;

import java.net.URL;
import java.sql.Array;
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
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

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
    private DatePicker fromDate2;
    @FXML
    private DatePicker toDate2;
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
    
    //fire list tab
    @FXML
    private DatePicker fireListFromDate;
    @FXML
    private DatePicker fireListToDate;
    @FXML
    private Button fireListUpdateButton;
    @FXML
    private TableView<Wildfire> fireListTableView;
    
    @FXML
    private WebView mapWebView;
    WebEngine webEngine;

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
        fromDate2.setValue(LocalDate.now());
        toDate2.setValue(LocalDate.now());
        //set other from and to dates
        webEngine = mapWebView.getEngine();
        final URL urlGoogleMaps = getClass().getResource("GoogleMapsV3.html");
        webEngine.load(urlGoogleMaps.toExternalForm());
        webEngine.setJavaScriptEnabled(true);

   
    }
    @FXML
    private void submitMapDates(ActionEvent event) {
        List<Wildfire> fires = dm.getRangeInclusive(fromDate2.getValue().toString(), 
        toDate2.getValue().toString());
        Map<Double, double[]> fire_plots = new HashMap<>();
        fires.stream().forEach((wf) -> {
            Double size = wf.getSize();
            double[] coord = wf.getCoordinates();
            fire_plots.put(size, coord);
            //System.out.println("" + coord[0] + " " + coord[1] + ", " + size);
            webEngine.executeScript("addLocation(" + coord[0] + "," + coord[1] + "," + size + ")");
        });
    }
    
    @FXML
    private void pieGroupByCause(ActionEvent event) {
        updateCauseData();
    }

    @FXML
    private void pieGroupByWeather(ActionEvent event) {
        if (pieWeatherRadio.isSelected()) {
            updateWeatherData();
        }
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
        List<Wildfire> fires = dm.getRangeInclusive(fromDate2.getValue().toString(), 
                toDate2.getValue().toString());

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
        List<Wildfire> fires = dm.getRangeInclusive(fromDate2.getValue().toString(), 
                toDate2.getValue().toString());
        
        Map<String, Integer> data_weather = new HashMap<>();
        for (Wildfire wf : fires) {
            String weather = wf.getWeather();
            if (weather == null) continue;
            
            if (data_weather.containsKey(weather)) {
                data_weather.put(weather, data_weather.get(weather) + 1);
            } else {
                data_weather.put(weather, 1);
            }
        }
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (String weather : data_weather.keySet()) {
            PieChart.Data pcd = new PieChart.Data(weather, data_weather.get(weather));
            pieChartData.add(pcd);
        }
        
        pieChart.setData(pieChartData);
        
        DecimalFormat df = new DecimalFormat("#.##");
        pieChart.getData().stream().forEach(pcd -> {
            Tooltip tooltip = new Tooltip();
            tooltip.setText((int) pcd.getPieValue() + 
                    " (" + df.format(pcd.getPieValue()/fires.size()*100) + "%)");
            Tooltip.install(pcd.getNode(), tooltip);
        });
        
        
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
    
    //fireList tab
    @FXML
    private void fireListUpdate(ActionEvent event){
        //clear elements
        
        //gets the list of fires
        List<Wildfire> fires = dm.getRangeInclusive(
                fireListFromDate.getValue().toString(),
                fireListToDate.getValue().toString());
        
        //add to the table
        for(Wildfire fire : fires){
            //fireListTableView.
            //fireListTableView;
            
        }
    }
    
    
}
