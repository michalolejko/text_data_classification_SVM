package statistical;

import java.io.*;
import java.util.ArrayList;

public class StatisticalManagerAcllmbd extends StatisticalManager {

    private File[] files = null;
    private int delimiter;

    public StatisticalManagerAcllmbd(int delimiter, String inputPathname) {
        this.inputPathname = inputPathname;
        outputPath = "output/" + (inputPathname.substring(inputPathname.indexOf("/") + 1)
                .replaceAll("/", "_"));
        loadFiles();
        this.delimiter = Math.min(delimiter, files.length);
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
        generateFilesAndCharts();
        runExplore();
    }

    protected void generateRaport() {
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

    protected void calculateData() {
        loadFiles();
        preparedDataList = new ArrayList<>();
        for(int i = 0; i < delimiter; i++){
            DataPreparer dataPreparer = new DataPreparer(files[i], generateFiles);
            preparedDataList.add(dataPreparer.getPreparedData());
        }
        super.calculateData();
    }

    protected void addRegressionInfoToFiles() {
        if(!generateFiles)
            return;
        loadFiles();
        for (int i = 0; i < delimiter; i++) {
            DataPreparer.getSimpleInstance(files[i]).writeToFileAtFirstLine("Regression prediction for important words = "
                    + getRegression(preparedDataList.get(i).getImportantContent().length));
        }
    }
}
