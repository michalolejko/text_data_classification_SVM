import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        List<Thread> threads = new ArrayList<>();
        String[] filesPath = {
                "input/aclImdb/train/pos/",
                "input/aclImdb/train/neg/",
                "input/aclImdb/train/unsup/",
                "input/aclImdb/test/pos/",
                "input/aclImdb/test/neg/"
        };
        DataManager[] dataManagers = new DataManager[filesPath.length];
        for (int i = 0; i< filesPath.length;i++)
            dataManagers[i] = new DataManager(filesPath[i]);

        //start threads
        for (int i = 0; i < dataManagers.length; ++i) {
            Thread t = new Thread(dataManagers[i]);
            t.start();
            threads.add(t);
        }
        //Wait for threads
        for (Thread t : threads)
            t.join();
    }
}
