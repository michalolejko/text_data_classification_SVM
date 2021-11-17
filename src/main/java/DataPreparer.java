
import java.io.*;
import java.util.*;

public class DataPreparer {

    private final String stopWordsPath = "input/stop_words_english.txt";

    private PreparedData preparedData;
    private File file;

    public DataPreparer(File inputFile) {
        preparedData = new PreparedData();
        preparedData.setContentTotally(DirectoryManager.getContentOfTheSingleFile((File) inputFile).split(" "));
        this.file = inputFile;
    }

    public PreparedData getPreparedData() {
        return preparedData;
    }

    public void generatePreparedData() {
        generatePreparedData(null, null);
    }

    public void generatePreparedData(String outputPathname, File file) {
        if (file == null) {
            if (this.file == null)
                throw new NullPointerException();
            else file = this.file;
        }
        if (outputPathname == null) {
            outputPathname = file.getPath().replace("input", "output");
        }
        DirectoryManager.createDirectory(outputPathname);
        File generatedFile = new File(outputPathname);
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(generatedFile));
            preparedData = prepareData();
            printWriter.println(preparedData);
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PreparedData prepareData() {
        PreparedData result = preparedData;
        Map<String, ArrayList<String>> splittedContent = splitContent(preparedData.getContentTotally());

        //result.setImportantContent(preparedData.getImportantContent());
        result.setContentTotally(preparedData.getContentTotally());
        result.setImportantContent(splittedContent.get("imp").toArray(new String[0]));
        result.setIrrelevantContent(splittedContent.get("irr").toArray(new String[0]));
        result.setFilePath(file.getPath());

        return result;
    }

    private Map<String, ArrayList<String>> splitContent(String[] fullContent) {
        ArrayList<String> importantWords = new ArrayList<>(), irrelevantWords = new ArrayList<>();
        ArrayList<String> stopWords = getStopWords();
        HashMap<String, ArrayList<String>> result = new HashMap();
        for (String word : fullContent)
            if (!stopWords.contains(word))
                importantWords.add(deleteSpecialChar(word));
            else
                irrelevantWords.add(deleteSpecialChar(word));
        result.put("imp", importantWords);
        result.put("irr", irrelevantWords);
        return result;
    }

    private ArrayList<String> getStopWords() {
        ArrayList<String> wordsList = new ArrayList<>();
        try {
            Scanner read = new Scanner(new File(stopWordsPath));
            while (read.hasNextLine())
                wordsList.add(read.nextLine());
            read.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return wordsList;
    }

    private String deleteSpecialChar(String in){
        return in.replaceAll("[^a-zA-Z0-9]", "");
    }
}
