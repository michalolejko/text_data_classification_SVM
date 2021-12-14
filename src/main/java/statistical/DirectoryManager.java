package statistical;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class DirectoryManager {

    private File[] filesInDir;

    public DirectoryManager(String pathname) {
        File dirFile = new File(pathname);
        dirFile.mkdirs();
        this.filesInDir = dirFile.listFiles();
    }

    public File[] getFiles() {
        return filesInDir;
    }

    public void printAllRecords() {
        if (filesInDir == null || filesInDir.length == 0) {
            System.out.println("Nothing found");
            return;
        }
        for (File file : filesInDir)
            System.out.println(file.getName());
    }

    public String[] getContentOfTheFiles() {
        List<String> contentList = new LinkedList<>();
        for (File file : filesInDir)
            contentList.add(getContentOfTheSingleFile(file));
        return contentList.toArray(new String[0]);
    }

    public static String getContentOfTheSingleFile(File file) {
        Scanner read;
        StringBuilder stringBuilder;
        try {
            read = new Scanner(file);
            stringBuilder = new StringBuilder();
            while (read.hasNextLine())
                stringBuilder.append(read.nextLine());
            read.close();
            return stringBuilder.toString().toLowerCase();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean createDirectory(String path) {
        if (path.contains(".") && !path.endsWith("\\"))
            path = path.substring(0, path.lastIndexOf("\\") + 1);
        return !new File(path).mkdirs();
    }
}
