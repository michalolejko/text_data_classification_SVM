package statistical;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MainManagement {

    private static final String[] aclImbdPaths = {
            "input/aclImdb/train/pos/",
            "input/aclImdb/train/neg/",
            // "input/aclImdb/train/unsup/", //Nie obslugujemy juz tego
            "input/aclImdb/test/pos/",
            "input/aclImdb/test/neg/"
    };

    private static final String[] amazonPaths = {
            "input/amazon/test.ft.txt",
            "input/amazon/train.ft.txt"
    };


    public static DataManager[] calculateStatisticalDataAcllmbd(int delimiter, boolean generateTxtFiles, boolean generateCharts) {
        DataManagerAcllmbd[] dataManagerAcllmbds = new DataManagerAcllmbd[aclImbdPaths.length];
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < aclImbdPaths.length; i++) {
            dataManagerAcllmbds[i] = new DataManagerAcllmbd(delimiter, aclImbdPaths[i]);

            dataManagerAcllmbds[i].setGenerateFiles(generateTxtFiles);
            dataManagerAcllmbds[i].setGenerateCharts(generateCharts);

            if (aclImbdPaths[i].contains("train"))
                dataManagerAcllmbds[i].setTrainData();
            else
                dataManagerAcllmbds[i].setTestData();

            if (aclImbdPaths[i].contains("pos"))
                dataManagerAcllmbds[i].setPositiveData();
            else
                dataManagerAcllmbds[i].setNegativeData();
        }
        //start threads
        for (int i = 0; i < dataManagerAcllmbds.length; ++i) {
            Thread t = new Thread(dataManagerAcllmbds[i]);
            t.start();
            threads.add(t);
        }
        //Wait for threads
        for (Thread t : threads)
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        return dataManagerAcllmbds;
    }

    public static DataManager[] calculateStatisticalDataAmazon(int delimiter, boolean generateTxtFiles, boolean generateCharts) {
        DataManagerAmazon[] dataManagerAmazon = new DataManagerAmazon[amazonPaths.length * 2];
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < amazonPaths.length; i++) {
            try {
                int index = i * 2;
                dataManagerAmazon[index] = new DataManagerAmazon();
                dataManagerAmazon[index + 1] = new DataManagerAmazon();

                dataManagerAmazon[index].setPositiveData();
                dataManagerAmazon[index].setGenerateFiles(generateTxtFiles);
                dataManagerAmazon[index].setGenerateCharts(generateCharts);
                dataManagerAmazon[index].setInputPathname(amazonPaths[i]);

                dataManagerAmazon[index + 1].setNegativeData();
                dataManagerAmazon[index + 1].setGenerateFiles(generateTxtFiles);
                dataManagerAmazon[index + 1].setGenerateCharts(generateCharts);
                dataManagerAmazon[index + 1].setInputPathname(amazonPaths[i]);

                if (amazonPaths[i].contains("test")) {
                    dataManagerAmazon[index].setTestData();
                    dataManagerAmazon[index + 1].setTestData();
                } else {
                    dataManagerAmazon[index].setTrainData();
                    dataManagerAmazon[index + 1].setTrainData();
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(amazonPaths[i])));
                for (int j = 0; j < delimiter; ++j) {
                    String line = br.readLine();
                    if (line == null)
                        break;
                    if (line.startsWith("__label__1"))
                        dataManagerAmazon[index].dataToPrepare.add(line.substring(line.indexOf(" ")));
                    else
                        dataManagerAmazon[index + 1].dataToPrepare.add(line.substring(line.indexOf(" ")));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                System.out.println("Zwieksz pamiec dla JVM lub zmniejsz wartosc zmiennej w 'Main.java' zmienna static final int  maxRowsFromAmazon");
                e.printStackTrace();
            }
        }
        //start threads
        for (int i = 0; i < dataManagerAmazon.length; ++i) {
            Thread t = new Thread(dataManagerAmazon[i]);
            t.start();
            threads.add(t);
        }
        //Wait for threads
        for (int i = 0; i < threads.size(); i++) {
            //for (Thread t : threads)
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return dataManagerAmazon;
    }
}
