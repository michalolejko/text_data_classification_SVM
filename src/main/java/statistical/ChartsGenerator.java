package statistical;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.BoxAndWhiskerToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ChartsGenerator {

    private String path;

    public ChartsGenerator(String path) {
        this.path = path;
    }

    /**
     * histogramy zmiennych
     */
    public void generateHistogram(ArrayList<PreparedData> preparedDataList) throws IOException {
        double[] values = new double[preparedDataList.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = preparedDataList.get(i).getImportantContent().length;
        }
        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("key", values, 50);

        JFreeChart histogram = ChartFactory.createHistogram("Liczebnosc slow znaczacych w dokumentach",
                "L. Slow", "Ilosc", dataset);

        generateChart("histogram", histogram, 3000, 4000);
    }

    /**
     * wykresy pudełkowe
     */
    public void generateBoxCharts(ArrayList<PreparedData> preparedDataList, CalculatedData data) throws IOException {

        ArrayList<Number> values = new ArrayList();
        for (int i = 0; i < preparedDataList.size(); i++) {
            values.add(preparedDataList.get(i).getImportantContent().length);
        }

        DefaultBoxAndWhiskerCategoryDataset d = new DefaultBoxAndWhiskerCategoryDataset();
        d.add(values, "DOKUMENTY", "DOKUMENTY");

        final CategoryAxis xAxis = new CategoryAxis("dokument");
        final NumberAxis yAxis = new NumberAxis("L. slow");
        yAxis.setAutoRangeIncludesZero(false);
        final BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
        renderer.setFillBox(true);
        renderer.setSeriesToolTipGenerator(1, new BoxAndWhiskerToolTipGenerator());
        renderer.setMeanVisible(false);
        final CategoryPlot plot = new CategoryPlot(d, xAxis, yAxis, renderer);

        final JFreeChart chart = new JFreeChart(
                "Wykres pudelkowy dla istotnej liczby znakow",
                plot
        );
        generateChart("box", chart, 1000, 600);
    }

    /**
     * wykresy rozrzutu dla wybranych par zmiennych oraz regresji liniowej między tymi
     * zmiennymi.
     */
    public void generateScatterPlot(ArrayList<PreparedData> preparedDataList, CalculatedData data) throws IOException {

        XYDataset dataset = createDataset(preparedDataList);
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Porownanie dlugosci slow wszystkich i istotnych dla dokumentow",
                "X", "Y", dataset);


        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(255, 228, 196));

        generateChart("plot", chart);
    }

    private XYDataset createDataset(ArrayList<PreparedData> preparedDataList) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        XYSeries series1 = new XYSeries("Wszystkie słowa");
        for (int i = 0; i < preparedDataList.size(); i++) {
            series1.add(i, preparedDataList.get(i).getContentTotally().length);
        }

        XYSeries series2 = new XYSeries("Słowa istotne");
        for (int i = 0; i < preparedDataList.size(); i++) {
            series2.add(i, preparedDataList.get(i).getImportantContent().length);
        }

        dataset.addSeries(series2);
        dataset.addSeries(series1);

        return dataset;
    }

    /**
     * wykresów prezentujących wartości zmiennych oznaczającej liczbę wszystkich słów w
     * dokumencie oraz liczbę istotnych słów w dokumencie statystyk,
     */
    public void generateWordsLineChart(ArrayList<PreparedData> preparedDataList) throws IOException {
        XYSeries series = new XYSeries("Totally");
        XYSeries series2 = new XYSeries("Important");
        for (int i = 0; i < preparedDataList.size(); i++) {
            series.add(i, preparedDataList.get(i).getContentTotally().length);
        }
        for (int i = 0; i < preparedDataList.size(); i++) {
            series2.add(i, preparedDataList.get(i).getImportantContent().length);
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series2);
        dataset.addSeries(series);
        //Tworzymy wykres XY
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Wyrazy znaczace",//Tytuł
                "Dokument", // x-axis Opis
                "L. wyrazow", // y-axis Opis
                dataset, // Dane
                PlotOrientation.VERTICAL, // Orjentacja wykresu /HORIZONTAL
                true, // pozkaż legende
                true, // podpowiedzi tooltips
                false
        );
        XYPlot plot = chart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesPaint(1, Color.BLUE);
        renderer.setSeriesStroke(1, new BasicStroke(2.0f));

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinesVisible(false);
        plot.setDomainGridlinesVisible(false);

        generateChart("xychart", chart);
    }

    private void generateChart(String chartName, JFreeChart chart){
        generateChart(chartName, chart, 9000, 2000);
    }

    private void generateChart(String chartName, JFreeChart chart, int width, int height){
        try {
            File chartFile = new File("output/charts/" + path + "/" + chartName + ".png");
            new File("output/charts/" + path + "/").mkdirs();
            chartFile.createNewFile();
            ChartUtils.saveChartAsPNG(chartFile, chart, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
