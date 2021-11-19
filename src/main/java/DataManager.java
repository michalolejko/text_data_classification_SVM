import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DataManager implements Runnable {

    private String inputPathname, outputPath;
    private CalculatedData data;
    private ArrayList<PreparedData> preparedDataList;
    private ArrayList<PreparedData> remotePointsTotally, remotePointsImportant;
    private Double pearsonLinearCorrelationCoeficient = null;
    private Double regressionCoefficientA = null, regressionCoefficientB = null;
    private File[] files = null;

    public DataManager(String inputPathname) {
        this.inputPathname = inputPathname;
        outputPath = "output/" + (inputPathname.substring(inputPathname.indexOf("/") + 1)
                .replaceAll("/", "_"));
    }

    @Override
    public void run() {
        //punkt 2 - analiza statystyczna
        calculateData();
        //punkt 3 - analiza statystyczna
        identifyRemotePoints();
        //punkt 4 - analiza statystyczna
        calculatePearsonLCC();
        //punkt 6 - analiza statystyczna
        addRegressionInfoToFiles();

        generateRaport();
    }

    private void generateRaport() {
        try {
            PrintWriter printWriter = new PrintWriter(
                    new FileWriter(
                            new File(outputPath.substring(0, outputPath.length() - 1) + ".txt")));
            if (data != null)
                printWriter.println(this.data);
            if (pearsonLinearCorrelationCoeficient != null)
                printWriter.println("\nPearson Linear Correlation Coeficient = "
                        + pearsonLinearCorrelationCoeficient + "\n");
            if (remotePointsImportant != null && remotePointsTotally != null)
                printWriter.println(remotePointsToString());
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFiles() {
        if (files == null)
            files = new DirectoryManager(inputPathname).getFiles();
    }

    /**
     * 2. Wyznaczenie dla zmiennych oznaczających liczbę wszystkich słów w dokumencie oraz
     * liczbę istotnych słów w dokumencie statystyk opisowych:
     *  zakresy wartości, jakie przyjmują zmienne (min-max);
     *  wartości średnie i odchylenie standardowe,
     *  mediana zmiennych,
     *  rozstępy międzykwartylowe dla zmiennych (IQR=Q3-Q1),
     *  kwantyle rzędu 0.1, 0.9.
     */
    private void calculateData() {
        loadFiles();
        preparedDataList = new ArrayList<>();
        for (File file : files) {
            DataPreparer dataPreparer = new DataPreparer(file);
            preparedDataList.add(dataPreparer.getPreparedData());
        }
        this.data = new CalculatedData(preparedDataList);
    }

    /**
     * 3. Identyfikację punktów oddalonych (dokumentów tekstowych dla których długość słów
     * spełnia określone warunki).
     */
    private void identifyRemotePoints() {
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

    private String remotePointsToString() {
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
     * 4. Wyznaczenie współczynnika korelacji liniowej Pearsona pomiędzy liczbą słów a klasą
     */
    private void calculatePearsonLCC() {
        //remotePointsTotally, remotePointsImportant;
        double covariance = 0;
        int n = preparedDataList.size();
        for (int i = 0; i < n; i++)
            covariance +=
                    ((double) preparedDataList.get(i).getContentTotally().length - data.arithmeticAverageTotally)
                            * ((double) preparedDataList.get(i).getImportantContent().length - data.arithmeticAverageImportant);
        pearsonLinearCorrelationCoeficient = covariance / (data.standardDeviationImportant * data.standardDeviationTotally);
    }

    /**
     * 6. Dokonanie prostej regresji liniowej pomiędzy wybranymi parami zmiennych.
     * [ PS. Szukamy przewidywanej liczby słów kluczowych względem liczby wszystkich słów. ]
     */
    private void addRegressionInfoToFiles() {
        loadFiles();
        for(int i = 0; i < files.length; i++){
            DataPreparer.getSimpleInstance(files[i]).writeToFileAtFirstLine( "Regression prediction for important words = "
                    + getRegression(preparedDataList.get(i).getContentTotally().length));
        }
    }

    // Obliczanie Parametrów Liniowej Funkcji Regresji
    private void calculateParametersOfLinearRegressionFunction() {
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

    public double getRegression(int totalLengthOfContent) {
        if (regressionCoefficientA == null || regressionCoefficientB == null)
            calculateParametersOfLinearRegressionFunction();
        return totalLengthOfContent * regressionCoefficientB + regressionCoefficientA;
    }
}
