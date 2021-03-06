package statistical;

import java.io.*;
import java.util.ArrayList;

public abstract class StatisticalManager implements Runnable, Serializable {

    protected String inputPathname, outputPath;
    protected CalculatedData data;
    protected ArrayList<PreparedData> preparedDataList;
    protected ArrayList<PreparedData> remotePointsTotally, remotePointsImportant;
    protected Double pearsonLinearCorrelationCoeficient = null;
    protected Double regressionCoefficientA = null, regressionCoefficientB = null;
    protected boolean isPositiveData = true, isNegativeData = false, isTrainData = true, isTestData = false;

    protected boolean generateFiles = false, generateCharts = false;

    public void setPositiveData() {
        isPositiveData = true;
        isNegativeData = false;
    }

    public void setNegativeData() {
        isPositiveData = false;
        isNegativeData = true;
    }

    public void setTrainData() {
        isTrainData = true;
        isTestData = false;
    }

    public void setTestData() {
        isTrainData = false;
        isTestData = true;
    }

    /**
     * @return info about statistical data like minimums, maximums, medians (means),
     * averages, quartiles, interquartiles, qunatiles separetly for important words and all of words
     */
    public CalculatedData getData() {
        return data;
    }

    /**
     * @return a list with file information, String arrays containing important words, unimportant words and all words
     */
    public ArrayList<PreparedData> getPreparedDataList() {
        return preparedDataList;
    }

    /**
     * @return a list of distant (remote) points for all words
     */
    public ArrayList<PreparedData> getRemotePointsTotally() {
        return remotePointsTotally;
    }

    /**
     * @return a list of distant (remote) points for important words
     */
    public ArrayList<PreparedData> getRemotePointsImportant() {
        return remotePointsImportant;
    }

    public Double getPearsonLinearCorrelationCoeficient() {
        return pearsonLinearCorrelationCoeficient;
    }

    public double[] getRegressionArray(PreparedData[] pd) {
        double[] resultArray = new double[pd.length];
        for (int i = 0; i < resultArray.length; i++)
            resultArray[i] = getRegression(pd[i].getImportantContent().length);
        return resultArray;
    }

    public double getRegression(PreparedData pd) {
        return getRegression(pd.getImportantContent().length);
    }

    public double getRegression(int totalLengthOfContent) {
        if (regressionCoefficientA == null || regressionCoefficientB == null)
            calculateParametersOfLinearRegressionFunction();
        return totalLengthOfContent * regressionCoefficientB + regressionCoefficientA;
    }

    @Override
    public void run() {
    }

    protected void generateFilesAndCharts() {
        if (generateFiles)
            generateRaport();
        if (generateCharts)
            generateCharts();
    }

    protected void generateCharts() {
        if (inputPathname == null) {
            System.out.println("Nie podano sciezki dla wygenerowania wykresow.");
            return;
        }
        try {
            ChartsGenerator chartsGenerator = new ChartsGenerator(inputPathname.substring(inputPathname.indexOf("/") + 1));
            chartsGenerator.generateWordsLineChart(getPreparedDataList());
            chartsGenerator.generateHistogram(getPreparedDataList());
            chartsGenerator.generateBoxCharts(getPreparedDataList(), getData());
            chartsGenerator.generateScatterPlot(getPreparedDataList(), getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a report files if the method setGenerateFiles is set to true.
     */
    protected void generateRaport() {
    }

    public void setGenerateCharts(boolean generateCharts) {
        this.generateCharts = generateCharts;
    }

    public void setGenerateFiles(boolean isFilesNeedToBeGenerated) {
        generateFiles = isFilesNeedToBeGenerated;
    }


    /**
     * 2. Wyznaczenie dla zmiennych oznaczaj??cych liczb?? wszystkich s????w w dokumencie oraz
     * liczb?? istotnych s????w w dokumencie statystyk opisowych:
     * ??? zakresy warto??ci, jakie przyjmuj?? zmienne (min-max);
     * ??? warto??ci ??rednie i odchylenie standardowe,
     * ??? mediana zmiennych,
     * ??? rozst??py mi??dzykwartylowe dla zmiennych (IQR=Q3-Q1),
     * ??? kwantyle rz??du 0.1, 0.9.
     */
    protected void calculateData() {
        this.data = new CalculatedData(preparedDataList);
    }

    /**
     * 3. Identyfikacj?? punkt??w oddalonych (dokument??w tekstowych dla kt??rych d??ugo???? s????w
     * spe??nia okre??lone warunki).
     */
    protected void identifyRemotePoints() {
        if (preparedDataList == null) {
            remotePointsTotally = null;
            remotePointsImportant = null;
            return;
        }
        remotePointsTotally = new ArrayList<>();
        remotePointsImportant = new ArrayList<>();
        for (PreparedData pd : preparedDataList) {
            if ((double) pd.getContentTotally().length < this.data.quantile01Totally
                    || (double) pd.getContentTotally().length > this.data.quantile09Totally)
                remotePointsTotally.add(pd);
            if ((double) pd.getImportantContent().length < this.data.quantile01Important
                    || (double) pd.getImportantContent().length > this.data.quantile09Important)
                remotePointsImportant.add(pd);
        }
    }

    protected String remotePointsToString() {
        if (remotePointsTotally == null || remotePointsImportant == null)
            return "";
        StringBuilder sb = new StringBuilder();
        sb.append("\n\nRemote points for length of totally content: \n");
        for (PreparedData pd : remotePointsTotally)
            sb.append("\t").append(pd.getFilePath()).append(" - content size = ").append(pd.getContentTotally().length).append("\n");
        sb.append("\n\nRemote points for length of important content only: \n");
        for (PreparedData pd : remotePointsTotally)
            sb.append("\t").append(pd.getFilePath()).append(" - content size = ").append(pd.getImportantContent().length).append("\n");
        return sb.toString();
    }

    /**
     * 4. Wyznaczenie wsp????czynnika korelacji liniowej Pearsona pomi??dzy liczb?? s????w a klas??
     */
    protected void calculatePearsonLCC() {
        double covariance = 0;
        int n = preparedDataList.size();
        for (int i = 0; i < n; i++)
            covariance +=
                    ((double) preparedDataList.get(i).getContentTotally().length - data.arithmeticAverageTotally)
                            * ((double) preparedDataList.get(i).getImportantContent().length - data.arithmeticAverageImportant);
        pearsonLinearCorrelationCoeficient = covariance / (data.standardDeviationImportant * data.standardDeviationTotally);
    }

    /**
     * 6. Dokonanie prostej regresji liniowej pomi??dzy wybranymi parami zmiennych.
     * [ PS. Szukamy przewidywanej liczby s????w kluczowych wzgl??dem liczby wszystkich s????w. ]
     */
    // Obliczanie Parametr??w Liniowej Funkcji Regresji
    protected void calculateParametersOfLinearRegressionFunction() {
        // x -> totallies; y -> importants;
        int nxy = 0, nxPow = 0, nx = 0, ny = 0, x, y;
        for (PreparedData pd : preparedDataList) {
            x = pd.getContentTotally().length;
            y = pd.getImportantContent().length;
            nxy += x * y;
            nxPow += x * x;
            nx += x;
            ny += y;
        }
        nxy *= preparedDataList.size();
        nxPow *= preparedDataList.size();
        regressionCoefficientB = ((double) (nxy - (nx * ny))) / ((double) (nxPow - (nx * nx)));
        regressionCoefficientA = data.arithmeticAverageImportant - (regressionCoefficientB * data.arithmeticAverageTotally);
    }
}
