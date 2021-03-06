package com.apogee.trackarea.dtoapi.api;

import com.apogee.trackarea.helpers.algo.Point;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Gaussian Blob
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>ChartType.Scatter
 * <li>Series data as a Set
 * <li>Setting marker size
 * <li>Formatting of negative numbers with large magnitude but small differences
 */
@Service
public class ScatterChartApi {

    public XYChart getChart(List<Point> polygonPoints, String seriesName, String title) {
        XYChart chart = new XYChartBuilder().width(450).height(300).build();

        chart.getStyler().setChartTitleVisible(true);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideSW);
        chart.getStyler().setMarkerSize(4);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        chart.getStyler().setChartFontColor(Color.BLACK);
        chart.getStyler().setChartBackgroundColor(Color.WHITE);
        chart.getStyler().setLegendVisible(false);
        chart.setTitle(title);
        List<Double> xData = new LinkedList<>();
        List<Double> yData = new LinkedList<>();

        for (Point point : polygonPoints) {
            xData.add(point.x);
            yData.add(point.y);
        }
        xData.add(polygonPoints.get(0).x);
        yData.add(polygonPoints.get(0).y);

        XYSeries series = chart.addSeries(seriesName, xData, yData);
        series.setMarker(SeriesMarkers.CIRCLE);
        series.setMarkerColor(Color.RED);
        series.setLineColor(new Color(127, 58, 153));
        return chart;

    }

}