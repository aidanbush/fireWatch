/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firewatch;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author alan
 */
public class SplitLineChartController implements Initializable {
    @FXML
    private ToggleButton temperatureToggle;
    @FXML
    private ToggleButton precipitationToggle;
    
    @FXML
    private SplitPane chartSplit;
    private CategoryAxis xAxis;
    private ObservableList<String> months;
    private BorderPane tempPane, precipPane;
    @FXML
    private Button updateButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        NumberAxis tempYAxis = new NumberAxis(0, 5, 1);
        NumberAxis precipYAxis = new NumberAxis(-5, 0, 1);
        months = FXCollections.observableArrayList("Jan", "Feb", "Mar");
        xAxis = new CategoryAxis(months);
        
        XYChart.Series<String, Number> tempSeries = new XYChart.Series<>();
        XYChart.Series<String, Number> precipSeries = new XYChart.Series<>();
        
        for (int i = 0; i < months.size(); ++i) {
            tempSeries.getData().add(new XYChart.Data<>(months.get(i), (double) i));
            precipSeries.getData().add(new XYChart.Data<>(months.get(i), (double) -i));
        }
       
        XYChart<String, Number> tempChart = new LineChart<>(xAxis, tempYAxis);
        XYChart<String, Number> precipChart = new LineChart<>(xAxis, precipYAxis);
        
        tempChart.getData().addAll(tempSeries);
        precipChart.getData().addAll(precipSeries);
        
        tempChart.setLegendVisible(false);
        precipChart.setLegendVisible(false);
        
        tempPane = new BorderPane();
        precipPane = new BorderPane();
        
        tempPane.setCenter(tempChart);
        precipPane.setCenter(precipChart);
        
        chartSplit.getItems().set(0, tempPane);
        chartSplit.getItems().set(1, precipPane);
        
    }    
    
    @FXML
    private void togglePrecipitation(ActionEvent event) {
        if (!precipitationToggle.isSelected()) {
            chartSplit.getItems().remove(precipPane);
        } else {
            chartSplit.getItems().add(precipPane);
            chartSplit.setDividerPosition(0, 0.5);
        }
    }

    @FXML
    private void toggleTemperature(ActionEvent event) {
        if (!temperatureToggle.isSelected()) {
            chartSplit.getItems().remove(0);
        } else {
            chartSplit.getItems().add(0, tempPane);
            chartSplit.setDividerPosition(0, 0.5);
        }
    }

}
