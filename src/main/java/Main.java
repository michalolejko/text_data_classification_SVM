import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String[] aclImbdPaths = {
                "input/aclImdb/train/pos/",
                "input/aclImdb/train/neg/",
                "input/aclImdb/train/unsup/",
                "input/aclImdb/test/pos/",
                "input/aclImdb/test/neg/"
        };

        DataManager[] dataManagers = calculateStatisticalData(aclImbdPaths);
    }

    private static DataManager[] calculateStatisticalData(String[] filesPaths){
        if(filesPaths.length <= 0)
            return null;
        DataManager[] dataManagers = new DataManager[filesPaths.length];
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i< filesPaths.length;i++) {
            dataManagers[i] = new DataManager(filesPaths[i]);
            //false = bez generowania plikow, true = generowanie plikow (pliki .txt)
            dataManagers[i].setGenerateFiles(false);
        }
        //start threads
        for (int i = 0; i < dataManagers.length; ++i) {
            Thread t = new Thread(dataManagers[i]);
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
        return dataManagers;
    }
}
