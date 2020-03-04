package com.jac.codeanalyzer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class App {

    int totalNumberOfJavaFiles;
    int numberOfUniqueJavaFiles;
    int totalBlankLineCount;
    int totalCommentLineCount;
    int totalCodeLineCount;
    Utility u;

    public App() {
        this.u = new Utility();
    }

    public void analyzeFile(File projectPath) {
        // System.out.println("\n" + javaFilePath);
        int[] fileAnalysisResult = u.countLines(projectPath.getAbsolutePath());
        this.totalBlankLineCount += fileAnalysisResult[0];
        this.totalCommentLineCount += fileAnalysisResult[1];
        this.totalCodeLineCount += fileAnalysisResult[2];
        this.totalNumberOfJavaFiles = 1;
        this.numberOfUniqueJavaFiles = 1;
    }

    public void analyzeFolder(File projectPath) {
        // Fetch a list of all .java files
        List<String> javaFileList = new ArrayList<>();
        u.search(".*\\.java", projectPath, javaFileList);
        this.totalNumberOfJavaFiles = javaFileList.size();

        // Calculate MD-5 Hash for all files
        List<String> md5FileList = new ArrayList<>();
        for (String filePath : javaFileList) {
            md5FileList.add(u.md5(filePath));
        }

        // Remove duplicates
        for (int i = md5FileList.size() - 1; i > 0; i--) {
            if (md5FileList.indexOf(md5FileList.get(i)) < i) {
                javaFileList.remove(i);
            }
        }
        md5FileList.clear();
        this.numberOfUniqueJavaFiles = javaFileList.size();

        for (String javaFilePath : javaFileList) {
            int[] fileAnalysisResult = u.countLines(javaFilePath);
            this.totalBlankLineCount += fileAnalysisResult[0];
            this.totalCommentLineCount += fileAnalysisResult[1];
            this.totalCodeLineCount += fileAnalysisResult[2];
        }
    }

    public static void main(String[] args) {
        App app = new App();

        final File projectPath = new File(args[0]);

        if (projectPath.isFile()) {
            if (args[0].endsWith(".java")) {
                app.analyzeFile(projectPath);
            } else {
                System.out.println("Please provide a Java file or a folder.");
            }
        } else {
            app.analyzeFolder(projectPath);
        }

        // Print Output
        System.out.printf("Total Number of Java Files:\t %d\n" +
                        "Number of Unique Java Files:\t %d\n" +
                        "Total Number of Blank Lines:\t %d\n" +
                        "Total Number of Comment Lines:\t %d\n" +
                        "Total Number of Code Lines:\t %d\n",
                app.totalNumberOfJavaFiles,
                app.numberOfUniqueJavaFiles,
                app.totalBlankLineCount,
                app.totalCommentLineCount,
                app.totalCodeLineCount);
    }
}

