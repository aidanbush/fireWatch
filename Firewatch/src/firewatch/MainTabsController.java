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
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private DatePicker fromDate2;
    @FXML
    private DatePicker toDate2;
    @FXML
    private Button pieUpdateButton;
    
    @FXML
    private BarChart<Number, Number> barChart;
    @FXML
    private RadioButton numFiresBarRadio;
    @FXML
    private ToggleGroup barRadioGroup;
    @FXML
    private RadioButton burnedBarRadio;
    @FXML 
    private Tab fireCause;
    @FXML
    private Tab fireList;
    @FXML
    private Tab fireYear;
    @FXML
    private TabPane tabs;
    @FXML
    private Label noFire;
    @FXML
    private Tab map;
    
    
    @FXML
    private TableView<Wildfire> fireListTableView;
    @FXML
    private TableColumn<Wildfire, String> fireNumberColumn;
    @FXML
    private TableColumn<Wildfire, String> yearColumn;
    @FXML
    private TableColumn<Wildfire, String> nameColumn;
    @FXML
    private TableColumn<Wildfire, String> sizeColumn;
    @FXML
    private TableColumn<Wildfire, String> fireClassColumn;
    @FXML
    private TableColumn<Wildfire, String> startDateColumn;
    @FXML
    private TableColumn<Wildfire, String> endDateColumn;
    @FXML
    private TableColumn<Wildfire, String> weatherColumn;
    @FXML
    private TableColumn<Wildfire, String> activeCauseColumn;
    @FXML
    private TableColumn<Wildfire, String> generalCauseColumn;
    
    @FXML
    private WebView mapWebView;
    WebEngine webEngine;
    private ObservableList<Wildfire> fires;
    private DatabaseModel dm;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dm = new DatabaseModel("jdbc:sqlite:FireWatch.sqlite");
        try {
            dm.connect();
        } catch (SQLException e) {
            System.err.println("Error: cannot connect to database");
            Platform.exit();
            System.exit(1);
        }
        fromDate2.setValue(LocalDate.now());
        toDate2.setValue(LocalDate.now());
        webEngine = mapWebView.getEngine();
        final URL urlGoogleMaps = getClass().getResource("GoogleMapsV3.html");
        webEngine.load(urlGoogleMaps.toExternalForm());
        webEngine.setJavaScriptEnabled(true);
        
        setupTableView();
        
        tabs.getSelectionModel().selectedItemProperty().addListener(
            (ObservableValue<? extends Tab> ov, Tab t, Tab t1) -> {
                updateTab(ov, t, t1);
        });
        
        fires = dm.getFires();
        fireListTableView.setItems(fires);
        fires.addListener((ListChangeListener.Change<? extends Wildfire> c) -> {
            updateMap(c);
        });
    }
    
    @FXML
    private void submitMapDates(ActionEvent event) {
        noFire.setText("");
        webEngine.executeScript("deleteMarkers()");
        dm.getRangeInclusive(fromDate2.getValue().toString(), 
                toDate2.getValue().toString());
        if (fires.isEmpty()) {
            noFire.setText("No fires");
        } 
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
        Map<String, Integer> data = new HashMap<>();
        fires.stream().map((wf) -> wf.getGenCause()).filter((cause) ->
                !(cause == null)).forEach((cause) -> {
            if (data.containsKey(cause)) {
                data.put(cause, data.get(cause) + 1);
            } else {
                data.put(cause, 1);
            }
        });
        
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        data.keySet().stream().map((cause) -> new PieChart.Data(cause, data.get(cause))).forEach((pcd) -> {
            pieChartData.add(pcd);
        });
        
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
        Map<String, Integer> data_weather = new HashMap<>();
        fires.stream().map((wf) -> wf.getWeather()).filter((weather) -> !(weather == null)).forEach((weather) -> {
            if (data_weather.containsKey(weather)) {
                data_weather.put(weather, data_weather.get(weather) + 1);
            } else {
                data_weather.put(weather, 1);
            }
        });
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        data_weather.keySet().stream().map((weather) -> new PieChart.Data(weather, data_weather.get(weather))).forEach((pcd) -> {
            pieChartData.add(pcd);
        });
        
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
    private void barGroupByNumFires() {
        barChart.getData().clear();
        
        Map<Integer, Integer> counts = new HashMap<>();
        fires.stream().map((wf) -> wf.getYear()).forEach((year) -> {
            if (counts.containsKey(year)) {
                counts.put(year, counts.get(year) + 1);
            } else {
                counts.put(year, 1);
            }
        });
                
        XYChart.Series bcseries = new XYChart.Series();
        counts.keySet().stream().forEach((year) -> {
            bcseries.getData().add(new XYChart.Data(Integer.toString(year), counts.get(year)));
        });
        
        barChart.getData().add(bcseries);
    }

    @FXML
    private void barGroupByHaBurned(ActionEvent event) {
        barChart.getData().clear();
        //List<Wildfire> fires = dm.getRangeInclusive("1900-01-01", "3000-01-01");

        Map<Integer, Double> counts = new HashMap<>();
        fires.stream().forEach((wf) -> {
            int year = wf.getYear();
            
            if (counts.containsKey(year)) {
                counts.put(year, counts.get(year) + wf.getSize());
            } else {
                counts.put(year, wf.getSize());
            }
        });
                
        XYChart.Series bcseries = new XYChart.Series();
        counts.keySet().stream().forEach((year) -> {
            bcseries.getData().add(new XYChart.Data(Integer.toString(year), counts.get(year)));
        });
        
        barChart.getData().add(bcseries);
    }
    
    private void updateMap(ListChangeListener.Change<? extends Wildfire> c){
        while (c.next()) {
            List<? extends Wildfire> addedSubList = c.getAddedSubList();

            for(int i =0; i <addedSubList.size(); i++){
                //update map
                Double size = addedSubList.get(i).getSize();
                double[] coord = addedSubList.get(i).getCoordinates();
                webEngine.executeScript("addLocation(" + coord[0] + "," + coord[1] + "," + size + ")");
            }
         }
    }
    
    private void updateTab(ObservableValue<? extends Tab> ov, Tab t, Tab t1){
        switch (t1.getId()) {
            case "fireCause":
                if (pieWeatherRadio.isSelected()) {
                    updateWeatherData();
                }
                else {
                    updateCauseData();
                }
                break;
            case "fireYear":
                barGroupByNumFires();
                break;
            default:
                break;
        }
    }
    
    private void setupTableView(){
        fireNumberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        weatherColumn.setCellValueFactory(new PropertyValueFactory<>("weather"));
        activeCauseColumn.setCellValueFactory(new PropertyValueFactory<>("activeCause"));
        generalCauseColumn.setCellValueFactory(new PropertyValueFactory<>("genCause"));
    }
}
