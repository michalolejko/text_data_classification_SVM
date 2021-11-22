
import javax.xml.crypto.Data;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class DataPreparer {

    private final String stopWordsPath = "input/stop_words_english.txt";

    private PreparedData preparedData;
    private File file;
    private boolean generateFiles = false;

    private DataPreparer() {
        super();
    }

    public DataPreparer(File inputFile) {
        this(inputFile, true);
    }

    public DataPreparer(File inputFile, boolean generateFiles) {
        preparedData = new PreparedData();
        preparedData.setContentTotally(DirectoryManager.getContentOfTheSingleFile((File) inputFile).split(" "));
        this.file = inputFile;
        this.generateFiles = generateFiles;
        preparedData = prepareData();
        if(generateFiles == true)
            generatePreparedData();
    }

    public static DataPreparer getSimpleInstance(File inputFile) {
        DataPreparer result = new DataPreparer();
        result.file = inputFile;
        return result;
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
        if (outputPathname == null)
            outputPathname = file.getPath().replace("input", "output");
        DirectoryManager.createDirectory(outputPathname);
        File generatedFile = new File(outputPathname);
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(generatedFile));
            printWriter.println(preparedData);
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToFileAtFirstLine(String toWrite) {
        if(generateFiles == false)
            return;
        Path path = Paths.get(file.getPath().replace("input", "output"));
        try {
            List<String> fileContent = Files.readAllLines(path, StandardCharsets.UTF_8);
            fileContent.add(0, toWrite);
            Files.write(path, fileContent, StandardCharsets.UTF_8);
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

    private String deleteSpecialChar(String in) {
        return in.replaceAll("[^a-zA-Z0-9]", "");
    }
}