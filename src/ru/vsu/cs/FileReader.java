package ru.vsu.cs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileReader {
    private File file;

    public FileReader(String path) {
        this.file = new File(path);
    }

    public String readAll() throws FileNotFoundException {
        Scanner scan = new Scanner(this.file);
        StringBuilder sb = new StringBuilder();
        while (scan.hasNext()) {
            sb.append(scan.nextLine());
            sb.append("\n");
        }
        return sb.toString();
    }
}
