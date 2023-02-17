package edu.uw.bothell.css.dsl.mass.apps.abc.utils;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class LineGraphHelper extends Application {

    public float[] histogram;
    public float[][] classes;

    public void setData(float[] histogram, float[][] classes)
    {
        this.histogram = new float[256];
        this.classes = new float[3][256];
    }

    @Override public void start(Stage stage) {
        this.histogram = new float[256];
        this.classes = new float[3][256];

        stage.setTitle("Histogram Details of Camera Man Image");
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Pixel Intensity");

        final LineChart<Number,Number> lineChart = new LineChart<>(xAxis, yAxis);

        lineChart.setTitle("Histogram");

        XYChart.Series<Number, Number> series_originalHist = new XYChart.Series<>();
        series_originalHist.setName("Original Image Histogram");
        for (int i = 0; i < 256; i++)
        {
            series_originalHist.getData().add(new XYChart.Data<>(i, histogram[i]));
        }

        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().add(series_originalHist);

        for (int k = 0; k < classes.length; k++)
        {
            XYChart.Series<Number, Number> estimatedHists = new XYChart.Series<>();
            estimatedHists.setName("Estimated Histogram Class " + k);
            for (int i =0; i < 256; i++) {
                estimatedHists.getData().add(new XYChart.Data<>(k, classes[k][i]));
            }
            lineChart.getData().add(estimatedHists);
        }

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}