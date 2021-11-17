import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CalculatedData {
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
        //MANUALNE OBLICZENIA
        /*
        updateMinMax();
        updateMedians();
        updateAverages();
        updateStandardDeviations();
        updateInterquartileStrechMarks();
        updateQuartile();
        */
        //APACHE math3
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

/*
    private void updateQuartile(){
        DescriptiveStatistics totallyStats = new DescriptiveStatistics(), importantStats = new DescriptiveStatistics();
        for (PreparedData pd : preparedDataList) {
            totallyStats.addValue(pd.getContentTotally().length);
            importantStats.addValue(pd.getImportantContent().length);
        }
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

    private void updateStandardDeviations() {
        float t = 0, i = 0;
        for (PreparedData pd : preparedDataList) {
            t += Math.pow((float) pd.getContentTotally().length - arithmeticAverageTotally, 2);
            i += Math.pow((float) pd.getImportantContent().length - arithmeticAverageImportant, 2);
        }
        standardDeviationTotally = (double) Math.sqrt(t / preparedDataList.size());
        standardDeviationImportant = (double) Math.sqrt(i / preparedDataList.size());
    }

    private void updateAverages() {
        int t = 0, i = 0;
        for (PreparedData pd : preparedDataList) {
            t += pd.getContentTotally().length;
            i += pd.getImportantContent().length;
        }
        arithmeticAverageTotally = (double) t / (double) preparedDataList.size();
        arithmeticAverageImportant = (double) i / (double) preparedDataList.size();
    }

    private void updateMedians() {

        ArrayList<PreparedData> totalliesList = new ArrayList<>(preparedDataList);
        ArrayList<PreparedData> importantsList = new ArrayList<>(preparedDataList);
        Collections.sort(totalliesList, new TotalliesComparator());
        Collections.sort(importantsList, new ImportantsComparator());

        if (totalliesList.size() % 2 == 0)
            medianTotally = ((double) totalliesList.get(totalliesList.size() / 2).getContentTotally().length
                    + (double) totalliesList.get(totalliesList.size() / 2 - 1).getContentTotally().length) / 2.d;
        else
            medianTotally = totalliesList.get(totalliesList.size() / 2).getContentTotally().length;

        if (importantsList.size() % 2 == 0)
            medianImportant = ((double) importantsList.get(importantsList.size() / 2).getContentTotally().length
                    + (double) importantsList.get(importantsList.size() / 2 - 1).getContentTotally().length) / 2.d;
        else
            medianImportant = importantsList.get(importantsList.size() / 2).getContentTotally().length;
    }

    private void updateMinMax() {
        for (PreparedData pd : preparedDataList) {
            if (pd.getContentTotally().length > maxTotally)
                maxTotally = pd.getContentTotally().length;
            else if (pd.getContentTotally().length < minTotally)
                minTotally = pd.getContentTotally().length;

            if (pd.getImportantContent().length > maxImportant)
                maxImportant = pd.getImportantContent().length;
            else if (pd.getImportantContent().length < minImportant)
                minImportant = pd.getImportantContent().length;
        }
    }

    private void updateInterquartileStrechMarks() {
        for (int quartileType = 1; quartileType < 4; quartileType++) {
            float length = preparedDataList.size() + 1;
            double quartileTotally, quartileImportant;
            float quartileArray = (length * ((float) (quartileType) * 25 / 100)) - 1;
            if (quartileArray % 1 == 0) {
                quartileTotally = preparedDataList.get((int) quartileArray).getContentTotally().length;
                quartileImportant = preparedDataList.get((int) quartileArray).getImportantContent().length;
            } else {
                quartileTotally = (preparedDataList.get((int) (quartileArray)).getContentTotally().length
                        + preparedDataList.get((int) (quartileArray) + 1).getImportantContent().length) / 2;
                quartileImportant = (preparedDataList.get((int) (quartileArray)).getContentTotally().length
                        + preparedDataList.get((int) (quartileArray) + 1).getImportantContent().length) / 2;
            }
            this.quartileTotally[quartileType - 1] = quartileTotally;
            this.quartileImportant[quartileType - 1] = quartileImportant;
        }
        interquartileRangeTotally = this.quartileTotally[2] - this.quartileTotally[0];
        interquartileRangeImportant = this.quartileImportant[2] - this.quartileImportant[0];
    }

    private class TotalliesComparator implements Comparator<PreparedData> {
        @Override
        public int compare(PreparedData o1, PreparedData o2) {
            return o1.getContentTotally().length - o2.getContentTotally().length;
        }
    }

    private class ImportantsComparator implements Comparator<PreparedData> {
        @Override
        public int compare(PreparedData o1, PreparedData o2) {
            return o1.getImportantContent().length - o2.getImportantContent().length;
        }
    }
*/
}
