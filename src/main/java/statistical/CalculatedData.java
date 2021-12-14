package statistical;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.io.Serializable;
import java.util.ArrayList;

public class CalculatedData implements Serializable {
    public ArrayList<PreparedData> preparedDataList;

    public double minTotally = Double.MAX_VALUE, maxTotally = Double.MAX_VALUE;
    public double minImportant = 0, maxImportant = 0;
    public double medianTotally, medianImportant;
    public Double arithmeticAverageTotally = null, arithmeticAverageImportant = null;
    public Double standardDeviationTotally = null, standardDeviationImportant = null;
    public double quartileTotally[] = new double[3], quartileImportant[] = new double[3];
    public double interquartileRangeTotally, interquartileRangeImportant;
    public double quantile01Totally, quantile09Totally, quantile01Important, quantile09Important;

    public CalculatedData(ArrayList<PreparedData> preparedDataList) {
        this.preparedDataList = preparedDataList;
        DescriptiveStatistics totallyStats = new DescriptiveStatistics(), importantStats = new DescriptiveStatistics();
        for (PreparedData pd : preparedDataList) {
            totallyStats.addValue(pd.getContentTotally().length);
            importantStats.addValue(pd.getImportantContent().length);
        }
        minTotally = totallyStats.getMin();
        maxTotally = totallyStats.getMax();
        minImportant = importantStats.getMin();
        maxImportant = importantStats.getMax();
        medianTotally = totallyStats.getPercentile(50.0D);
        medianImportant = importantStats.getPercentile(50.0D);
        arithmeticAverageTotally = totallyStats.getMean();
        arithmeticAverageImportant = totallyStats.getMean();
        standardDeviationTotally = totallyStats.getStandardDeviation();
        standardDeviationImportant = importantStats.getStandardDeviation();
        for (int i = 1; i <= 3; i++) {
            double percentile = i * 25;
            quartileTotally[i - 1] = totallyStats.getPercentile(percentile);
            quartileImportant[i - 1] = totallyStats.getPercentile(percentile);
        }
        interquartileRangeTotally = this.quartileTotally[2] - this.quartileTotally[0];
        interquartileRangeImportant = this.quartileImportant[2] - this.quartileImportant[0];

        quantile01Totally = totallyStats.getPercentile(10.d);
        quantile09Totally = totallyStats.getPercentile(90.d);
        quantile01Important = importantStats.getPercentile(10.d);
        quantile09Important = importantStats.getPercentile(90.d);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Values Ranges (min-max):\n\tTotally: ").append(minTotally).append(" - ").append(maxTotally);
        sb.append("\n\tWith important words: ").append(minImportant).append(" - ").append(maxImportant);
        sb.append("\n\nMean values and standard deviation:\n\tTotally:\n\t avg = ").append(arithmeticAverageTotally);
        sb.append("\n\t standard deviation = ").append(standardDeviationTotally);
        sb.append("\n\tImportants:\n\t avg = ").append(arithmeticAverageImportant);
        sb.append("\n\t standard deviation = ").append(standardDeviationImportant);
        sb.append("\n\nMedians: \n\t Totally = ").append(medianTotally).append("\n\t Importants = ").append(medianImportant);
        sb.append("\n\nInterquartile ranges for variables (IQR = Q3-Q1):\n\tTotally = ").append(interquartileRangeTotally)
                        .append("\n\tImportants = ").append(interquartileRangeImportant);
        sb.append("\n\nQuantiles: \n\tTotally:\n\t (0.1) = ").append(quantile01Totally).append("\n\t (0.9) = ").append(quantile09Totally)
                        .append("\n\tImportants:\n\t (0.1) = ").append(quantile01Important).append("\n\t (0.9) = ").append(quantile09Important);
        return sb.toString();
    }
}
