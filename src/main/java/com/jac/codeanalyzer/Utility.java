package com.jac.codeanalyzer;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Utility {

    public int[] countLines(String filePath) {
        int blankLineCount = 0;
        int commentLineCount = 0;
        int codeLineCount = 0;

        try (Scanner input = new Scanner(Paths.get(filePath)).useDelimiter("[\r\n]")) {
            String line;
            while (input.hasNextLine()) {
                line = input.nextLine().trim();
                // System.out.println(line);
                if (line.isEmpty()) {
                    blankLineCount++;
                } else if (line.startsWith("//") || (line.startsWith("/*") && line.endsWith("*/"))) {
                    commentLineCount++;
                } else if (line.startsWith("/*")) {
                    commentLineCount++;
                    while (input.hasNextLine()) {
                        line = input.nextLine().trim();
                        if (line.endsWith("*/")) {
                            commentLineCount++;
                            break;
                        } else {
                            commentLineCount++;
                        }
                    }
                } else {
                    codeLineCount++;
                }
            }
            blankLineCount++; // last empty line
        } catch (Exception e) {
            System.out.println("Error openning file");
        }
        return new int[] { blankLineCount, commentLineCount, codeLineCount };
    }

    public void search(final String pattern, final File projectFolder, List<String> javaFileList) {
        for (final File f : projectFolder.listFiles()) {

            if (f.isDirectory()) {
                search(pattern, f, javaFileList);
            }

            if (f.isFile()) {
                if (f.getName().matches(pattern)) {
                    javaFileList.add(f.getAbsolutePath());
                }
            }

        }
    }

    public String md5(String filePath) {
        try {
            InputStream is = Files.newInputStream(Paths.get(filePath));
            String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(is);
            return md5;
        } catch (Exception e) {
            System.out.println("Error Creating MD5");
            return "";
        }
    }

}
