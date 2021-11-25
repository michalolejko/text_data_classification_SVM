package statistical;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

public class PreparedData implements Serializable {

    private String filePath;
    private String[] contentTotally, irrelevantContent, importantContent;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String[] getContentTotally() {
        return contentTotally;
    }

    public void setContentTotally(String[] contentTotally) {
        this.contentTotally = contentTotally;
    }

    public String[] getIrrelevantContent() {
        return irrelevantContent;
    }

    public void setIrrelevantContent(String[] irrelevantContent) {
        this.irrelevantContent = irrelevantContent;
    }

    public String[] getImportantContent() {
        return importantContent;
    }

    public void setImportantContent(String[] importantContent) {
        this.importantContent = importantContent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("__________________________________________________________________________________________\n");
        sb.append("Words count: \ntotally = ").append(contentTotally.length);
        if (irrelevantContent != null)
            sb.append("\nirrelevant = ").append(irrelevantContent.length);
        sb.append("\nimportant = ").append(importantContent.length);
        sb.append("\n__________________________________________________________________________________________\n");
        sb.append("\nContent: (toLowerCase): \n");
        sb.append("\n__________________________________________________________________________________________\n");
        sb.append("Original:\n");
        for (String el : contentTotally) {
            sb.append(el);
            sb.append(" ");
        }
        sb.append("\n__________________________________________________________________________________________\n");
        if (irrelevantContent != null) {
            sb.append("\n\nIrrelevant:\n");
            for (String el : irrelevantContent) {
                sb.append(el);
                sb.append(" ");
            }
        }
        sb.append("\n__________________________________________________________________________________________\n");
        sb.append("\n\nImportant:\n");
        for (String el : importantContent) {
            sb.append(el);
            sb.append(" ");
        }
        sb.append("\n__________________________________________________________________________________________\n");
        return sb.toString();
    }

    public String toCsvFormat(){
        StringBuilder sb = new StringBuilder();
        sb.append("Totally,").append(oneArrayToCsvFormat(contentTotally));
        sb.append("Important,").append(oneArrayToCsvFormat(importantContent));
        sb.append("Irrelevant,").append(oneArrayToCsvFormat(irrelevantContent));
        return sb.toString();
    }

    private StringBuilder oneArrayToCsvFormat(String[] array){
        Iterator<String> iterator = Arrays.asList(array).iterator();
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()) {
            sb.append(iterator.next());
            if (iterator.hasNext()) {
                sb.append(',');
            }
        }
        sb.append("\n");
        return sb;
    }
}
