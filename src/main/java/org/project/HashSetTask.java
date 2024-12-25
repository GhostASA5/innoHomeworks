package org.project;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class HashSetTask {

    private static final String readFromFile = "resources/numbersSet.txt";

    private static final String writeToFile = "resources/outputSet.txt";

    public static void main(String[] args) {

        Set<String> set = readArrayFromFile();
        System.out.println("HashSet после добавления: " + set);
        writeToFile(set);

        Collections.addAll(set, "apple", "banana", "cherry", "date", "elderberry");
        System.out.println("HashSet после добавления 5 новых слов: " + set);
        writeToFile(set);

        for (String s : set) {
            System.out.println(s);
        }

        set.add("apple");
        System.out.println("HashSet после добавления дубликата: " + set);
        writeToFile(set);

        System.out.println("Содержит ли множество 'banana': " + set.contains("banana"));

        set.remove("apple");
        System.out.println("HashSet после удаления элемента: " + set);
        writeToFile(set);

        System.out.println("Количество элементов в HashSet: " + set.size());

        set.clear();
        System.out.println("HashSet после очистки: " + set);
        writeToFile(set);

        System.out.println("HashSet пустой: " + set.isEmpty());
    }


    private static HashSet<String> readArrayFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(readFromFile))) {
            String line = br.readLine();
            String[] arr = line.split(";");
            return new HashSet<>(Arrays.stream(arr).toList());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static void writeToFile(Set<String> set) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(writeToFile, true))) {
            for (String s : set) {
                bw.write(s + " ");
            }
            bw.write("\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
