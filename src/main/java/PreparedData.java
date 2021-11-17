
public class PreparedData {


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
        sb.append("Words count: \ntotally = ").append(contentTotally.length)
                .append("\nirrelevant = ").append(irrelevantContent.length)
                .append("\nimportant = ").append(importantContent.length);
        sb.append("\n__________________________________________________________________________________________\n");
        sb.append("\nContent: (toLowerCase): \n");
        sb.append("\n__________________________________________________________________________________________\n");
        sb.append("Original:\n");
        for (String el : contentTotally) {
            sb.append(el);
            sb.append(" ");
        }
        sb.append("\n__________________________________________________________________________________________\n");
        sb.append("\n\nIrrelevant:\n");
        for (String el : irrelevantContent) {
            sb.append(el);
            sb.append(" ");
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
}
